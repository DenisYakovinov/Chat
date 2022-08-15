package ru.job4j.chat.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "login can't be empty or null")
    @Size(min = 4, max = 255, message = "Login length should be between 4 and 255")
    private String login;

    @NotBlank(message = "password can't be empty or null")
    @Size(min = 6, max = 255, message = "Password length should be greater than 6 and less than 255")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Person() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String name) {
        this.login = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id && Objects.equals(login, person.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}
