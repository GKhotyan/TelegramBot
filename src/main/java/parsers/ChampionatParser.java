package parsers;

import data.ChampionatNewsData;
import data.serialize.ChampionatNewsSerializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import populators.RfplNewsPopulator;
import utils.TelegramSender;

import java.io.IOException;
import java.net.URL;

@Component
public class ChampionatParser {

  @Autowired
  ChampionatNewsData championatNewsData;
  @Autowired
  ChampionatNewsSerializer championatNewsSerializer;
  @Autowired
  RfplNewsPopulator rfplNewsPopulator;
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
          String populated_news = rfplNewsPopulator.populate(result);
          championatNewsData.addToCurrentNews(key, populated_news);
          if(!championatNewsData.isPostedKey(key)&&!populated_news.equals(result)){
            telegramSender.send(populated_news);
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
        ChampionatParser championatParser = new ChampionatParser();
        championatParser.parseNews();

  }
}
