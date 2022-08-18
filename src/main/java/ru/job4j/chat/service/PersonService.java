package ru.job4j.chat.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.aspect.NonLoggableParameter;
import ru.job4j.chat.exception.EntityNotFoundException;
import ru.job4j.chat.exception.LoginReservedException;
import ru.job4j.chat.mapper.PersonDtoMapper;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;

@Service
@Loggable
public class PersonService implements GenericService<Person> {

    private final PersonRepository personRepository;
    private final PersonDtoMapper personMapper;
    private final PasswordEncoder encoder;

    public PersonService(PersonRepository personRepository, PersonDtoMapper personMapper, PasswordEncoder encoder) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public Person save(@NonLoggableParameter Person person) {
        if (personRepository.findByLogin(person.getLogin()).isPresent()) {
            throw new LoginReservedException(String.format("login '%s' already reserved please try again",
                    person.getLogin()));
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Person partialUpdate(@NonLoggableParameter Person person) {
        Person oldPerson = getById(person.getId());
        personMapper.updateNewNotNullFieldsToPerson(person, oldPerson);
        return save(oldPerson);
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
    @Transactional
    public void delete(@NonLoggableParameter Person person) {
        long id = person.getId();
        if (personRepository.findById(person.getId()).isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("can't delete, user with id = %d wasn't found.", id));
        }
        personRepository.delete(person);
    }
}
