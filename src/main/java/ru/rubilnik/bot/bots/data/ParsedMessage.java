package ru.rubilnik.bot.bots.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by Alexey on 23.07.2017.
 */
public final class ParsedMessage {

    @Getter
    private final String message;
    @Getter
    private final String contact;
    @Getter
    private final Long chatId;

    public ParsedMessage(Update update) {
        this.message = getOriginalMessage(update);
        this.contact = getContact(update);
        this.chatId = getChatId(update);
    }

    private String getOriginalMessage(Update update) {
        if (update.getChannelPost() != null && update.getChannelPost().hasText()) {
            return update.getChannelPost().getText();
        } else if (update.getMessage() != null && update.getMessage().hasText()) {
            return update.getMessage().getText();
        }
        throw new IllegalArgumentException("No message");
    }

    private Long getChatId(Update update) {
        if (update.getChannelPost() != null) {
            return update.getChannelPost().getChatId();
        } else if (update.getMessage() != null) {
            return update.getMessage().getChatId();
        }
        throw new IllegalArgumentException("No chat Id");
    }

    private String getContact(Update update) {
        if (update.getChannelPost() != null) {
            return getContactInternal(update.getChannelPost());
        } else if (update.getMessage() != null) {
            return getContactInternal(update.getMessage());
        }
        throw new IllegalArgumentException("No contact");
    }

    private String getContactInternal(Message message) {
        if (message != null && message.getFrom() != null) {
            return message.getFrom().getUserName() == null ? message.getFrom().getFirstName() : message.getFrom().getUserName();
        }
        return "Anonymous";
    }

}
