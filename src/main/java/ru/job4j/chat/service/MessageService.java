package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        messageRepository.findAll().forEach(messages::add);
        return messages;
    }

    public Message getById(long id) {
        return messageRepository.findById(id).orElse(new Message());
    }

    public Message createOrUpdate(Message message) {
        return messageRepository.save(message);
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }
}
