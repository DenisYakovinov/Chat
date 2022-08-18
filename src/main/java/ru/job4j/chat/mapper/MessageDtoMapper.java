package ru.job4j.chat.mapper;

import org.mapstruct.*;
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNewNotNullFieldsToMessage(Message newMessage, @MappingTarget Message message);

    @AfterMapping
    default Message afterMapping(@MappingTarget Message message) {
        if (message.getRoom() != null && message.getRoom().getId() == 0) {
            message.setRoom(null);
        }
        if (message.getPerson() != null && message.getPerson().getId() == 0) {
            message.setPerson(null);
        }
        return message;
    }
}
