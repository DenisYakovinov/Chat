package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}

