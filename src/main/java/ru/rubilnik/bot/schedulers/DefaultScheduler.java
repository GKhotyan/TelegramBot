package ru.rubilnik.bot.schedulers;

import ru.rubilnik.bot.messages.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.rubilnik.bot.utils.TelegramSender;

@EnableScheduling
@Component
public class DefaultScheduler {

  @Autowired
  private Messenger avdotyaMessenger;

  @Autowired
  private
  TelegramSender telegramSender;

  @Scheduled(cron="${rubilnik.scheduler.cron}")
  public void schedule() {
    telegramSender.send(avdotyaMessenger.getMessage(null));
  }
}
