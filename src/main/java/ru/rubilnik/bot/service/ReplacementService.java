package ru.rubilnik.bot.service;

import ru.rubilnik.bot.data.model.Replacement;

import java.util.Optional;

/**
 * Created by Alexey on 21.07.2017.
 */
public interface ReplacementService {
    void save(Replacement replacement);
    Iterable<Replacement> getAll();
    Optional<Replacement> findByFrom(String from);
    void deleteByFrom(String from);
}
