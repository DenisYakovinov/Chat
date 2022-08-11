package ru.job4j.chat.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.exception.RoomNameReservedException;
import ru.job4j.chat.exception.ServiceException;
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
        try {
            return roomRepository.save(room);
        } catch (DataAccessException e) {
            if ((e.getMostSpecificCause()) instanceof PSQLException) {
                PSQLException sqlEx = (PSQLException) e.getMostSpecificCause();
                if (sqlEx.getSQLState().equals("23505")) {
                    throw new RoomNameReservedException(String.format("Room name already reserved please try again (%s)",
                            sqlEx.getMessage()), sqlEx);
                }
            }
            throw new ServiceException(String.format("Room %s can't be added (%s)", room, e.getMessage()), e);
        }
    }

    public void delete(Room room) {
        roomRepository.delete(room);
    }
}
