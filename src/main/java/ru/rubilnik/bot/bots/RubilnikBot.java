package ru.rubilnik.bot.bots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.rubilnik.bot.bots.data.NoMessageException;
import ru.rubilnik.bot.bots.data.ParsedMessage;
import ru.rubilnik.bot.bots.data.PatternList;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.data.ChampionatNewsData;
import ru.rubilnik.bot.data.model.Replacement;
import ru.rubilnik.bot.data.serialize.ChampionatNewsSerializer;
import ru.rubilnik.bot.parsers.AnekdotParser;
import ru.rubilnik.bot.parsers.YandexImageParser;
import ru.rubilnik.bot.sevice.ReplacementService;
import ru.rubilnik.bot.utils.ImagesUtil;
import ru.rubilnik.bot.utils.Porter;

import java.io.InputStream;
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
    private String replacePattern = "бот замена";
    private String swearPatterns = "бот";
    private String photoPatterns = "фото";

    private String getContact(Message message) {
        if (message != null && message.getFrom() != null) {
            return message.getFrom().getUserName() == null ? message.getFrom().getFirstName() : message.getFrom().getUserName();
        }
        return "default";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = null;
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
        else if (coincidence(parsedMessage.getMessage(), photoPatterns)) {
            if (parsedMessage.getMessage().toLowerCase().startsWith(photoPatterns)) {
                String substring = parsedMessage.getMessage().substring(photoPatterns.length()).trim();
                YandexImageParser yandexImageParser = (YandexImageParser) appContext.getBean("yandexImageParser");
                ImagesUtil imagesUtil = (ImagesUtil) appContext.getBean("imagesUtil");
                try {
                    String imageUrl = yandexImageParser.getRandomImageURL(substring);
                    if (imageUrl != null) {
                        InputStream is = imagesUtil.getImageInputStream(imageUrl);
                        SendPhoto photoMessage = new SendPhoto().setChatId(parsedMessage.getChatId()).setNewPhoto("image", is);
                        sendPhoto(photoMessage);
                    } else {
                        sendMessage(new SendMessage().setChatId(parsedMessage.getChatId()).setText("нет таких фото"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            PatternList.FullMessage fullMessage = patternList.getMessage(update);
            message = new SendMessage().setChatId(parsedMessage.getChatId()).setText(fullMessage.getMessage());
            updateContact(parsedMessage.getChatId(), parsedMessage.getContact(), fullMessage.getType());

        } catch (NoMessageException e) {
            //do nothing
        }

        if (message == null) {
            if (coincidence(parsedMessage.getMessage(), replacePattern)) {
                String resp = "Непонятно!";
                if (parsedMessage.getMessage().toLowerCase().startsWith(replacePattern)) {
                    String substring = parsedMessage.getMessage().substring(replacePattern.length()).trim();
                    String[] split = substring.split(" ");
                    if (split.length == 2) {
                        if (split[0].length() > 3) {
                            ReplacementService service = (ReplacementService) appContext.getBean("replacementServiceImpl");
                            try {
                                service.save(new Replacement(split[0], split[1]));
                                resp = "Замена произведена";
                            } catch (Throwable th) {
                                resp = "Проблемы при сохранении";
                            }
                        } else {
                            resp = "Короткое какое-то слово. Отказать.";
                        }
                    }
                }
                message = new SendMessage().setChatId(parsedMessage.getChatId()).setText(resp);
                updateContact(parsedMessage.getChatId(), parsedMessage.getContact(), PatternType.REPLACE);
            } else if (coincidence(parsedMessage.getMessage(), swearPatterns)) {
                message = new SendMessage().setChatId(parsedMessage.getChatId()).setText(Porter.transform(parsedMessage.getMessage()));
                updateContact(parsedMessage.getChatId(), parsedMessage.getContact(), PatternType.SWEAR);
            }
        }
        try {
            if (message != null && contactCounts.get(parsedMessage.getChatId()) != null && contactCounts.get(parsedMessage.getChatId()).isOverflowed()) {
                String name = contactCounts.get(parsedMessage.getChatId()).getName();
                String[] mess =
                        {
                                name + ", ты заебал",
                                name + ", с тобой не общаюсь",
                                "Отвали", "" +
                                "Я занят, непонятно, " + name + "?",
                                "Ладно, " + name + ":\n" + message.getText()
                        };
                Random rand = new Random();
                sendMessage(new SendMessage().setChatId(parsedMessage.getChatId()).setText(mess[rand.nextInt(mess.length)]));
            } else if (message != null) {
                message.enableMarkdown(true);
                sendMessage(message);
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
