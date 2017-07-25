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
    private final LiveScoresService liveScoresService;
    private final AvdotyaService avdotyaService;
    private final TeamService teamService;
    private final ReplaceCommandService replaceCommandService;
    private final SwearService swearService;
    private final PhotoService photoService;
    private final CommandsService commandsService;

    @PostConstruct
    private void init() {
        patterns.add(new Pattern("фото", PatternType.PHOTO, photoService, 0, "Высылает фото по запросу", startWithFunction));
        patterns.add(new Pattern("счет:счёт:как сыграл:кто ведет", PatternType.LIVE, liveScoresService, 3, "Показывает текущие результаты"));
        patterns.add(new Pattern("авдотья", PatternType.AVDOTYA, avdotyaService, 4, "Выводит народный календарь"));
        patterns.add(new Pattern("а что", PatternType.ABOUT_TEAM, teamService, 5, "Выводит данные о матчах комнад", startWithFunction));
        patterns.add(new Pattern("бот замена", PatternType.REPLACE, replaceCommandService, 6, "Автозамена в новостях и результатах", startWithFunction));
        patterns.add(new Pattern("команды", PatternType.COMMANDS, commandsService, 7, "Список комманд для бота", startWithFunction));
        patterns.add(new Pattern("бот", PatternType.SWEAR, swearService, Integer.MAX_VALUE, "Хуебот"));
        patterns.sort(Comparator.comparingInt(Pattern::getPriority));
        commandsService.setPatterns(patterns);
    }

    @Autowired
    public PatternList(LiveScoresService liveScoresService, AvdotyaService avdotyaService, TeamService teamService, ReplaceCommandService replaceCommandService, SwearService swearService, PhotoService photoService, CommandsService commandsService) {
        this.liveScoresService = liveScoresService;
        this.avdotyaService = avdotyaService;
        this.teamService = teamService;
        this.replaceCommandService = replaceCommandService;
        this.swearService = swearService;
        this.photoService = photoService;
        this.commandsService = commandsService;
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
