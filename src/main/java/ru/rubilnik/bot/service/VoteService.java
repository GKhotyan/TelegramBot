package ru.rubilnik.bot.service;

import ru.rubilnik.bot.data.model.Replacement;
import ru.rubilnik.bot.data.model.Vote;

import java.util.List;
import java.util.Optional;

/**
 * Created by Alexey on 21.07.2017.
 */
public interface VoteService {
    void save(Vote vote);

    Iterable<Vote> getAll();

    List<Vote> findByChatId(Long chatID);

    Optional<Vote> findOne(Long voteID);

    void delete(Long id);

    String getResult(Long voteId);
}
