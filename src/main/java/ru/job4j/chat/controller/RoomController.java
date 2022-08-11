package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public List<Room> getAll() {
        return roomService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable long id) {
        Room room = roomService.getByIdWithMessages(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "room wasn't found. Please, check requisites."
        ));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room.getId() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "room id must be 0 to create");
        }
        if (room.getName() == null) {
            throw new NullPointerException("room name mustn't be null");
        }
        return new ResponseEntity<>(roomService.createOrUpdate(room), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        if (room.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "room id mustn't be 0 to update");
        }
        if (room.getName() == null) {
            throw new NullPointerException("room name mustn't be null");
        }
        roomService.createOrUpdate(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Room room = new Room();
        room.setId(id);
        roomService.delete(room);
        return ResponseEntity.ok().build();
    }
}
