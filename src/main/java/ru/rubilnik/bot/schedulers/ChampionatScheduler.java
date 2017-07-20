package ru.rubilnik.bot.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.rubilnik.bot.parsers.ChampionatParser;

@EnableScheduling
@Component
public class ChampionatScheduler {
  @Autowired
  ChampionatParser championatParser;

  @Scheduled(cron="${championat.news.cron}")
  public void schedule() {
    championatParser.parseNews();
  }
}
