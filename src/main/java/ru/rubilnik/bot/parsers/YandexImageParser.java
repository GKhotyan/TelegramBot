package ru.rubilnik.bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

@Component
public class YandexImageParser {
    private static final String url = "https://yandex.ru/images/search?text=";

    public String getRandomImageURL(String text){
        try{
            String full_url = url+text.replaceAll(" ", "%20");
            Document doc = Jsoup.parse(new URL(full_url), 30000);
            Elements elements = doc.select("img[class=serp-item__thumb]");
            if(elements.size()>0) {
                Random rand = new Random();
                int index = rand.nextInt(elements.size());
                return "http:" + elements.get(index).attr("src");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        YandexImageParser yandexImageParser = new YandexImageParser();
        yandexImageParser.getRandomImageURL("греция");

    }
}
