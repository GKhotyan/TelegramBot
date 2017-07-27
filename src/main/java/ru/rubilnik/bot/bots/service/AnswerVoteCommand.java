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

import static ru.rubilnik.bot.service.EmojiService.HAND_NO_WHITE;
import static ru.rubilnik.bot.service.EmojiService.HAND_YES_WHITE;

/**
 * Created by Alexey on 27.07.2017.
 */
@Service
public class AnswerVoteCommand extends DefaultService implements BotCommand {

    private final VoteAnswerService voteAnswerService;
    private final VoteService voteService;

    @Autowired
    public AnswerVoteCommand(VoteAnswerService voteAnswerService, VoteService voteService) {
        this.voteAnswerService = voteAnswerService;
        this.voteService = voteService;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {

        String message = command.getParsedMessage().getMessage();
        String[] split = message.split("_");
        Long voteId = Long.valueOf(split[2]);
        Long userId = Long.valueOf(command.getParsedMessage().getContact().getId());
        boolean yes = split[3].equals(EmojiService.HAND_YES_WHITE.toString());
        Optional<Vote> vote = voteService.findOne(voteId);
        String error = "Нет такого опроса";
        try {
            if (vote.isPresent()) {
                VoteAnswer voteAnswer = new VoteAnswer(yes, userId, vote.get());
                voteAnswerService.save(voteAnswer);
                SendMessage sendMessage = createMessage("ok", command.getParsedMessage().getChatId());
                sendMessage.setReplyMarkup(getSettingsKeyboard());
                return new FullMessage(sendMessage, PatternType.VOTE_ANSWER, MessageType.NORMAL);
            }
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        SendMessage sendMessage = createMessage(error, command.getParsedMessage().getChatId());
        sendMessage.setReplyMarkup(getSettingsKeyboard());
        return new FullMessage(sendMessage, PatternType.VOTE_ANSWER, MessageType.NORMAL);

    }

    private static ReplyKeyboardMarkup getSettingsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.clear();
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}
