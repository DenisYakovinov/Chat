package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.dto.PersonCreationDto;
import ru.job4j.chat.dto.PersonDto;
import ru.job4j.chat.mapper.PersonDtoMapper;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/users")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private static final long DEFAULT_USER_ROLE_ID = 1;

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    private final PersonDtoMapper personDtoMapper;

    public PersonController(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder encoder,
                            ObjectMapper objectMapper, PersonDtoMapper personDtoMapper) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
        this.personDtoMapper = personDtoMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<PersonDto> signUp(@RequestBody PersonCreationDto personCreationDto) {
        if (personCreationDto.getId() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "personCreationDto id must be 0 to create");
        }
        if (personCreationDto.getLogin() == null || personCreationDto.getPassword() == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (personCreationDto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Not really secure password");
        }
        personCreationDto.setPassword(encoder.encode(personCreationDto.getPassword()));
        Person person = personDtoMapper.toModel(personCreationDto);
        person.setRole(new Role(DEFAULT_USER_ROLE_ID));
        return ResponseEntity.status(HttpStatus.CREATED).body(personDtoMapper.toDTO(userDetailsServiceImpl.save(person)));
    }

    @GetMapping("/")
    public ResponseEntity<List<PersonDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                userDetailsServiceImpl.getAll().stream().map(personDtoMapper::toDTO)
                                                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable long id) {
        Person person = userDetailsServiceImpl.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("user with id = %d wasn't found. Please, check requisites.", id)
        ));
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDTO(person));
    }

    @PutMapping("/")
    public ResponseEntity<PersonDto> update(@RequestBody PersonCreationDto personCreationDto) {
        if (personCreationDto.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "message id mustn't be 0 to update");
        }
        if (personCreationDto.getLogin() == null || personCreationDto.getPassword() == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (personCreationDto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Not really secure password");
        }
        Person person = personDtoMapper.toModel(personCreationDto);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoMapper.toDTO(userDetailsServiceImpl.save(person)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        userDetailsServiceImpl.delete(person);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(Stream.of(
                        new AbstractMap.SimpleEntry<>("message", e.getMessage()),
                        new AbstractMap.SimpleEntry<>("type", e.getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
        logger.error(e.getMessage());
    }
}