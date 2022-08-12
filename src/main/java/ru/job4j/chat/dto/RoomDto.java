package ru.job4j.chat.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RoomDto {

    private long id;
    private String name;
    private PersonDto admin;
    private Set<MessageDto> messages = new HashSet<>();

    public RoomDto() {
    }

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

    public Set<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageDto> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomDto roomDTO = (RoomDto) o;
        return id == roomDTO.id && Objects.equals(name, roomDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
