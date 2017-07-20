package messages;

import messages.data.ChampionatTeams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alexey on 20.07.2017.
 */
@Component
public class TeamMessenger implements Messenger {

    private static final String URL = "https://www.championat.com/football/";

    @Override
    public String getMessage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMessage(String name) {
        String result = "К сожалению я ничего не знаю про " + name;
        try {
            String url = createURL(name);
            Document doc = Jsoup.parse(new URL(url), 30000);
            Elements elements = doc.select(".sport__table > table > tbody > tr");
            if (elements != null && elements.size() >= 1) {
                int nextGameIndex = getNextGameIndex(elements);
                if(nextGameIndex>0) {
                    result = parseLastGame(elements.get(nextGameIndex - 1)) + "\n";
                }
                result += parseNextGame(elements.get(nextGameIndex));
            }
            return result;
        } catch (Throwable e) {
            //do nothing
        }
        return result;
    }

    private int getNextGameIndex(Elements elements) {
        int index = -1;
        for (Element e : elements) {
            //skip first row
            if(index != -1) {
                if (e.select("td").get(4).text().contains("-:-")) {
                    return index + 1;
                }
            }
            index++;
        }
        throw new IllegalArgumentException();
    }

    private String parseLastGame(Element element) {
        Elements data = element.select("td");
        String teams = data.get(3).text();
        String score = data.get(4).text();
        return "Последний матч:\n" + teams + " " + score;
    }

    private String parseNextGame(Element element) {
        Elements data = element.select("td");
        String teams = data.get(3).text();
        String date = data.get(1).text();
        String time = data.get(2).text();
        String tour = data.get(0).text();
        return "Следующий матч:\n" + tour + ": " + date + " " + time + "\n" + teams;
    }

    private String createURL(String teamName) {
        ChampionatTeams.TeamInfo teamInfo = ChampionatTeams.getTeam(teamName);
        if (teamInfo != null) {
            return URL + teamInfo.getDivision() + "/team/" + teamInfo.getTeamId() + "/result.html";
        } else throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        System.out.println(new TeamMessenger().getMessage("Арарат"));
    }


}
