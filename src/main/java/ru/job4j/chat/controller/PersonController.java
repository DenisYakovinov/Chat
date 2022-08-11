package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public PersonController(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder encoder,
                            ObjectMapper objectMapper) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@RequestBody Person person) {
        if (person.getId() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "person id must be 0 to create");
        }
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Not really secure password");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        Role userRole = new Role();
        userRole.setId(1);
        person.setRole(userRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsServiceImpl.save(person));
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsServiceImpl.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable long id) {
        Person person = userDetailsServiceImpl.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("user with id = %d wasn't found. Please, check requisites.", id)
        ));
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @PutMapping("/")
    public ResponseEntity<Person> update(@RequestBody Person person) {
        if (person.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "message id mustn't be 0 to update");
        }
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Not really secure password");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsServiceImpl.save(person));
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