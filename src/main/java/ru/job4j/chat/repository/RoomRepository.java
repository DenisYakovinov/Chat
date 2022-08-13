package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.chat.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("from Room r left join fetch r.messages where r.id = :rId")
    Optional<Room> findByIdWithMessages(@Param("rId") long id);

    @Override
    List<Room> findAll();
}
