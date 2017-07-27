package ru.rubilnik.bot.bots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.rubilnik.bot.bots.data.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class RubilnikBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;
    @Value("${bot.tocken}")
    private String botToken;
    private final PatternList patternList;
    private final Map<Long, ContactCount> contactCounts = new HashMap<>();

    @Autowired
    public RubilnikBot(PatternList patternList) {
        this.patternList = patternList;
    }

    @Override
    public void onUpdateReceived(Update update) {

        ParsedMessage parsedMessage = new ParsedMessage(update);

        try {
            FullMessage fullMessage = patternList.getMessage(update);
            updateContact(parsedMessage.getChatId(), parsedMessage.getContact().getName(), fullMessage.getPatternType());
            if (fullMessage.getMessageType() == MessageType.NORMAL) {
                sendChatMessage(fullMessage.getBotApiMethod(), parsedMessage);
            } else if (fullMessage.getMessageType() == MessageType.PHOTO) {
                sendPhoto(fullMessage.getPhotoMethod());
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendChatMessage(BotApiMethod<Message> message, ParsedMessage parsedMessage) {
        try {
            if (message != null && contactCounts.get(parsedMessage.getChatId()) != null && contactCounts.get(parsedMessage.getChatId()).isOverflowed()) {
                String name = contactCounts.get(parsedMessage.getChatId()).getName();
                String[] mess =
                        {
                                name + ", ты заебал",
                                name + ", с тобой не общаюсь",
                                "Отвали", "" +
                                "Я занят, непонятно, " + name + "?"
                        };
                Random rand = new Random();
                sendMessage(new SendMessage().setChatId(parsedMessage.getChatId()).setText(mess[rand.nextInt(mess.length)]));
            } else if (message != null) {
                this.sendApiMethod(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void updateContact(Long chatId, String name, PatternType type) {
        ContactCount contactCount = contactCounts.get(chatId);
        if (contactCount == null) {
            contactCount = ContactCount.emptyInstance();
        }
        contactCount.updateContact(name, type);
        contactCounts.put(chatId, contactCount);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


}
