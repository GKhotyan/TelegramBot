package ru.rubilnik.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;
import ru.rubilnik.bot.bots.RubilnikBot;
import ru.rubilnik.bot.parsers.ChampionatParser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.yml"),
        @PropertySource("classpath:private.yml")
})
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {"ru.rubilnik.bot.bots", "ru.rubilnik.bot"})
@EnableJpaRepositories("ru.rubilnik.bot.data.repository")
public class Application {

    private List<BotSession> sessions = new ArrayList<>();
    private final RubilnikBot rubilnikBot;
    private final ChampionatParser championatParser;

    static {
       ApiContextInitializer.init();
    }

    @Autowired
    public Application(RubilnikBot rubilnikBot, ChampionatParser championatParser) {
        this.rubilnikBot = rubilnikBot;
        this.championatParser = championatParser;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
    }

    @PostConstruct
    public void start() {
        championatParser.parseNews();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            sessions.add(botsApi.registerBot(rubilnikBot));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        sessions.forEach(session -> {
            if (session != null) {
                session.stop();
            }
        });
    }
}
