package ru.rubilnik.bot.messages;

import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.ParsedMessage;

/**
 * Created by Alexey on 20.07.2017.
 */
public interface Messenger {

    String getMessage(MessageCommand command);

}
