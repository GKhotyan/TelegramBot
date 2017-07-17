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
@ComponentScan(basePackages = {"common", "utils", "bots", "parsers", "schedulers", "data", "populators"})
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class);
  }
}
