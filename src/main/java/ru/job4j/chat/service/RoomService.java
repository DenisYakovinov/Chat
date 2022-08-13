package ru.job4j.chat.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.exception.EntityNotFoundException;
import ru.job4j.chat.exception.RoomNameReservedException;
import ru.job4j.chat.exception.ServiceException;
import ru.job4j.chat.exception.ServiceValidateException;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.*;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public Room getById(long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("room with id = %d wasn't found", id)));
    }

    public Room getByIdWithMessages(long id) {
        return roomRepository.findByIdWithMessages(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("room with id = %d wasn't found", id)));
    }

    public Room save(Room room) {
        if (room.getName() == null) {
            throw new ServiceValidateException("room name mustn't be null");
        }
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
