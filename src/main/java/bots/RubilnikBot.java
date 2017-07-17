package bots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import data.ChampionatNewsData;
import populators.RfplNewsPopulator;

@Component
public class RubilnikBot extends TelegramLongPollingBot {
  public RubilnikBot(ApplicationContext appContext) {
    this.appContext = appContext;
  }

  private ApplicationContext appContext;

  @Override
  public void onUpdateReceived(Update update) {
    // We check if the update has a message and the message has text
    if (update.getChannelPost().hasText()) {
      String message_text = update.getChannelPost().getText();
      if(message_text.toLowerCase().contains("бот") || message_text.toLowerCase().equals("еще")) {
        long chat_id = update.getChannelPost().getChatId();

        ChampionatNewsData championatNewsData = (ChampionatNewsData)appContext.getBean("championatNewsData");
        RfplNewsPopulator rfplNewsPopulator = (RfplNewsPopulator)appContext.getBean("rfplNewsPopulator");
        String news = championatNewsData.getNextNews();
        String populated_news = rfplNewsPopulator.populate(news);
        SendMessage message = new SendMessage().setChatId(chat_id).setText(populated_news);
        try {
          sendMessage(message);
        }
        catch (TelegramApiException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public String getBotUsername() {
    return "RatingHelperBot";
  }

  @Override
  public String getBotToken() {
    return "262114337:AAHwzwVOaQ-LYm_2YRgPLYmYMr1xFEa1mfI";
  }
}
