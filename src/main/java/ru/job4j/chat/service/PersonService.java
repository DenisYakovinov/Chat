package ru.job4j.chat.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.aspect.NonLoggableParameter;
import ru.job4j.chat.exception.EntityNotFoundException;
import ru.job4j.chat.exception.ServiceException;
import ru.job4j.chat.exception.LoginReservedException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;

@Service
@Loggable
public class PersonService implements UserDetailsService, GenericService<Person> {

    private final PersonRepository personRepository;

    private final PasswordEncoder encoder;

    public PersonService(final PersonRepository personRepository, PasswordEncoder encoder) {
        this.personRepository = personRepository;
        this.encoder = encoder;
    }

    @Override
    public Person save(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        try {
            return personRepository.save(person);
        } catch (DataAccessException e) {
            if ((e.getMostSpecificCause()) instanceof PSQLException) {
                PSQLException sqlEx = (PSQLException) e.getMostSpecificCause();
                if (sqlEx.getSQLState().equals("23505")) {
                    throw new LoginReservedException(String.format("login already reserved please try again (%s)",
                            sqlEx.getMessage()), sqlEx);
                }
            }
            throw new ServiceException(String.format("User %s can't be added (%s)", person, e.getMessage()), e);
        }
    }

    @Override
    public void delete(@NonLoggableParameter Person person) {
        personRepository.delete(person);
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person getById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("user with id = %d wasn't found.", id)));
    }

    public Person findByLogin(String login) {
        return personRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(
                String.format("user with login %s wasn't found.", login)));
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Person person = findByLogin(login);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(person.getRole().getName());
        return new User(person.getLogin(), person.getPassword(), List.of(authority));
    }
}
