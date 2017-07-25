package ru.rubilnik.bot.bots.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.populators.RfplNewsPopulator;
import ru.rubilnik.bot.utils.net.WebClient;

import java.util.Optional;

/**
 * Created by Alexey on 20.07.2017.
 */
@Component
public class LiveScoresService extends DefaultService implements BotService {

    private final RfplNewsPopulator rfplNewsPopulator;
    private final WebClient webClient;

    private final static String SCORE_URL = "https://www.championat.com/live/live.json";

    @Autowired
    public LiveScoresService(RfplNewsPopulator rfplNewsPopulator, WebClient webClient) {
        this.rfplNewsPopulator = rfplNewsPopulator;
        this.webClient = webClient;
    }

    public FullMessage getMessage(MessageCommand command) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        try {
            Optional<JSONObject> json = webClient.httpGet(SCORE_URL);
            json.ifPresent(j -> {
                JSONObject football = (JSONObject) j.get("football");
                JSONObject match_data = (JSONObject) football.get("match_data");
                for (Object key : match_data.keySet()) {
                    JSONObject champ = (JSONObject) match_data.get(key);
                    String title = "*" + champ.get("name") + "*\n---------------\n";
                    JSONArray data = (JSONArray) champ.get("m_data");
                    StringBuilder results = new StringBuilder();
                    for (Object d : data) {
                        JSONObject match = (JSONObject) d;
                        if (match.get("on_main").equals("1")) {
                            results.append(match.get("time")).append(" ").append(match.get("name_m")).append(" ").append(match.get("sb")).append(" (").append(match.get("status")).append(")\n");
                        }
                    }
                    if (results.length() != 0) {
                        sb.append(title).append(results);
                        sb.append("\n");
                    }
                }
            });
            result = sb.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            result = "Все пошло по пизде.";
        }
        if (sb.length() == 0) {
            result = "Бот не смог ничего найти. Виноват - Виталик.";
        }
        return new FullMessage(createMessage(rfplNewsPopulator.populate(result), command.getParsedMessage().getChatId()), PatternType.LIVE, MessageType.NORMAL);
    }

}
