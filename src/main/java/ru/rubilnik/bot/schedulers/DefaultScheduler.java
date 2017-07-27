package ru.rubilnik.bot.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.rubilnik.bot.bots.service.TextScheduler;
import ru.rubilnik.bot.utils.TelegramSender;

@EnableScheduling
@Component
public class DefaultScheduler {

  private final TextScheduler avdotyaService;
  private final TelegramSender telegramSender;

  @Autowired
  public DefaultScheduler(TextScheduler avdotyaService, TelegramSender telegramSender) {
    this.avdotyaService = avdotyaService;
    this.telegramSender = telegramSender;
  }

  @Scheduled(cron="${rubilnik.scheduler.cron}")
  public void schedule() {
    telegramSender.send(avdotyaService.getMessageText());
  }
}
