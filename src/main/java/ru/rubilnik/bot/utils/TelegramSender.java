package ru.rubilnik.bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@Component
public class TelegramSender {

  @Value("${bot.tocken}")
  private String bot_tocken;

  @Value("${channel.id}")
  private Long channel_id;

  public void send(String message) {
    try {
      TelegramBot bot = TelegramBotAdapter.build(bot_tocken);
      bot.execute(new SendMessage(channel_id, message).parseMode(ParseMode.HTML));
    } catch (Exception e){
      e.printStackTrace();
    }
  }

}
