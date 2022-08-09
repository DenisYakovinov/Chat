package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
