package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.dto.MessageCreationDto;
import ru.job4j.chat.dto.MessageDto;
import ru.job4j.chat.mapper.MessageDtoMapper;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@Loggable
public class MessageController {

    private final MessageService messageService;
    private final MessageDtoMapper messageDtoMapper;
    private final PersonService personService;

    public MessageController(MessageService messageService, MessageDtoMapper messageDtoMapper,
                             PersonService personService) {
        this.messageService = messageService;
        this.messageDtoMapper = messageDtoMapper;
        this.personService = personService;
    }

    @GetMapping("/")
    public ResponseEntity<List<MessageDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAll().stream()
                                                                                .map(messageDtoMapper::toDto)
                                                                                .collect(Collectors.toList()));
    }

    @PostMapping("/")
    public ResponseEntity<MessageDto> create(@RequestBody MessageCreationDto messageCreationDto,
                                             @CurrentSecurityContext(expression = "authentication?.name")
                                             String currentUser) {
        Message message = messageDtoMapper.toModel(messageCreationDto);
        Person requestPerson = personService.findByLogin(currentUser);
        message.setPerson(requestPerson);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                messageDtoMapper.toDto(messageService.save(message)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageDto> partialUpdate(@RequestBody MessageCreationDto messageCreationDto,
                                                    @PathVariable long id,
                                                    @CurrentSecurityContext(expression = "authentication?.name")
                                                    String currentUser) {
        Message newMessage = messageDtoMapper.toModel(messageCreationDto);
        Person requestUser = personService.findByLogin(currentUser);
        newMessage.setPerson(requestUser);
        newMessage.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageDtoMapper.toDto(messageService.partialUpdate(newMessage)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id,
                                       @CurrentSecurityContext(expression = "authentication?.name")
                                       String currentUser) {
        Message message = new Message();
        message.setId(id);
        Person requestUser = personService.findByLogin(currentUser);
        message.setPerson(requestUser);
        messageService.delete(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
