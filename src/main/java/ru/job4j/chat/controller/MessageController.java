package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        if (message.getId() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "message id must be 0 to create");
        }
        genericValidate(message);
        return ResponseEntity.ok(messageService.createOrUpdate(message));
    }

    @PutMapping("/")
    public ResponseEntity<Message> update(@RequestBody Message message) {
        if (message.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "message id mustn't be 0 to update");
        }
        genericValidate(message);
        return ResponseEntity.ok(messageService.createOrUpdate(message));
    }

    private void genericValidate(Message message) {
        if (message.getRoom().getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "need to specify the room id");
        }
        if (message.getText() == null || message.getText().trim().length() == 0) {
            throw new NullPointerException("message text mustn't be empty");
        }
        Person requestPerson = userService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user wasn't found")
        );
        message.setPerson(requestPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int messageId) {
        Message message = messageService.getById(messageId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message wasn't found"));
        Person requestUser = userService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user wasn't found")
        );
        if (!Objects.equals(requestUser.getId(), message.getPerson().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "another user's message!");
        }
        messageService.delete(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
