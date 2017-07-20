package bots;

import java.util.stream.Stream;

import messages.AvdotyaMessenger;
import messages.LiveScoresMessenger;
import messages.Messenger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import data.ChampionatNewsData;
import data.serialize.ChampionatNewsSerializer;
import parsers.AnekdotParser;
import parsers.ChampionatParser;
import utils.Porter;

@Component
public class RubilnikBot extends TelegramLongPollingBot {
  public RubilnikBot(ApplicationContext appContext) {
    this.appContext = appContext;
  }

  private ApplicationContext appContext;
  String championatNewsPatterns = "новост:что нового";
  String anekdotPatterns = "боян:баян:анекдот";
  String scorePatterns = "счет:счёт:как сыграл:кто ведет";
  String avdotyaPatterns = "авдотья";
  String swearPatterns = "бот";

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
      if (coincidence(message_text, championatNewsPatterns)) {
        ChampionatNewsData championatNewsData = (ChampionatNewsData) appContext.getBean("championatNewsData");
        ChampionatNewsSerializer championatNewsSerializer = (ChampionatNewsSerializer) appContext.getBean("championatNewsSerializer");
        String news = championatNewsData.getNextNews();
        championatNewsSerializer.serializePostedKeys();
        message = new SendMessage().setChatId(chat_id).setText(news);
      }
      else if (coincidence(message_text, anekdotPatterns)) {
        AnekdotParser anekdotParser = (AnekdotParser) appContext.getBean("anekdotParser");
        String anekdot = anekdotParser.getAnekdot();
        message = new SendMessage().setChatId(chat_id).setText(anekdot);
      }
      else if (coincidence(message_text, scorePatterns)) {
        Messenger parser = (LiveScoresMessenger) appContext.getBean("liveScoresMessenger");
        message = new SendMessage().setChatId(chat_id).setText(parser.getMessage());
      }
      else if (coincidence(message_text, avdotyaPatterns)) {
        Messenger parser = (AvdotyaMessenger) appContext.getBean("avdotyaMessenger");
        message = new SendMessage().setChatId(chat_id).setText(parser.getMessage());
      }
      else if (coincidence(message_text, swearPatterns)) {
        message = new SendMessage().setChatId(chat_id).setText(Porter.transform(message_text));
      }

      try{
        if(message!=null)
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
