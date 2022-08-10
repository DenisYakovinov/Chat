package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.chat.model.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("select distinct r from Room r left join fetch r.messages where r.id = :rId")
    Room findByIdWithMessages(@Param("rId") long id);
}
