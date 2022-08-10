package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        List<Room> rooms = new ArrayList<>();
        roomRepository.findAll().forEach(r -> {
            r.setMessages(Collections.emptySet());
            rooms.add(r);
        });
        return rooms;
    }

    public Optional<Room> getById(long id) {
        return roomRepository.findById(id);
    }

    public Optional<Room> getByIdWithMessages(long id) {
        return Optional.ofNullable(roomRepository.findByIdWithMessages(id));
    }

    public Room createOrUpdate(Room room) {
        return roomRepository.save(room);
    }

    public void delete(Room room) {
        roomRepository.delete(room);
    }
}
