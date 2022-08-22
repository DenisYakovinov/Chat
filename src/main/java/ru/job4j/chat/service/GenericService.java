package ru.job4j.chat.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface GenericService<T> {

    T getById(long id);

    List<T> getAll();

    T save(@Valid T model);

    void delete(T model);

    T update(T model);

    T partialUpdate(T model);
}
