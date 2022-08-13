package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    @Override
    List<Message> findAll();
}
