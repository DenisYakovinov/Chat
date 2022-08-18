package ru.job4j.chat.mapper;

import org.mapstruct.*;
import ru.job4j.chat.dto.RoomCreationDto;
import ru.job4j.chat.dto.RoomDto;
import ru.job4j.chat.dto.RoomWithoutMessagesDto;
import ru.job4j.chat.model.Room;

@Mapper(componentModel = "spring", uses = {MessageDtoMapper.class, PersonDtoMapper.class})
public interface RoomDtoMapper {

    RoomDto toDto(Room room);

    @Mapping(target = "admin.id", source = "adminId")
    Room toModel(RoomCreationDto roomCreationDto);

    RoomWithoutMessagesDto toDtoWithNotMessages(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNewNotNullFieldsToRoom(Room newRoom, @MappingTarget Room room);

    @AfterMapping
    default Room afterMapping(@MappingTarget Room room) {
        if (room.getName() != null && room.getAdmin().getId() == 0) {
            room.setAdmin(null);
        }
        return room;
    }
}
