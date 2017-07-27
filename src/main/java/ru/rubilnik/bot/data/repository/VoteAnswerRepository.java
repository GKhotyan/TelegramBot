package ru.rubilnik.bot.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rubilnik.bot.data.model.Vote;
import ru.rubilnik.bot.data.model.VoteAnswer;

import java.util.Optional;

/**
 * Created by Alexey on 27.07.2017.
 */
public interface VoteAnswerRepository extends CrudRepository<VoteAnswer, Long> {
    Optional<VoteAnswer> findByVoteIdAndUserId(Long voteId, Long userId);
}
