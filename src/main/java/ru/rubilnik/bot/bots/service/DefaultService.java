package ru.rubilnik.bot.bots.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;

import java.io.InputStream;

/**
 * Created by Alexey on 25.07.2017.
 */
public abstract class DefaultService {

    public SendMessage createMessage(String text, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId).setText(text);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    public SendPhoto createPhoto(InputStream is, Long chatId) {
        return new SendPhoto().setChatId(chatId).setNewPhoto("image", is);
    }

}
