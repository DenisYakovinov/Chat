package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.UserDetailsServiceImpl;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final UserDetailsServiceImpl userService;

    public MessageController(MessageService messageService, UserDetailsServiceImpl userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/")
    public List<Message> getAll() {
        return messageService.getAll();
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getRoom().getId() == 0) {
            return new ResponseEntity("need to specify the room id", HttpStatus.NOT_ACCEPTABLE);
        }
        Person requestPerson = userService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setPerson(requestPerson);
        return ResponseEntity.ok(messageService.createOrUpdate(message));
    }

    @PutMapping("/")
    public ResponseEntity<Message> update(@RequestBody Message message) {
        if (message.getId() == 0) {
            return new ResponseEntity("to update need to specify existing message id", HttpStatus.NOT_ACCEPTABLE);
        }
        return create(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int messageId) {
        Message message = messageService.getById(messageId);
        Person requestUser = userService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (!Objects.equals(requestUser.getId(), message.getPerson().getId())) {
            return new ResponseEntity("another user's message!", HttpStatus.NOT_ACCEPTABLE);
        }
        messageService.delete(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
