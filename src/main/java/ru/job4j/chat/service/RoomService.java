package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.exception.EntityNotFoundException;
import ru.job4j.chat.exception.RoomNameReservedException;
import ru.job4j.chat.mapper.RoomDtoMapper;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.*;

@Service
@Loggable
public class RoomService implements GenericService<Room> {

    private static final String NOT_FOUND_ENTITY = "room with id = %d wasn't found";

    private final RoomRepository roomRepository;
    private final RoomDtoMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomDtoMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room getById(long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(NOT_FOUND_ENTITY, id)));
    }

    public Room getByIdWithMessages(long id) {
        return roomRepository.findByIdWithMessages(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(NOT_FOUND_ENTITY, id)));
    }

    @Override
    public Room save(Room room) {
        if (roomRepository.findByName(room.getName()).isPresent()) {
            throw new RoomNameReservedException(
                    String.format("Room name '%s' already reserved please try again", room.getName()));
        }
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room update(Room room) {
        long id = room.getId();
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("can't delete, %s %s", NOT_FOUND_ENTITY, id));
        }
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room partialUpdate(Room room) {
        Room oldRoom = getById(room.getId());
        roomMapper.updateNewNotNullFieldsToRoom(room, oldRoom);
        return save(oldRoom);
    }

    @Override
    @Transactional
    public void delete(Room room) {
        long id = room.getId();
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("can't delete, %s %s", NOT_FOUND_ENTITY, id));
        }
        roomRepository.delete(room);
    }
}
