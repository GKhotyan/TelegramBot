package ru.rubilnik.bot.bots.data;

import lombok.Getter;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Created by Alexey on 25.07.2017.
 */
public class FullMessage {

    @Getter
    private BotApiMethod<Message> botApiMethod = null;
    @Getter
    private SendPhoto photoMethod = null;
    @Getter
    private final PatternType patternType;
    @Getter
    private final MessageType messageType;

    public FullMessage(SendPhoto photoMethod, PatternType patternType, MessageType messageType) {
        this(patternType, messageType);
        this.photoMethod = photoMethod;
    }

    public FullMessage(BotApiMethod<Message> botApiMethod, PatternType patternType, MessageType messageType) {
        this(patternType, messageType);
        this.botApiMethod = botApiMethod;
    }

    public FullMessage(PatternType patternType, MessageType messageType) {
        this.patternType = patternType;
        this.messageType = messageType;
    }

    public static FullMessage EmptyMessage() {
        return new FullMessage(PatternType.UNKNOWN, MessageType.NONE);
    }

}
