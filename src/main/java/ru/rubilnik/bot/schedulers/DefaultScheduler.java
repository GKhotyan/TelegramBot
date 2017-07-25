package ru.rubilnik.bot.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.rubilnik.bot.bots.service.BotService;
import ru.rubilnik.bot.utils.TelegramSender;

@EnableScheduling
@Component
public class DefaultScheduler {

  @Autowired
  private BotService avdotyaService;

  @Autowired
  private
  TelegramSender telegramSender;

  @Scheduled(cron="${rubilnik.scheduler.cron}")
  public void schedule() {
    SendMessage message = (SendMessage) avdotyaService.getMessage(null).getBotApiMethod();
    telegramSender.send(message.getText());
  }
}
