package ru.job4j.chat.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.exception.ServiceException;
import ru.job4j.chat.exception.LoginReservedException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    public UserDetailsServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person save(Person person) {
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

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        personRepository.findAll().forEach(persons::add);
        return persons;
    }

    public Optional<Person> findById(long id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByLogin(String login) {
        return Optional.ofNullable(personRepository.findByLogin(login));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username);
        if (person == null) {
            throw new UsernameNotFoundException("user was not found");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(person.getRole().getName());
        return new User(person.getLogin(), person.getPassword(), List.of(authority));
    }
}
