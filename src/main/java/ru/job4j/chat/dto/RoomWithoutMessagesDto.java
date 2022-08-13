package ru.job4j.chat.dto;

import java.util.Objects;

public class RoomWithoutMessagesDto {

    private long id;
    private String name;
    private PersonDto admin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonDto getAdmin() {
        return admin;
    }

    public void setAdmin(PersonDto admin) {
        this.admin = admin;
    }

    public RoomWithoutMessagesDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomWithoutMessagesDto that = (RoomWithoutMessagesDto) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
