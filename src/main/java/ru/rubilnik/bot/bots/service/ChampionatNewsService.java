package ru.rubilnik.bot.bots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.data.ChampionatNewsData;
import ru.rubilnik.bot.data.serialize.ChampionatNewsSerializer;
import ru.rubilnik.bot.populators.RfplNewsPopulator;

/**
 * Created by Alexey on 26.07.2017.
 */

@Service
public class ChampionatNewsService extends DefaultService implements BotService {

    private final ChampionatNewsData championatNewsData;
    private final ChampionatNewsSerializer championatNewsSerializer;
    private final  RfplNewsPopulator rfplNewsPopulator;

    @Autowired
    public ChampionatNewsService(ChampionatNewsData championatNewsData, ChampionatNewsSerializer championatNewsSerializer, RfplNewsPopulator rfplNewsPopulator) {
        this.championatNewsData = championatNewsData;
        this.championatNewsSerializer = championatNewsSerializer;
        this.rfplNewsPopulator = rfplNewsPopulator;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String news = rfplNewsPopulator.populate(championatNewsData.getNextNews());
        championatNewsSerializer.serializePostedKeys();
        return new FullMessage(createMessage(news, command.getParsedMessage().getChatId()), PatternType.NEWS, MessageType.NORMAL);
    }

}

