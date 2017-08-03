package ru.rubilnik.bot.bots.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.rubilnik.bot.bots.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alexey on 25.07.2017.
 */
@Service
public class CommandsCommand extends DefaultService implements BotCommand {

    @Setter
    private  List<Pattern> patterns;

    @Override
    public FullMessage getMessage(MessageCommand command) {
        SendMessage sendMessage = createMessage("Чего умеет Рубибот:", command.getParsedMessage().getChatId());
        sendMessage.setReplyMarkup(createInlineMarkup(getMessageInternal()));
        return new FullMessage(sendMessage, PatternType.COMMANDS, MessageType.NORMAL);
    }

    private List<Command> getMessageInternal() {
        return patterns.stream().filter(p -> p.getType() != null).filter(p -> p.getType().isDescription()).map(p -> new Command(p.getPattern().replaceAll(":", ", ") + ": " + p.getDescription().toLowerCase(),p.getPattern().split(":")[0])).collect(Collectors.toList());
    }

    private InlineKeyboardMarkup createInlineMarkup(List<Command> commands) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        commands.forEach(c-> {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(c.getDesc()).setCallbackData(c.getCommand()));
            rowsInline.add(rowInline);
        });
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @AllArgsConstructor
    private class Command {
        @Getter
        String desc;
        @Getter
        String command;
    }
}
