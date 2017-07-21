package ru.rubilnik.bot.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rubilnik.bot.data.model.Replacement;

import java.util.Optional;

/**
 * Created by Alexey on 21.07.2017.
 */
public interface ReplacementRepository extends CrudRepository<Replacement, Long> {
    Optional<Replacement> findByFromWord(String from);
}
