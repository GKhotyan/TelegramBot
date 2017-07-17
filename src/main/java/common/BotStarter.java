package common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import bots.RubilnikBot;
import data.ChampionatNewsData;
import parsers.ChampionatParser;

@Component
public class BotStarter {
  @Autowired
  ApplicationContext appContext;
  @Autowired
  ChampionatParser   championatParser;

  @PostConstruct
  public void initialization() {
    championatParser.parseNews();
    // Initialize Api Context
    ApiContextInitializer.init();

    // Instantiate Telegram Bots API
    TelegramBotsApi botsApi = new TelegramBotsApi();

    // Register our bot
    try {
      botsApi.registerBot(new RubilnikBot(appContext));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
