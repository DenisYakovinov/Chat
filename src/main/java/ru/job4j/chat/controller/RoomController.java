package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.dto.RoomCreationDto;
import ru.job4j.chat.dto.RoomDto;
import ru.job4j.chat.dto.RoomWithoutMessagesDto;
import ru.job4j.chat.mapper.RoomDtoMapper;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.RoomService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/room")
@Loggable
public class RoomController {

    private final RoomService roomService;
    private final RoomDtoMapper roomDtoMapper;

    public RoomController(RoomService roomService, RoomDtoMapper roomDtoMapper) {
        this.roomService = roomService;
        this.roomDtoMapper = roomDtoMapper;
    }

    @PostMapping("/")
    public ResponseEntity<RoomWithoutMessagesDto> create(@RequestBody RoomCreationDto roomCreationDto) {
        Room room = roomDtoMapper.toModel(roomCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDtoMapper.toDtoWithNotMessages(roomService.save(room)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomDto> partialUpdate(@RequestBody RoomCreationDto roomDto, @PathVariable long id) {
        Room room = roomDtoMapper.toModel(roomDto);
        room.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(roomDtoMapper.toDto(roomService.partialUpdate(room)));
    }

    @GetMapping("/")
    public ResponseEntity<List<RoomWithoutMessagesDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                roomService.getAll().stream().map(roomDtoMapper::toDtoWithNotMessages)
                                             .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getById(@PathVariable long id) {
        Room room = roomService.getByIdWithMessages(id);
        return ResponseEntity.status(HttpStatus.OK).body(roomDtoMapper.toDto(room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Room room = new Room();
        room.setId(id);
        roomService.delete(room);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
