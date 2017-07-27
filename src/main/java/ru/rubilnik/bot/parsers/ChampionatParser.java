package ru.rubilnik.bot.parsers;

import ru.rubilnik.bot.data.ChampionatNewsData;
import ru.rubilnik.bot.data.serialize.ChampionatNewsSerializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rubilnik.bot.populators.RfplNewsPopulator;
import ru.rubilnik.bot.utils.TelegramSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ChampionatParser {

  @Autowired
  ChampionatNewsData championatNewsData;
  @Autowired
  ChampionatNewsSerializer championatNewsSerializer;
  @Autowired
  TelegramSender telegramSender;

  private static final String url = "https://www.championat.com/football/_russiapl.html";

  public void parseNews(){
    try {
      if(championatNewsData.getPostedKeysSize()==0)
        championatNewsSerializer.deserializePostedKeys();

      Document doc = Jsoup.parse(new URL(url), 30000);
      Elements elements = doc.select("div[class=news__i]");
      championatNewsData.clearCurrentNews();
      for(Element element : elements){
        String key = element.select("span[class=counter _comments _icon js-comments-count]").attr("data-id");
        Elements textElements = element.select("span[class=news__i__text _important]");
        if(textElements.size()>0 && !championatNewsData.isCurrentNewsContains(key)) {
          String result = textElements.text();
          championatNewsData.addToCurrentNews(key, result);
          if(!championatNewsData.isPostedKey(key)){
            telegramSender.send(result);
            championatNewsData.addPostedKey(key);
            championatNewsSerializer.serializePostedKeys();
          }
        }
      }

      //old news removing
      championatNewsData.clearOld();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

       public static void main(String[] args) {

       }
}
