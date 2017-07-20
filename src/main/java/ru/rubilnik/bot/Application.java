package ru.rubilnik.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySources({
  @PropertySource("classpath:application.yml"),
  @PropertySource("classpath:private.yml")
})
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {"ru.rubilnik.bot.common", "ru.rubilnik.bot"})
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class);
  }
}
