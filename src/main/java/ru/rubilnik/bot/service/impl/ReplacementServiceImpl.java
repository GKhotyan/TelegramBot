package ru.rubilnik.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubilnik.bot.data.repository.ReplacementRepository;
import ru.rubilnik.bot.data.model.Replacement;
import ru.rubilnik.bot.service.ReplacementService;

import java.util.Optional;

/**
 * Created by Alexey on 21.07.2017.
 */

@Service
public class ReplacementServiceImpl implements ReplacementService {

    @Autowired
    private ReplacementRepository repo;

    @Override
    public void save(Replacement replacement) {
        Optional<Replacement> old = findByFrom(replacement.getFromWord());
        if(old.isPresent()) {
            Replacement oldReplace = old.get();
            oldReplace.setToWord(replacement.getToWord());
            repo.save(oldReplace);
        }
        else {
            repo.save(replacement);
        }
    }

    @Override
    public Iterable<Replacement> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Replacement> findByFrom(String fromWord) {
        return repo.findByFromWord(fromWord);
    }

    @Override
    public void deleteByFrom(String from) {
        Optional<Replacement> rep = this.findByFrom(from);
        Replacement replacement = rep.orElseThrow(() -> new IllegalArgumentException("Замена не найдена."));
        repo.delete(replacement);
    }
}
