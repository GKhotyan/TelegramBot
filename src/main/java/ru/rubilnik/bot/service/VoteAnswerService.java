package ru.rubilnik.bot.service;

import ru.rubilnik.bot.data.model.VoteAnswer;

import java.util.Optional;

/**
 * Created by Alexey on 21.07.2017.
 */
public interface VoteAnswerService {
    void save(VoteAnswer vote);
    Optional<VoteAnswer> find(Long voteId, Long userId);
}
