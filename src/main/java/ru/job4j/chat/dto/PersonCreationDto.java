package ru.job4j.chat.dto;

import java.util.Objects;

public class PersonCreationDto {

    private long id;
    private String login;
    private String password;
    private long roleId;


    public PersonCreationDto() {
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

    public void setLogin(String login) {
        this.login = login;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonCreationDto that = (PersonCreationDto) o;
        return roleId == that.roleId && Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, roleId);
    }
}
