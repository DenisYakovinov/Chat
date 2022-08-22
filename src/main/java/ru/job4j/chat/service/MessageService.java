package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.exception.EntityNotFoundException;
import ru.job4j.chat.exception.ServiceValidateException;
import ru.job4j.chat.mapper.MessageDtoMapper;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

import java.util.List;
import java.util.Objects;

@Service
@Loggable
public class MessageService implements GenericService<Message> {

    private final MessageRepository messageRepository;
    private final MessageDtoMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageDtoMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message getById(long id) {
        return messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("message with id = %d wasn't found", id)));
    }

    @Override
    public Message save(Message message) {
        if (message.getRoom().getId() == 0) {
            throw new ServiceValidateException("need to specify the room id");
        }
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public Message update(Message message) {
        long id = message.getId();
        if (!messageRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("can't update, message with id = %d wasn't found", id));
        }
        Message oldMessage = getById(message.getId());
        checkMessageOwner(message, oldMessage);
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public Message partialUpdate(Message message) {
        Message oldMessage = getById(message.getId());
        checkMessageOwner(message, oldMessage);
        messageMapper.updateNewNotNullFieldsToMessage(message, oldMessage);
        return save(oldMessage);
    }

    @Override
    @Transactional
    public void delete(Message message) {
        Message oldMessage = getById(message.getId());
        checkMessageOwner(message, oldMessage);
        messageRepository.delete(message);
    }

    private void checkMessageOwner(Message newMessage, Message oldMessage) {
        if (!Objects.equals(newMessage.getPerson().getId(), oldMessage.getPerson().getId())) {
            throw new ServiceValidateException(String.format("another user's message (id = %d)", newMessage.getId()));
        }
    }
}