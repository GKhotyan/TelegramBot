package ru.rubilnik.bot.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rubilnik.bot.data.model.Vote;

import java.util.List;
import java.util.Optional;

/**
 * Created by Alexey on 27.07.2017.
 */
public interface VoteRepository extends CrudRepository<Vote, Long> {
    List<Vote> findByChatId(Long chatId);
    Optional<Vote> findByChatIdAndId(Long chatId, Long voteId);
}
