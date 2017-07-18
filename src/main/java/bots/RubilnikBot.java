package bots;

import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import data.ChampionatNewsData;
import parsers.AnekdotParser;

@Component
public class RubilnikBot extends TelegramLongPollingBot {
  public RubilnikBot(ApplicationContext appContext) {
    this.appContext = appContext;
  }

  private ApplicationContext appContext;
  String championatImportantNewsPatterns = "интересные новости";
  String championatNewsPatterns = "бот:новости:еще новости:ещё новости";
  String anekdotPatterns = "боян:баян:анекдот";

  @Override
  public void onUpdateReceived(Update update) {
    // We check if the update has a message and the message has text
    SendMessage message = null;
    String message_text = null;
    Long chat_id = null;
    if (update.getChannelPost()!=null&& update.getChannelPost().hasText()) {
      message_text = update.getChannelPost().getText();
      chat_id = update.getChannelPost().getChatId();
    }
    else if (update.getMessage()!=null && update.getMessage().hasText()) {
      message_text = update.getMessage().getText();
      chat_id = update.getMessage().getChatId();
    }

    if(message_text!=null && chat_id!=null) {
      if (coincidence(message_text, championatImportantNewsPatterns)) {
        ChampionatNewsData championatNewsData = (ChampionatNewsData) appContext.getBean("championatNewsData");
        String news = championatNewsData.getNextImportantNews();
        message = new SendMessage().setChatId(chat_id).setText(news);
      }
      else if (coincidence(message_text, championatNewsPatterns)) {
        ChampionatNewsData championatNewsData = (ChampionatNewsData) appContext.getBean("championatNewsData");
        String news = championatNewsData.getNextNews();
        message = new SendMessage().setChatId(chat_id).setText(news);
      }
      else if (coincidence(message_text, anekdotPatterns)) {
        AnekdotParser anekdotParser = (AnekdotParser) appContext.getBean("anekdotParser");
        String anekdot = anekdotParser.getAnekdot();
        message = new SendMessage().setChatId(chat_id).setText(anekdot);
      }

      try{
        sendMessage(message);
      }
      catch (TelegramApiException e) {
        e.printStackTrace();
      }

    }

  }

  private boolean coincidence(String text, String pattern){
    return Stream.of(pattern.split(":")).anyMatch(e -> text.toLowerCase().contains(e));
  }

  @Override
  public String getBotUsername() {
    return appContext.getEnvironment().getProperty("bot.name");
  }

  @Override
  public String getBotToken() {
    return appContext.getEnvironment().getProperty("bot.tocken");
  }


}
