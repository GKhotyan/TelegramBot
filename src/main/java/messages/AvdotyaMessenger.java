package messages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
public class AvdotyaMessenger implements Messenger {

    private static final String URL = "http://calendareveryday.ru";

    public String getMessage() {
        try {
            String url = URL + "/?id=narodn/" + currentDate();
            Document doc = Jsoup.parse(new URL(url), 30000);
            Elements elements = doc.select("div#content > p");
            if (elements != null && elements.size() >= 2) {
                String header = elements.get(1).text();
                if(!StringUtils.isEmpty(header)) {
                    if(header.contains("(")) {
                        header = header.substring(0, header.indexOf("(")).trim() + ".\n";
                    }
                }
                else {
                    header = "";
                }
                return header + elements.get(2).text();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Ничего не найдено. Все из-за VitalyVk";
    }

    private String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d");
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(new AvdotyaMessenger().getMessage());
    }


}
