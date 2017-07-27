package ru.rubilnik.bot.bots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.parsers.AnekdotParser;

/**
 * Created by Alexey on 26.07.2017.
 */

@Service
public class AnekdotCommand extends DefaultService implements BotCommand {

    private final AnekdotParser anekdotParser;

    @Autowired
    public AnekdotCommand(AnekdotParser anekdotParser) {
        this.anekdotParser = anekdotParser;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {

        String anekdot = anekdotParser.getAnekdot();
        if (StringUtils.isEmpty(anekdot)) {
            anekdot = "Ни одного баяна сегодня нет";
        }
        return new FullMessage(createMessage(anekdot, command.getParsedMessage().getChatId()), PatternType.ANEKDOT, MessageType.NORMAL);
    }

}

