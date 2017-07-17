package parsers;

import java.io.IOException;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;

import data.ChampionatNewsData;

@Component
public class ChampionatParser {

  @Autowired
  ChampionatNewsData championatNewsData;

  String url = "https://www.championat.com/football/_russiapl.html";

  public void parseNews(){
    try {
      Document doc = Jsoup.parse(new URL(url), 30000);
      Elements elements = doc.select("div[class=news__i]");
      for(Element element : elements){
        String key = element.select("span[class=counter _comments _icon js-comments-count]").attr("data-id");
        String value = element.select("span[class=news__i__text]").text();
        if(value.equals("")){
          value = element.select("span[class=news__i__text _important]").text();
        }
        if(!championatNewsData.isCurrentNewsContains(key))
          championatNewsData.addToCurrentNews(key, value);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String [] args){
    ChampionatParser championatParser = new ChampionatParser();
    championatParser.parseNews();
  }
}
