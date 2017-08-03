package ru.rubilnik.bot.bots.data;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import ru.rubilnik.bot.bots.service.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.rubilnik.bot.bots.functions.FitFunctions.*;

/**
 * Created by Alexey on 23.07.2017.
 */
@Component
public class PatternList {

    @Getter
    private List<Pattern> patterns = new ArrayList<>();
    private final LiveScoresCommand liveScoresCommand;
    private final AvdotyaCommand avdotyaCommand;
    private final TeamService teamService;
    private final ReplaceCommandService replaceCommandService;
    private final SwearService swearService;
    private final PhotoService photoService;
    private final CommandsCommand commandsCommand;
    private final ChampionatNewsService championatNewsService;
    private final AnekdotCommand anekdotCommand;
    private final CreateVoteCommand createVoteCommand;
    private final AnswerVoteCommand answerVoteCommand;
    private final VoteResultCommand voteResultCommand;

    @PostConstruct
    private void init() {
        patterns.add(new Pattern("фото", PatternType.PHOTO, photoService, 0, "Высылает фото по запросу", startWithFunction));
        patterns.add(new Pattern("новость:новости:что нового", PatternType.NEWS, championatNewsService, 1, "Футбольные новости"));
        patterns.add(new Pattern("боян:баян:анекдот", PatternType.ANEKDOT, anekdotCommand, 2, "Анекдоты"));
        patterns.add(new Pattern("счет:счёт:как сыграл:кто ведет", PatternType.LIVE, liveScoresCommand, 3, "Показывает текущие результаты"));
        patterns.add(new Pattern("авдотья", PatternType.AVDOTYA, avdotyaCommand, 4, "Выводит народный календарь"));
        patterns.add(new Pattern("а что", PatternType.ABOUT_TEAM, teamService, 5, "Выводит данные о матчах команд", startWithFunction));
        patterns.add(new Pattern("бот замена", PatternType.REPLACE, replaceCommandService, 6, "Автозамена в новостях и результатах", startWithFunction));
        patterns.add(new Pattern("/:/start", PatternType.COMMANDS, commandsCommand, 7, "Список комманд для бота", equalsFunction));
        //patterns.add(new Pattern("бот опрос", PatternType.VOTE, createVoteCommand, 8, "Создать опрос", startWithFunction));
        //patterns.add(new Pattern("бот_голос_", PatternType.VOTE_ANSWER, answerVoteCommand, 9, "Ответ на опрос", pollAnswerFunction));
        //patterns.add(new Pattern("результат опроса", PatternType.VOTE, voteResultCommand, 10, "Резульат опроса", startWithFunction));
        patterns.add(new Pattern("бот", PatternType.SWEAR, swearService, Integer.MAX_VALUE, "Хуебот"));
        patterns.sort(Comparator.comparingInt(Pattern::getPriority));
        commandsCommand.setPatterns(patterns);
    }

    @Autowired
    public PatternList(LiveScoresCommand liveScoresCommand, AvdotyaCommand avdotyaCommand, TeamService teamService, ReplaceCommandService replaceCommandService, SwearService swearService, PhotoService photoService, CommandsCommand commandsCommand, ChampionatNewsService championatNewsService, AnekdotCommand anekdotCommand, CreateVoteCommand createVoteCommand, AnswerVoteCommand answerVoteCommand, VoteResultCommand voteResultCommand) {
        this.liveScoresCommand = liveScoresCommand;
        this.avdotyaCommand = avdotyaCommand;
        this.teamService = teamService;
        this.replaceCommandService = replaceCommandService;
        this.swearService = swearService;
        this.photoService = photoService;
        this.commandsCommand = commandsCommand;
        this.championatNewsService = championatNewsService;
        this.anekdotCommand = anekdotCommand;
        this.createVoteCommand = createVoteCommand;
        this.answerVoteCommand = answerVoteCommand;
        this.voteResultCommand = voteResultCommand;
    }

    public FullMessage getMessage(Update update) {
        ParsedMessage parsedMessage = new ParsedMessage(update);
        for (Pattern p : patterns) {
            if (p.isFit(parsedMessage.getMessage())) {
                return p.getService().getMessage(new MessageCommand(parsedMessage, p.getPattern()));
            }
        }
        return FullMessage.EmptyMessage();
    }

}
