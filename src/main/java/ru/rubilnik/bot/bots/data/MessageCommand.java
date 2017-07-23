package ru.rubilnik.bot.bots.data;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Alexey on 23.07.2017.
 */
public class MessageCommand {
    @Getter @NonNull
    private final ParsedMessage parsedMessage;
    @Getter @NonNull
    private final String pattern;

    public MessageCommand(ParsedMessage parsedMessage, String pattern) {
        this.parsedMessage = parsedMessage;
        this.pattern = pattern;
    }
}
