package bots;

import java.util.Random;
import java.util.stream.Stream;

import messages.AvdotyaMessenger;
import messages.LiveScoresMessenger;
import messages.Messenger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
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

  ContactCount contactCount = ContactCount.emptyInstance();

  public RubilnikBot(ApplicationContext appContext) {
    this.appContext = appContext;
  }

  private ApplicationContext appContext;
  String championatNewsPatterns = "новост:что нового";
  String anekdotPatterns = "боян:баян:анекдот";
  String scorePatterns = "счет:счёт:как сыграл:кто ведет";
  String avdotyaPatterns = "авдотья";
  String swearPatterns = "бот";

  private String getContact(Message message) {
    if(message!=null && message.getContact()!=null) {
      return message.getContact().getFirstName();
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
        contactCount.updateContact(contact, RequestType.NEWS);
      }
      else if (coincidence(message_text, anekdotPatterns)) {
        AnekdotParser anekdotParser = (AnekdotParser) appContext.getBean("anekdotParser");
        String anekdot = anekdotParser.getAnekdot();
        message = new SendMessage().setChatId(chat_id).setText(anekdot);
        contactCount.updateContact(contact, RequestType.ANEKDOT);
      }
      else if (coincidence(message_text, scorePatterns)) {
        Messenger parser = (LiveScoresMessenger) appContext.getBean("liveScoresMessenger");
        message = new SendMessage().setChatId(chat_id).setText(parser.getMessage());
        contactCount.updateContact(contact, RequestType.LIVE);
      }
      else if (coincidence(message_text, avdotyaPatterns)) {
        Messenger parser = (AvdotyaMessenger) appContext.getBean("avdotyaMessenger");
        message = new SendMessage().setChatId(chat_id).setText(parser.getMessage());
        contactCount.updateContact(contact, RequestType.AVDOTYA);
      }
      else if (coincidence(message_text, swearPatterns)) {
        message = new SendMessage().setChatId(chat_id).setText(Porter.transform(message_text));
        contactCount.updateContact(contact, RequestType.SWEAR);
      }

      try{
          if (contactCount.isOverflowed() && message != null) {
              String name = contactCount.getName();
              String[] mess =
                      {
                              name + ", ты заебал.",
                              name + ", с тобой не общаюсь.",
                              "Отвали.", "" +
                              "Я занят, непонятно, " + name + " ?",
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
