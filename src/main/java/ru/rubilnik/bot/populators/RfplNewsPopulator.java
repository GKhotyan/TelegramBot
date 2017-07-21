package ru.rubilnik.bot.populators;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.rubilnik.bot.data.model.Replacement;
import ru.rubilnik.bot.sevice.ReplacementService;

@Component
public class RfplNewsPopulator {

    @Autowired
    private ReplacementService replacementService;

    private final HashMap<String, String> replacementMap = new HashMap<String, String>() {{
        put("Манчини", "Бышовец");
        put("Дзюб\\S", "Бесчастных");
        put("Кокорин\\S", "Ширко");
        put("«Зенит\\S", "«ФК Бомжи»");
        put("Гончаренко", "Газзаев");
        put("Каррера", "Ярцев");
        put("Мутко", "Басков");
        put("ЦСКА", "СКА-Хабаровск");
        put("Черчесов", "Романцев");
        put("КДК", "Шойгу");
        put("РФПЛ", "ЛФЛ");
        put("Кадыров", "Собянин");
    }};

    public String populate(String text) {

        String clean = text.replaceAll("[^а-яА-Я\\s]", "").replaceAll("\n", "");
        String[] split = clean.split(" ");

        Stream.of(split).filter(s-> !StringUtils.isEmpty(s)).forEach(s -> {
            replacementService.findByFrom(s.trim()).map(Replacement::getToWord).ifPresent(to -> replacementMap.put(s, to));
        });

        for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
            text = text.replaceAll(entry.getKey(), entry.getValue());
        }
        return text;
    }

    public static void main(String[] args) {
        RfplNewsPopulator rfplNewsPopulator = new RfplNewsPopulator();
        System.out.print(rfplNewsPopulator.populate("РФПЛ Черчесову Дзюбы"));
    }
}
