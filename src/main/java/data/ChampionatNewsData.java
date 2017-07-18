package data;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.springframework.stereotype.Component;

@Component
@Singleton
public class ChampionatNewsData {
  private Map<String, String> currentNewsMap = new LinkedHashMap<>();
  private Set<String> postedKeys = new HashSet<>();
  private Set<String> importantNewsKeys = new HashSet<>();

  public void removeFromCurrentNews(String key){
    currentNewsMap.remove(key);
  }

  public void removeFromPostedKeys(String key){
    postedKeys.remove(key);
  }

  public void addToCurrentNews(String key, String text){
    currentNewsMap.put(key, text);
  }

  public void addToPosted(String key){
    postedKeys.add(key);
  }

  public void addToImportant(String key){
    importantNewsKeys.add(key);
  }

  public boolean isCurrentNewsContains(String key){
    return currentNewsMap.containsKey(key);
  }

  public String getNextNews(){
    for(String key : currentNewsMap.keySet()){
      if(!postedKeys.contains(key)){
        postedKeys.add(key);
        return currentNewsMap.get(key);
      }
    }
    return "нет никаких новостей";
  }

  public String getNextImportantNews(){
    for(String key : currentNewsMap.keySet()){
      if(!postedKeys.contains(key) && importantNewsKeys.contains(key)){
        postedKeys.add(key);
        return currentNewsMap.get(key);
      }
    }
    return "нет интересных новостей";
  }

  public void keepOnlyKeys(Set<String> keys){
    currentNewsMap.entrySet().removeIf(e->!keys.contains(e.getKey()));
    postedKeys.removeIf(e->!keys.contains(e));
    importantNewsKeys.removeIf(e->!keys.contains(e));
  }
}
