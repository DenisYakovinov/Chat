package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.dto.MessageCreationDto;
import ru.job4j.chat.dto.MessageDto;
import ru.job4j.chat.mapper.MessageDtoMapper;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.service.MessageService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@Loggable
public class MessageController {

    private final MessageService messageService;
    private final MessageDtoMapper messageDtoMapper;

    public MessageController(MessageService messageService, MessageDtoMapper messageDtoMapper) {
        this.messageService = messageService;
        this.messageDtoMapper = messageDtoMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<MessageDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAll().stream()
                                                                                .map(messageDtoMapper::toDto)
                                                                                .collect(Collectors.toList()));
    }

    @PostMapping("/")
    public ResponseEntity<MessageDto> create(@RequestBody MessageCreationDto messageCreationDto) {
        Message message = messageDtoMapper.toModel(messageCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                messageDtoMapper.toDto(messageService.save(message)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDto> update(@RequestBody MessageCreationDto messageCreationDto,
                                             @PathVariable long id) {
        Message message = messageDtoMapper.toModel(messageCreationDto);
        message.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                messageDtoMapper.toDto(messageService.save(message)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        messageService.delete(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/")
    public ResponseEntity<MessageDto> partialUpdate(@RequestBody Message message) throws InvocationTargetException,
            IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(messageDtoMapper.toDto(messageService.partialUpdate(message)));
    }
}
