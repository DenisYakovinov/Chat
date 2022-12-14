package ru.job4j.chat.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "text can't be empty")
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull(message = "user (message owner) can't be null")
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Message() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id && Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
