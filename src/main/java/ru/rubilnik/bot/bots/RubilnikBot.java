package ru.rubilnik.bot.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import ru.rubilnik.bot.messages.AvdotyaMessenger;
import ru.rubilnik.bot.messages.LiveScoresMessenger;
import ru.rubilnik.bot.messages.Messenger;
import ru.rubilnik.bot.messages.TeamMessenger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ru.rubilnik.bot.data.ChampionatNewsData;
import ru.rubilnik.bot.data.serialize.ChampionatNewsSerializer;
import ru.rubilnik.bot.parsers.AnekdotParser;
import ru.rubilnik.bot.utils.Porter;

@Component
public class RubilnikBot extends TelegramLongPollingBot {

  private Map<Long, ContactCount> contactCounts = new HashMap<>();

  public RubilnikBot(ApplicationContext appContext) {
    this.appContext = appContext;
  }

  private ApplicationContext appContext;
  String championatNewsPatterns = "новост:что нового";
  String anekdotPatterns = "боян:баян:анекдот";
  String scorePatterns = "счет:счёт:как сыграл:кто ведет";
  String avdotyaPatterns = "авдотья";
  String aboutPatterns = "а что";
  String swearPatterns = "бот";

  private String getContact(Message message) {
    if(message!=null && message.getFrom()!=null) {
      return message.getFrom().getUserName()==null?message.getFrom().getFirstName():message.getFrom().getUserName();
    }
    return null;
  }

  @Override
  public void onUpdateReceived(Update update) {
    // We check if the update has a message and the message has text
    SendMessage message = null;
    String message_text = null;
    Long chat_id = null;
    String contact = null;
    if (update.getChannelPost()!=null&& update.getChannelPost().hasText()) {
      message_text = update.getChannelPost().getText();
      chat_id = update.getChannelPost().getChatId();
      contact = getContact(update.getChannelPost());
    }
    else if (update.getMessage()!=null && update.getMessage().hasText()) {
      message_text = update.getMessage().getText();
      chat_id = update.getMessage().getChatId();
      contact = getContact(update.getMessage());
    }

    if(message_text!=null && chat_id!=null) {
      if (coincidence(message_text, championatNewsPatterns)) {
        ChampionatNewsData championatNewsData = (ChampionatNewsData) appContext.getBean("championatNewsData");
        ChampionatNewsSerializer championatNewsSerializer = (ChampionatNewsSerializer) appContext.getBean("championatNewsSerializer");
        String news = championatNewsData.getNextNews();
        championatNewsSerializer.serializePostedKeys();
        message = new SendMessage().setChatId(chat_id).setText(news);
        updateContact(chat_id, contact, RequestType.NEWS);
      }
      else if (coincidence(message_text, anekdotPatterns)) {
        AnekdotParser anekdotParser = (AnekdotParser) appContext.getBean("anekdotParser");
        String anekdot = anekdotParser.getAnekdot();
        message = new SendMessage().setChatId(chat_id).setText(anekdot);
        updateContact(chat_id, contact, RequestType.ANEKDOT);
      }
      else if (coincidence(message_text, scorePatterns)) {
        Messenger parser = (LiveScoresMessenger) appContext.getBean("liveScoresMessenger");
        message = new SendMessage().setChatId(chat_id).setText(parser.getMessage());
        updateContact(chat_id, contact, RequestType.LIVE);
      }
      else if (coincidence(message_text, avdotyaPatterns)) {
        Messenger parser = (AvdotyaMessenger) appContext.getBean("avdotyaMessenger");
        message = new SendMessage().setChatId(chat_id).setText(parser.getMessage());
        updateContact(chat_id, contact, RequestType.AVDOTYA);
      }
      else if (coincidence(message_text, aboutPatterns)) {
          Messenger parser = (TeamMessenger) appContext.getBean("teamMessenger");
          message = new SendMessage().setChatId(chat_id).setText(parser.getMessage(message_text.trim().substring(aboutPatterns.length()).replace("?", "").trim()));
          updateContact(chat_id, contact, RequestType.ABOUT_TEAM);
      }
      else if (coincidence(message_text, swearPatterns)) {
        message = new SendMessage().setChatId(chat_id).setText(Porter.transform(message_text));
        updateContact(chat_id, contact, RequestType.SWEAR);
      }

      try{
          if (contactCounts.get(chat_id)!=null && contactCounts.get(chat_id).isOverflowed() && message != null) {
              String name = contactCounts.get(chat_id).getName();
              String[] mess =
                      {
                              name + ", ты заебал",
                              name + ", с тобой не общаюсь",
                              "Отвали", "" +
                              "Я занят, непонятно, " + name + "?",
                              "Ладно, " + name + ":\n" + message
                      };
              Random rand = new Random();
              sendMessage(new SendMessage().setChatId(chat_id).setText(mess[rand.nextInt(mess.length)]));
          } else if (message != null)
              sendMessage(message);
      }
      catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

  private void updateContact(Long chatId, String name, RequestType type) {
      ContactCount contactCount = contactCounts.get(chatId);
      if(contactCount == null) {
          contactCount = ContactCount.emptyInstance();
      }
      contactCount.updateContact(name, type);
      contactCounts.put(chatId, contactCount);
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