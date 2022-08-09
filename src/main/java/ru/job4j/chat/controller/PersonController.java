package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.exception.LoginReservedException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.UserDetailsServiceImpl;

import java.util.List;
@RestController
@RequestMapping("/users")
public class PersonController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder encoder;

    public PersonController(final UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder encoder) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.encoder = encoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person>  signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        Role userRole = new Role();
        userRole.setId(1);
        person.setRole(userRole);
        return new ResponseEntity<>(
                userDetailsServiceImpl.save(person),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/")
    public List<Person> getAll() {
        return userDetailsServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable long id) {
        var person = userDetailsServiceImpl.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        userDetailsServiceImpl.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        userDetailsServiceImpl.delete(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(LoginReservedException.class)
    public ResponseEntity<Void> handleException(Exception e) {
        return new ResponseEntity("name already reserved please try again", HttpStatus.NOT_ACCEPTABLE);
    }
}