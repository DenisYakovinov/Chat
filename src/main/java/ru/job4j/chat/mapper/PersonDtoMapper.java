package ru.job4j.chat.mapper;

import org.mapstruct.*;
import ru.job4j.chat.dto.PersonCreationDto;
import ru.job4j.chat.dto.PersonDto;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;

@Mapper(componentModel = "spring")
public interface PersonDtoMapper {

    @Mapping(target = "roleId", source = "person.role.id")
    @Mapping(target = "roleName", source = "person.role.name")
    PersonDto toDto(Person person);

    Person toModel(PersonCreationDto personCreationDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNewNotNullFieldsToPerson(Person newPerson, @MappingTarget Person person);

    @AfterMapping
    default Person afterMapping(@MappingTarget Person person) {
        if (person.getRole() != null && person.getRole().getId() == 0) {
            person.setRole(null);
        }
        return person;
    }
}
