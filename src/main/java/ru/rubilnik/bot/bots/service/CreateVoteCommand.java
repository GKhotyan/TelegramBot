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
import ru.rubilnik.bot.service.VoteService;

import java.util.ArrayList;
import java.util.List;

import static ru.rubilnik.bot.service.EmojiService.HAND_NO_WHITE;
import static ru.rubilnik.bot.service.EmojiService.HAND_YES_WHITE;

/**
 * Created by Alexey on 27.07.2017.
 */
@Service
public class CreateVoteCommand extends DefaultService implements BotCommand {

    private final VoteService voteService;

    @Autowired
    public CreateVoteCommand(VoteService voteService) {
        this.voteService = voteService;
    }

    @Override
    public FullMessage getMessage(MessageCommand command) {
        String message = command.getParsedMessage().getMessage();
        String substring = "Создан опрос: " + message.substring(command.getPattern().length()).trim() + "\n";
        Vote vote = new Vote(command.getParsedMessage().getChatId(), substring);
        voteService.save(vote);
        substring += "ID: " + vote.getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(command.getParsedMessage().getChatId()).setText(substring);
        sendMessage.setReplyMarkup(getSettingsKeyboard(vote.getId()));
        return new FullMessage(sendMessage, PatternType.VOTE, MessageType.NORMAL);
    }

    private static ReplyKeyboardMarkup getSettingsKeyboard(Long voteId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("бот_голос_" + voteId + "_" + HAND_YES_WHITE.toString());
        keyboardFirstRow.add("бот_голос_" + voteId + "_" + HAND_NO_WHITE.toString());
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }


}
