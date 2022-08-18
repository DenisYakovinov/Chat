package ru.job4j.chat.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonService personService;

    public UserDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Person person = personService.findByLogin(login);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(person.getRole().getName());
        return new User(person.getLogin(), person.getPassword(), List.of(authority));
    }
}
