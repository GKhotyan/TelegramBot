package ru.rubilnik.bot.bots.service;

import org.springframework.stereotype.Service;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.utils.Porter;

/**
 * Created by Alexey on 25.07.2017.
 */
@Service
public class SwearService extends DefaultService implements BotCommand {
    @Override
    public FullMessage getMessage(MessageCommand command) {
        String transform = Porter.transform(command.getParsedMessage().getMessage());
        return new FullMessage(createMessage(transform, command.getParsedMessage().getChatId()), PatternType.SWEAR, MessageType.NORMAL);
    }
}
