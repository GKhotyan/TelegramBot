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

  private static final String no_news = "нет никаких новостей";

  public int getPostedKeysSize(){
    return postedKeys.size();
  }

  public void clearCurrentNews() {
    currentNewsMap.clear();
  }

  public void addToCurrentNews(String key, String text){
    currentNewsMap.put(key, text);
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
    return no_news;
  }

  public void clearOld(){
    postedKeys.removeIf(e->!currentNewsMap.containsKey(e));
  }

  public Set<String> getPostedKeys(){
    Set<String> postedKeysCopy = new HashSet<>();
    postedKeysCopy.addAll(this.postedKeys);
    return postedKeysCopy;
  }

  public void addPostedKeys(Set<String> postedKeys){
    this.postedKeys.addAll(postedKeys);
  }

  public void addPostedKey(String postedKey){
    this.postedKeys.add(postedKey);
  }

  public boolean isPostedKey(String key){
    return this.postedKeys.contains(key);
  }
}
