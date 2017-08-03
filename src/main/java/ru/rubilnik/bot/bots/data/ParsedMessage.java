package ru.rubilnik.bot.bots.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Optional;

/**
 * Created by Alexey on 23.07.2017.
 */
public final class ParsedMessage {

    private String message = null;
    @Getter
    private Contact contact = null;
    @Getter
    private Long chatId = null;
    @Getter
    private Optional<CallbackData> callbackData;

    public String getMessage() {
        return callbackData.map(CallbackData::getCommand).orElse(message);
    }

    public ParsedMessage(Update update) {
        this.callbackData = getCallback(update);
        if(!callbackData.isPresent()) {
            this.message = getOriginalMessage(update);
            this.contact = getContact(update);
            this.chatId = getChatId(update);
        }
    }

    private Optional<CallbackData> getCallback(Update update) {
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            return Optional.of(new CallbackData(messageId, callData, chatId));
        }
        return Optional.empty();
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

    private Contact getContact(Update update) {
        if (update.getChannelPost() != null) {
            return new Contact(getContactInternal(update.getChannelPost()), update.getChannelPost().getFrom().getId());
        } else if (update.getMessage() != null) {
            return new Contact(getContactInternal(update.getMessage()), update.getMessage().getFrom().getId());
        }
        throw new IllegalArgumentException("No contact");
    }

    private String getContactInternal(Message message) {
        if (message != null && message.getFrom() != null) {
            return message.getFrom().getUserName() == null ? message.getFrom().getFirstName() : message.getFrom().getUserName();
        }
        return "Anonymous";
    }

    @AllArgsConstructor
    public class Contact {
        @Getter
        String name;
        @Getter
        Integer id;
    }

    @AllArgsConstructor
    public class CallbackData {
        @Getter
        private final Long messageId;
        @Getter
        private String command;
        @Getter
        private Long chatId;
    }

}
