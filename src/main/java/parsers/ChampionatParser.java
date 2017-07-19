package parsers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;

import data.ChampionatNewsData;
import populators.RfplNewsPopulator;
import utils.net.JsonClient;
import utils.net.WebClient;

@Component
public class ChampionatParser {

    @Autowired
    ChampionatNewsData championatNewsData;
    @Autowired
    RfplNewsPopulator rfplNewsPopulator;

    @Autowired
    private WebClient webClient;

    private static final int DEFAULT_TIMEOUT_MILLIS = 30000;
    String url = "https://www.championat.com/football/_russiapl.html";
    private final static String SCORE_URL = "https://www.championat.com/live/live.json";


    public void parseNews() {
        try {

            Document doc = Jsoup.parse(new URL(url), 30000);
            Elements elements = doc.select("div[class=news__i]");
            championatNewsData.clearCurrentNews();
            for (Element element : elements) {
                String key = element.select("span[class=counter _comments _icon js-comments-count]").attr("data-id");
                String value = element.select("span[class=news__i__text]").text();
                if (value.equals("")) {
                    value = element.select("span[class=news__i__text _important]").text();
                    championatNewsData.addToImportant(key);
                }
                if (!championatNewsData.isCurrentNewsContains(key)) {
                    String populated_news = rfplNewsPopulator.populate(value);
                    championatNewsData.addToCurrentNews(key, populated_news);
                    if (!populated_news.equals(value))
                        championatNewsData.addToImportant(key);
                }
            }

            //old news removing
            championatNewsData.clearOld();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getScores() {
        StringBuilder result = new StringBuilder();
        try {
            Optional<JSONObject> json = new JsonClient().httpGet(SCORE_URL);
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
                            results.append(match.get("time")).append(" ").append(match.get("name")).append(" ").append(match.get("result")).append(" (").append(match.get("status")).append(")\n");
                        }
                    }
                    if (results.length() != 0) {
                        result.append(title).append(results);
                    }
                    result.append("\n");
                }
            });
        }
        catch (Throwable th) {
            return "Все пошло по пизде.";
        }
        if(result.length() == 0) {
            return "Бот не смог ничего неайти. Неизвестно почему.";
        }
        return rfplNewsPopulator.populate(result.toString());
    }

    public static void main(String[] args) {
        ChampionatParser championatParser = new ChampionatParser();
        championatParser.getScores();

    }
}
