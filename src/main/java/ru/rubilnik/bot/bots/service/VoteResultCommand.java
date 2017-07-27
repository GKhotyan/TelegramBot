package ru.rubilnik.bot.bots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rubilnik.bot.bots.data.FullMessage;
import ru.rubilnik.bot.bots.data.MessageCommand;
import ru.rubilnik.bot.bots.data.MessageType;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.data.model.Vote;
import ru.rubilnik.bot.data.model.VoteAnswer;
import ru.rubilnik.bot.service.EmojiService;
import ru.rubilnik.bot.service.VoteAnswerService;
import ru.rubilnik.bot.service.VoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Alexey on 27.07.2017.
 */
@Service
public class VoteResultCommand extends DefaultService implements BotCommand {

    private final VoteService voteService;

    @Autowired
    public VoteResultCommand(VoteService voteService) {
        this.voteService = voteService;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String result;
        String message = command.getParsedMessage().getMessage();
        String trim = message.substring(command.getPattern().length()).trim();
        try {
            final Long voteId = Long.valueOf(trim);
            result = voteService.getResult(voteId);
        }
        catch(NumberFormatException ex) {
            result = "неверный id опроса";
        }
        SendMessage sendMessage = createMessage(result, command.getParsedMessage().getChatId());
        return new FullMessage(sendMessage, PatternType.VOTE, MessageType.NORMAL);

    }

}
