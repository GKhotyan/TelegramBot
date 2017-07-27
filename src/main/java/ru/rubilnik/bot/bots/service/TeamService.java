package ru.rubilnik.bot.bots.service;

import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.bots.service.data.ChampionatTeams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * Created by Alexey on 20.07.2017.
 */
@Component
public class TeamService extends DefaultService implements BotCommand {

    private static final String URL = "https://www.championat.com/football/";

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String name = extractName(command);
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
        } catch (Throwable e) {
            //do nothing
        }
        return new FullMessage(createMessage(result, command.getParsedMessage().getChatId()), PatternType.ABOUT_TEAM, MessageType.NORMAL);
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

    private String extractName(MessageCommand command) {
        return command.getParsedMessage().getMessage().trim().substring(command.getPattern().length()).replace("?", "").trim();
    }

}
