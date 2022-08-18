package ru.job4j.chat.dto;

import java.util.Objects;

public class PersonDto {

    private long id;
    private String login;
    private String roleName;
    private long roleId;

    public PersonDto() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonDto personDTO = (PersonDto) o;
        return id == personDTO.id && Objects.equals(login, personDTO.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}
