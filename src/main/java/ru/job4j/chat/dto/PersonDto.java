package ru.job4j.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "person response DTO")
public class PersonDto {

    @Schema(description = "person id")
    private long id;

    @Schema(description = "user login")
    private String login;

    @Schema(description = "user role name")
    private String roleName;

    @Schema(description = "role id")
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
