package ru.rubilnik.bot.bots.service;

import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;

/**
 * Created by Alexey on 20.07.2017.
 */
public interface BotService {

    FullMessage getMessage(MessageCommand command);

}
