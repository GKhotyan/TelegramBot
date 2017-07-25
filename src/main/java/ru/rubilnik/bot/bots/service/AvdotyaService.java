package ru.rubilnik.bot.bots.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alexey on 20.07.2017.
 */
@Component
public class AvdotyaService extends DefaultService implements BotService {

    private static final String URL = "http://calendareveryday.ru";

    public FullMessage getMessage(MessageCommand command) {
        String result = "Ничего не найдено.";
        try {
            String url = URL + "/?id=narodn/" + currentDate();
            Document doc = Jsoup.parse(new URL(url), 30000);
            Elements elements = doc.select("div#content > p");
            if (elements != null && elements.size() >= 2) {
                String header = elements.get(1).text();
                if (!StringUtils.isEmpty(header)) {
                    if (header.contains("(")) {
                        header = header.substring(0, header.indexOf("(")).trim() + ".\n";
                    }
                } else {
                    header = "";
                }
                result = header + elements.get(2).text();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FullMessage(createMessage(result, command.getParsedMessage().getChatId()), PatternType.AVDOTYA, MessageType.NORMAL);
    }

    private String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d");
        return sdf.format(new Date());
    }

}
