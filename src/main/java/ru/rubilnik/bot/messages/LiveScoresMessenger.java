package ru.rubilnik.bot.messages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rubilnik.bot.populators.RfplNewsPopulator;
import ru.rubilnik.bot.utils.net.JsonClient;
import ru.rubilnik.bot.utils.net.WebClient;

import java.util.Optional;

/**
 * Created by Alexey on 20.07.2017.
 */
@Component
public class LiveScoresMessenger implements Messenger {

    @Autowired
    private
    RfplNewsPopulator rfplNewsPopulator;
    @Autowired
    private WebClient webClient;

    private final static String SCORE_URL = "https://www.championat.com/live/live.json";

    public String getMessage() {
        StringBuilder result = new StringBuilder();
        try {
            Optional<JSONObject> json = webClient.httpGet(SCORE_URL);
            json.ifPresent(j -> {
                JSONObject football = (JSONObject) j.get("football");
                JSONObject match_data = (JSONObject) football.get("match_data");
                for (Object key : match_data.keySet()) {
                    JSONObject champ = (JSONObject) match_data.get(key);
                    String title = champ.get("name") + "\n---------------\n";
                    JSONArray data = (JSONArray) champ.get("m_data");
                    StringBuilder results = new StringBuilder();
                    for (Object d : data) {
                        JSONObject match = (JSONObject) d;
                        if (match.get("on_main").equals("1")) {
                            results.append(match.get("time")).append(" ").append(match.get("name_m")).append(" ").append(match.get("result")).append(" (").append(match.get("status")).append(")\n");
                        }
                    }
                    if (results.length() != 0) {
                        result.append(title).append(results);
                    }
                    result.append("\n");
                }
            });
        } catch (Throwable th) {
            th.printStackTrace();
            return "Все пошло по пизде.";
        }
        if (result.length() == 0) {
            return "Бот не смог ничего найти. Виноват - Виталик.";
        }
        return rfplNewsPopulator.populate(result.toString());
    }

    @Override
    public String getMessage(String name) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        System.out.println(new LiveScoresMessenger().getMessage());
    }

}
