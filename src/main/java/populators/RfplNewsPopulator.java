package populators;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RfplNewsPopulator {
  HashMap<String, String> replacementMap = new HashMap<String, String>() {{
    put("Манчини","Бышовец");
    put("Дзюба","Бесчастных");
    put("Дзюбы","Бесчастных");
    put("Кокорин","Ширко");
    put("Зенит","ФК Бомжи");
    put("Гончаренко","Газзаев");
    put("Каррера","Ярцев");
    put("Мутко","Басков");
    put("ЦСКА","СКА-Хабаровск");
    put("Черчесов","Романцев");
  }};

  public String populate(String text){
    for(Map.Entry<String, String> entry : replacementMap.entrySet()){
      text = text.replaceAll(entry.getKey(), entry.getValue());
    }
    return text;
  }
}
