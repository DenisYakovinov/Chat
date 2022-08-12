package ru.job4j.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.chat.dto.RoomCreationDto;
import ru.job4j.chat.dto.RoomDto;
import ru.job4j.chat.model.Room;

@Mapper(componentModel = "spring", uses = {MessageDtoMapper.class, PersonDtoMapper.class})
public interface RoomDtoMapper {

    RoomDto toDTO(Room room);

    @Mapping(target = "admin.id", source = "adminId")
    Room toModel(RoomCreationDto roomCreationDto);
}
