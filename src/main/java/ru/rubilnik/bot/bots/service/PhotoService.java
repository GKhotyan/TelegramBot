package ru.rubilnik.bot.bots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.parsers.YandexImageParser;
import ru.rubilnik.bot.utils.ImagesUtil;

import java.io.InputStream;

/**
 * Created by Alexey on 25.07.2017.
 */
@Service
public class PhotoService extends DefaultService implements BotCommand {

    private final YandexImageParser yandexImageParser;
    private final ImagesUtil imagesUtil;

    @Autowired
    public PhotoService(YandexImageParser yandexImageParser, ImagesUtil imagesUtil) {
        this.yandexImageParser = yandexImageParser;
        this.imagesUtil = imagesUtil;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String result = "Ничего не получилось найти";
        String substring = command.getParsedMessage().getMessage().substring(command.getPattern().length()).trim();
        try {
            String imageUrl = yandexImageParser.getRandomImageURL(substring);
            if (imageUrl != null) {
                InputStream is = imagesUtil.getImageInputStream(imageUrl);
                return new FullMessage(createPhoto(is, command.getParsedMessage().getChatId()), PatternType.PHOTO, MessageType.PHOTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FullMessage(createMessage(result, command.getParsedMessage().getChatId()), PatternType.PHOTO, MessageType.NORMAL);
    }
}
