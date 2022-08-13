package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
public class RoomController {

    private final RoomService roomService;

    private final RoomDtoMapper roomDtoMapper;

    public RoomController(RoomService roomService, RoomDtoMapper roomDtoMapper) {
        this.roomService = roomService;
        this.roomDtoMapper = roomDtoMapper;
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

    @PostMapping("/")
    public ResponseEntity<RoomDto> create(@RequestBody RoomCreationDto roomCreationDto) {
        Room room = roomDtoMapper.toModel(roomCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDtoMapper.toDto(roomService.save(room)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> update(@RequestBody RoomCreationDto roomCreationDto, @PathVariable long id) {
        Room room = roomDtoMapper.toModel(roomCreationDto);
        room.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(roomDtoMapper.toDto(roomService.save(room)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Room room = new Room();
        room.setId(id);
        roomService.delete(room);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
