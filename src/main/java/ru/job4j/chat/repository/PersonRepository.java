package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByLogin(String login);

    @Override
    List<Person> findAll();
}
