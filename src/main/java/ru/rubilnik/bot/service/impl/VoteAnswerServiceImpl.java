package ru.rubilnik.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubilnik.bot.data.model.VoteAnswer;
import ru.rubilnik.bot.data.repository.VoteAnswerRepository;
import ru.rubilnik.bot.service.VoteAnswerService;

import java.util.Optional;

/**
 * Created by Alexey on 27.07.2017.
 */
@Service
public class VoteAnswerServiceImpl implements VoteAnswerService {

    private final VoteAnswerRepository repo;

    @Autowired
    public VoteAnswerServiceImpl(VoteAnswerRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(VoteAnswer voteAnswer) {
        Optional<VoteAnswer> answer = this.find(voteAnswer.getVote().getId(), voteAnswer.getUserId());
        if(answer.isPresent()) {
            throw new IllegalArgumentException("Вы уже голосовали");
        }
        else {
            repo.save(voteAnswer);
        }

    }

    @Override
    public Optional<VoteAnswer> find(Long voteId, Long userId) {
        return repo.findByVoteIdAndUserId(voteId, userId);
    }
}
