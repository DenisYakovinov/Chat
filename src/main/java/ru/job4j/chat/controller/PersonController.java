package ru.job4j.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "users", description = "methods for users")
public class PersonController {

    private static final long DEFAULT_USER_ROLE_ID = 1;

    private final PersonService personService;
    private final PersonDtoMapper personDtoMapper;

    public PersonController(PersonService personService, PersonDtoMapper personDtoMapper) {
        this.personService = personService;
        this.personDtoMapper = personDtoMapper;
    }

    @PostMapping("/sign-up")
    @Operation(summary = "register a new user")
    public ResponseEntity<PersonDto> signUp(@RequestBody
                                            @NonLoggableParameter
                                            @Parameter(description = "user creation DTO")
                                            PersonCreationDto personCreationDto) {
        Person person = personDtoMapper.toModel(personCreationDto);
        person.setRole(new Role(DEFAULT_USER_ROLE_ID));
        return ResponseEntity.status(HttpStatus.CREATED).body(personDtoMapper.toDto(personService.save(person)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a user")
    public ResponseEntity<PersonDto> update(@RequestBody @NonLoggableParameter
                                            @Parameter(description = "User creation DTO")
                                            PersonCreationDto personCreationDto,
                                            @Parameter(description = "user id", required = true)
                                            @PathVariable long id) {
        Person person = personDtoMapper.toModel(personCreationDto);
        person.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDto(personService.update(person)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "partial update a user")
    public ResponseEntity<PersonDto> partialUpdate(@RequestBody @NonLoggableParameter
                                                   @Parameter(description = "User creation DTO")
                                                   PersonCreationDto personCreationDto,
                                                   @PathVariable
                                                   @Parameter(description = "user id", required = true) long id) {
        Person person = personDtoMapper.toModel(personCreationDto);
        person.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDto(personService.partialUpdate(person)));
    }

    @GetMapping("/")
    @Operation(summary = "get list of all users")
    public ResponseEntity<List<PersonDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                personService.getAll().stream().map(personDtoMapper::toDto)
                                               .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a user by id")
    public ResponseEntity<PersonDto> findById(@PathVariable
                                              @Parameter(description = "user id", required = true) long id) {
        Person person = personService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDto(person));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete user by id")
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "user id", required = true) int id) {
        Person person = new Person();
        person.setId(id);
        personService.delete(person);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}