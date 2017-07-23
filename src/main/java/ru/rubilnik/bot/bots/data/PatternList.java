package ru.rubilnik.bot.bots.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import ru.rubilnik.bot.bots.ContactCount;
import ru.rubilnik.bot.messages.AvdotyaMessenger;
import ru.rubilnik.bot.messages.LiveScoresMessenger;
import ru.rubilnik.bot.messages.TeamMessenger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alexey on 23.07.2017.
 */
@Component
public class PatternList {

    @Getter
    private List<Pattern> patterns = new ArrayList<>();
    private final LiveScoresMessenger liveScoresMessenger;
    private final AvdotyaMessenger avdotyaMessenger;
    private final TeamMessenger teamMessenger;

    @PostConstruct
    private void init() {
        patterns.add(new Pattern("счет:счёт:как сыграл:кто ведет", PatternType.LIVE, liveScoresMessenger, 3));
        patterns.add(new Pattern("авдотья", PatternType.AVDOTYA, avdotyaMessenger, 4));
        patterns.add(new Pattern("а что", PatternType.ABOUT_TEAM, teamMessenger, 5));
        patterns.sort(Comparator.comparingInt(Pattern::getPriority).reversed());
    }

    public PatternList(LiveScoresMessenger liveScoresMessenger, AvdotyaMessenger avdotyaMessenger, TeamMessenger teamMessenger) {
        this.liveScoresMessenger = liveScoresMessenger;
        this.avdotyaMessenger = avdotyaMessenger;
        this.teamMessenger = teamMessenger;
    }

    public FullMessage getMessage(Update update) throws NoMessageException {
        ParsedMessage parsedMessage = new ParsedMessage(update);
        for (Pattern p : patterns) {
            if(p.isFit(parsedMessage.getMessage())) {
                return new FullMessage(p.getMessenger().getMessage(new MessageCommand(parsedMessage, p.getPattern())), p.getType());
            }
        }
        throw new NoMessageException();
    }

    @AllArgsConstructor
    public static class FullMessage {
        @Getter
        private final String message;
        @Getter
        private final PatternType type;
    }

}
