package ru.rubilnik.bot.bots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.data.model.Replacement;
import ru.rubilnik.bot.service.ReplacementService;

/**
 * Created by Alexey on 25.07.2017.
 */
@Service
public class ReplaceCommandService extends DefaultService implements BotCommand {

    private final ReplacementService service;

    @Autowired
    public ReplaceCommandService(ReplacementService service) {
        this.service = service;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String resp = "Непонятно!";
        String substring = command.getParsedMessage().getMessage().substring(command.getPattern().length()).trim();
        String[] split = substring.split(" ");
        if (split.length == 2) {
            if (split[0].length() > 3) {
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
        return new FullMessage(createMessage(resp, command.getParsedMessage().getChatId()), PatternType.REPLACE, MessageType.NORMAL);
    }
}
