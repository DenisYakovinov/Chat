package ru.job4j.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.chat.dto.MessageCreationDto;
import ru.job4j.chat.dto.MessageDto;
import ru.job4j.chat.model.Message;

@Mapper(componentModel = "spring")
public interface MessageDtoMapper {

    @Mapping(target = "roomId", source = "message.room.id")
    @Mapping(target = "personId", source = "message.person.id")
    MessageDto toDto(Message message);

    @Mapping(target = "room.id", source = "roomId")
    Message toModel(MessageCreationDto messageDTO);
}
