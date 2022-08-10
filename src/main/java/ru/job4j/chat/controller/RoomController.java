package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        var room = roomService.getByIdWithMessages(id);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(roomService.createOrUpdate(room), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
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
