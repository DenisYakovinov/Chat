package ru.job4j.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.chat.dto.PersonCreationDto;
import ru.job4j.chat.dto.PersonDto;
import ru.job4j.chat.model.Person;

@Mapper(componentModel = "spring")
public interface PersonDtoMapper {

    @Mapping(target = "roleId", source = "person.role.id")
    @Mapping(target = "role", source = "person.role.name")
    PersonDto toDTO(Person person);

    @Mapping(target = "role.id", source = "roleId")
    Person toModel(PersonCreationDto personCreationDTO);
}
