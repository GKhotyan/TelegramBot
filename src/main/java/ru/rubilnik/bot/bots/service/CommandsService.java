package ru.rubilnik.bot.bots.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubilnik.bot.bots.data.*;
import ru.rubilnik.bot.utils.Porter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alexey on 25.07.2017.
 */
@Service
public class CommandsService extends DefaultService implements BotService {

    @Setter
    private  List<Pattern> patterns;

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String result = patterns.stream().filter(p -> p.getType() != null).filter(p -> p.getType().isDescription()).map(p -> p.getPattern().replaceAll(":", ", ") + ": " + p.getDescription().toLowerCase()).collect(Collectors.joining("\n"));
        return new FullMessage(createMessage(result, command.getParsedMessage().getChatId()), PatternType.COMMANDS, MessageType.NORMAL);
    }
}
