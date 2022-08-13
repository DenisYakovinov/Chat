package ru.job4j.chat.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.job4j.chat.exception.EntityNotFoundException;
import ru.job4j.chat.exception.ServiceValidateException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.MessageRepository;

import java.util.List;
import java.util.Objects;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final PersonService personService;

    public MessageService(MessageRepository messageRepository, PersonService personService) {
        this.messageRepository = messageRepository;
        this.personService = personService;
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public Message getById(long id) {
        return messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("message with id = %d wasn't found", id)));
    }

    public Message save(Message message) {
        if (message.getRoom().getId() == 0) {
            throw new ServiceValidateException("need to specify the room id");
        }
        if (message.getText() == null || message.getText().trim().length() == 0) {
            throw new ServiceValidateException("message text mustn't be empty");
        }
        Person requestPerson = personService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setPerson(requestPerson);
        return messageRepository.save(message);
    }

    public void delete(long id) {
        Message message = getById(id);
        Person requestUser = personService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (!Objects.equals(requestUser.getId(), message.getPerson().getId())) {
            throw new ServiceValidateException(String.format("another user's message (id = %d)", id));
        }
        messageRepository.delete(message);
    }
}