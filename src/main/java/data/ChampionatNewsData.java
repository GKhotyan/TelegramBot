package data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.springframework.stereotype.Component;

@Component
@Singleton
public class ChampionatNewsData implements Serializable {
  private Map<String, String> currentNewsMap = new LinkedHashMap<>();
  private Set<String> postedKeys = new HashSet<>();
  private Set<String> importantNewsKeys = new HashSet<>();

  public int getNewsSize(){
    return currentNewsMap.size();
  }

  public void clearCurrentNews() {
    currentNewsMap.clear();
  }

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

  public void clearOld(){
    postedKeys.removeIf(e->!currentNewsMap.containsKey(e));
    importantNewsKeys.removeIf(e->!currentNewsMap.containsKey(e));
  }
}
