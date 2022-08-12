package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.dto.RoomCreationDto;
import ru.job4j.chat.dto.RoomDto;
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
    public ResponseEntity<List<RoomDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAll().stream().map(roomDtoMapper::toDTO)
                                                                                      .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getById(@PathVariable long id) {
        Room room = roomService.getByIdWithMessages(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("room with id = %d wasn't found. Please, check requisites.", id)
        ));
        return ResponseEntity.status(HttpStatus.OK).body(roomDtoMapper.toDTO(room));
    }

    @PostMapping("/")
    public ResponseEntity<RoomDto> create(@RequestBody RoomCreationDto roomCreationDto) {
        if (roomCreationDto.getId() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "room id must be 0 to create");
        }
        if (roomCreationDto.getName() == null) {
            throw new NullPointerException("room name mustn't be null");
        }
        Room room = roomDtoMapper.toModel(roomCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDtoMapper.toDTO(roomService.createOrUpdate(room)));
    }

    @PutMapping("/")
    public ResponseEntity<RoomDto> update(@RequestBody RoomCreationDto roomCreationDto) {
        if (roomCreationDto.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "room id mustn't be 0 to update");
        }
        if (roomCreationDto.getName() == null) {
            throw new NullPointerException("room name mustn't be null");
        }
        Room room = roomDtoMapper.toModel(roomCreationDto);
        return ResponseEntity.status(HttpStatus.OK).body(roomDtoMapper.toDTO(roomService.createOrUpdate(room)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Room room = new Room();
        room.setId(id);
        roomService.delete(room);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
