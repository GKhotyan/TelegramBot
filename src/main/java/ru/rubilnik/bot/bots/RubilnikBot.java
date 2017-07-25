package ru.rubilnik.bot.bots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.rubilnik.bot.bots.data.*;
import ru.rubilnik.bot.data.ChampionatNewsData;
import ru.rubilnik.bot.data.serialize.ChampionatNewsSerializer;
import ru.rubilnik.bot.parsers.AnekdotParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@Component
public class RubilnikBot extends TelegramLongPollingBot {

    private final PatternList patternList;
    private final ApplicationContext appContext;
    private final Map<Long, ContactCount> contactCounts = new HashMap<>();

    @Autowired
    public RubilnikBot(ApplicationContext appContext, PatternList patternList) {
        this.appContext = appContext;
        this.patternList = patternList;
    }

    private String championatNewsPatterns = "новост:что нового";
    private String anekdotPatterns = "боян:баян:анекдот";

    @Override
    public void onUpdateReceived(Update update) {

        BotApiMethod<Message> message = null;
        ParsedMessage parsedMessage = new ParsedMessage(update);

        if (coincidence(parsedMessage.getMessage(), championatNewsPatterns)) {
            ChampionatNewsData championatNewsData = (ChampionatNewsData) appContext.getBean("championatNewsData");
            ChampionatNewsSerializer championatNewsSerializer = (ChampionatNewsSerializer) appContext.getBean("championatNewsSerializer");
            String news = championatNewsData.getNextNews();
            championatNewsSerializer.serializePostedKeys();
            message = new SendMessage().setChatId(parsedMessage.getChatId()).setText(news);
            updateContact(parsedMessage.getChatId(), parsedMessage.getContact(), PatternType.NEWS);
        } else if (coincidence(parsedMessage.getMessage(), anekdotPatterns)) {
            AnekdotParser anekdotParser = (AnekdotParser) appContext.getBean("anekdotParser");
            String anekdot = anekdotParser.getAnekdot();
            message = new SendMessage().setChatId(parsedMessage.getChatId()).setText(anekdot);
            updateContact(parsedMessage.getChatId(), parsedMessage.getContact(), PatternType.ANEKDOT);
        }

        if(message!=null) {
            sendChatMessage(message, parsedMessage);
        }
        else {
            try {
                FullMessage fullMessage = patternList.getMessage(update);
                updateContact(parsedMessage.getChatId(), parsedMessage.getContact(), fullMessage.getPatternType());
                if (fullMessage.getMessageType() == MessageType.NORMAL) {
                    sendChatMessage(fullMessage.getBotApiMethod(), parsedMessage);
                } else if (fullMessage.getMessageType() == MessageType.PHOTO) {
                    sendPhoto(fullMessage.getPhotoMethod());
                }

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendChatMessage(BotApiMethod<Message> message, ParsedMessage parsedMessage) {
        try {
            if (message != null && contactCounts.get(parsedMessage.getChatId()) != null && contactCounts.get(parsedMessage.getChatId()).isOverflowed()) {
                String name = contactCounts.get(parsedMessage.getChatId()).getName();
                String[] mess =
                        {
                                name + ", ты заебал",
                                name + ", с тобой не общаюсь",
                                "Отвали", "" +
                                "Я занят, непонятно, " + name + "?"
                        };
                Random rand = new Random();
                sendMessage(new SendMessage().setChatId(parsedMessage.getChatId()).setText(mess[rand.nextInt(mess.length)]));
            } else if (message != null) {
                this.sendApiMethod(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void updateContact(Long chatId, String name, PatternType type) {
        ContactCount contactCount = contactCounts.get(chatId);
        if (contactCount == null) {
            contactCount = ContactCount.emptyInstance();
        }
        contactCount.updateContact(name, type);
        contactCounts.put(chatId, contactCount);
    }

    private boolean coincidence(String text, String pattern) {
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
