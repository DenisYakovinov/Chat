CREATE TABLE IF NOT EXISTS role
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    name     VARCHAR(2000) UNIQUE
    );

CREATE TABLE IF NOT EXISTS person
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    login    VARCHAR(2000) UNIQUE,
    password VARCHAR(2000),
    role_id BIGINT
    );

ALTER TABLE person ADD CONSTRAINT fk_person_role FOREIGN KEY (role_id) REFERENCES role(id)
    ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS room
(
    id      BIGSERIAL PRIMARY KEY NOT NULL,
    name    VARCHAR(2000),
    admin_id BIGINT
    );

ALTER TABLE room ADD CONSTRAINT fk_room_person FOREIGN KEY (admin_id) REFERENCES person(id)
    ON DELETE CASCADE ON UPDATE CASCADE;


CREATE TABLE IF NOT EXISTS message
(
    id        BIGSERIAL PRIMARY KEY NOT NULL,
    text      TEXT,
    room_id   BIGINT NOT NULL,
    person_id BIGINT NOT NULL
);

ALTER TABLE message ADD CONSTRAINT fk_message_room FOREIGN KEY (room_id) REFERENCES room(id)
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE message ADD CONSTRAINT fk_message_person FOREIGN KEY (person_id)  REFERENCES person(id)
    ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO role VALUES (1, 'user');
INSERT INTO role VALUES (2, 'admin');
INSERT INTO role VALUES (3, 'enterprise_admin');
INSERT INTO person VALUES (1, 'adm01global', '$2a$10$uBQOILH2hDkzm2cFDVRw5O8HCzJPUbEIBagCQQeTZXZxWY4C44haK',
(select id from role where name = 'enterprise_admin'));

/*The password was encoded by BCrypt from "secret"*/