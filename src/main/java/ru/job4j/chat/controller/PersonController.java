package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.aspect.Loggable;
import ru.job4j.chat.aspect.NonLoggableParameter;
import ru.job4j.chat.dto.PersonCreationDto;
import ru.job4j.chat.dto.PersonDto;
import ru.job4j.chat.mapper.PersonDtoMapper;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Loggable
public class PersonController {

    private static final long DEFAULT_USER_ROLE_ID = 1;

    private final PersonService personService;
    private final PersonDtoMapper personDtoMapper;

    public PersonController(PersonService personService, PersonDtoMapper personDtoMapper) {
        this.personService = personService;
        this.personDtoMapper = personDtoMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<PersonDto> signUp(@RequestBody @NonLoggableParameter PersonCreationDto personCreationDto) {
        Person person = personDtoMapper.toModel(personCreationDto);
        person.setRole(new Role(DEFAULT_USER_ROLE_ID));
        return ResponseEntity.status(HttpStatus.CREATED).body(personDtoMapper.toDto(personService.save(person)));
    }

    @GetMapping("/")
    public ResponseEntity<List<PersonDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                personService.getAll().stream().map(personDtoMapper::toDto)
                                               .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable long id) {
        Person person = personService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDto(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@RequestBody @NonLoggableParameter PersonCreationDto personCreationDto,
                                            @PathVariable long id) {
        Person person = personDtoMapper.toModel(personCreationDto);
        person.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDto(personService.save(person)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        personService.delete(person);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}