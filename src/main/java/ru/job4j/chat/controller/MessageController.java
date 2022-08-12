package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.dto.MessageDto;
import ru.job4j.chat.mapper.MessageDtoMapper;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.UserDetailsServiceImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final UserDetailsServiceImpl userService;
    private final MessageDtoMapper messageDtoMapper;

    public MessageController(MessageService messageService, UserDetailsServiceImpl userService,
                             MessageDtoMapper messageDtoMapper) {
        this.messageService = messageService;
        this.userService = userService;
        this.messageDtoMapper = messageDtoMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<MessageDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAll().stream()
                                                                                .map(messageDtoMapper::toDTO)
                                                                                .collect(Collectors.toList()));
    }

    @PostMapping("/")
    public ResponseEntity<MessageDto> create(@RequestBody MessageDto messageDto) {
        if (messageDto.getId() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "message id must be 0 to create");
        }
        Message message = messageDtoMapper.toModel(messageDto);
        genericValidate(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                messageDtoMapper.toDTO(messageService.createOrUpdate(message)));
    }

    @PutMapping("/")
    public ResponseEntity<MessageDto> update(@RequestBody MessageDto messageDto) {
        if (messageDto.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "message id mustn't be 0 to update");
        }
        Message message = messageDtoMapper.toModel(messageDto);
        genericValidate(message);
        return ResponseEntity.status(HttpStatus.OK).body(
                messageDtoMapper.toDTO(messageService.createOrUpdate(message)));
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
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User (Message owner) wasn't found")
        );
        message.setPerson(requestPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int messageId) {
        Message message = messageService.getById(messageId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message wasn't found"));
        Person requestUser = userService.findByLogin(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User (Message owner) wasn't found")
        );
        if (!Objects.equals(requestUser.getId(), message.getPerson().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "another user's message!");
        }
        messageService.delete(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
