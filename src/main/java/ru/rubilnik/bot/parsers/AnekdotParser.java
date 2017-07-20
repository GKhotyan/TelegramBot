package ru.rubilnik.bot.parsers;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class AnekdotParser {
  String url = "https://www.anekdot.ru/random/anekdot/";

  public String getAnekdot(){
    String result = null;
    try {
      Document doc = Jsoup.parse(new URL(url), 30000);
      result = doc.select("div[class=text]").first().text();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void main(String [] args){
    AnekdotParser anekdotParser = new AnekdotParser();
    System.out.print(anekdotParser.getAnekdot());
  }
}
