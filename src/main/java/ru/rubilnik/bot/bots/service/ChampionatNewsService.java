package ru.rubilnik.bot.bots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.data.ChampionatNewsData;
import ru.rubilnik.bot.data.serialize.ChampionatNewsSerializer;
import ru.rubilnik.bot.utils.Porter;

/**
 * Created by Alexey on 26.07.2017.
 */

@Service
public class ChampionatNewsService extends DefaultService implements BotService {

    private final ChampionatNewsData championatNewsData;
    private final ChampionatNewsSerializer championatNewsSerializer;

    @Autowired
    public ChampionatNewsService(ChampionatNewsData championatNewsData, ChampionatNewsSerializer championatNewsSerializer) {
        this.championatNewsData = championatNewsData;
        this.championatNewsSerializer = championatNewsSerializer;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String news = championatNewsData.getNextNews();
        championatNewsSerializer.serializePostedKeys();
        return new FullMessage(createMessage(news, command.getParsedMessage().getChatId()), PatternType.NEWS, MessageType.NORMAL);
    }

}

