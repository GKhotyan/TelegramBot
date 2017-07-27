package ru.rubilnik.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.rubilnik.bot.data.model.VoteAnswer;
import ru.rubilnik.bot.data.model.Vote;
import ru.rubilnik.bot.data.repository.VoteRepository;
import ru.rubilnik.bot.service.VoteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Alexey on 21.07.2017.
 */

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository repo;

    @Autowired
    public VoteServiceImpl(VoteRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(Vote vote) {
        repo.save(vote);
    }

    @Override
    public Iterable<Vote> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Vote> findByChatId(Long chatID) {
        return repo.findByChatId(chatID);
    }

    @Override
    public Optional<Vote> findOne(Long voteID) {
        Vote one = repo.findOne(voteID);
        if (one != null) return Optional.of(one);
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public String getResult(Long voteId) {
        Optional<Vote> vote = findOne(voteId);
        if (vote.isPresent() && !CollectionUtils.isEmpty(vote.get().getAnswers())) {
            List<Boolean> votes = vote.get().getAnswers().stream().map(VoteAnswer::getYes).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder("Результат опроса: ").append(vote.get().getQuestion()).append("\n");;
            sb.append(votes.size()).append("\n");
            int yes = votes.stream().mapToInt(v -> {
                if (v) {
                    return 1;
                }
                return 0;
            }).sum();
            int no = votes.size() - yes;
            sb.append("Да: ").append(yes).append(" (");
            sb.append((int) ((yes * 100.0f) / votes.size()));
            sb.append("%)\n");
            sb.append("Нет: ").append(no).append(" (");
            sb.append((int) ((no * 100.0f) / votes.size()));
            sb.append("%)");
            return sb.toString();
        }
        return "Результата нет";
    }
}
