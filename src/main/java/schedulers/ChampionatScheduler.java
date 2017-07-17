package schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import parsers.ChampionatParser;

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
