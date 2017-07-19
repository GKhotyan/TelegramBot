package data.serialize;

import java.io.*;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import data.ChampionatNewsData;

@Component
public class ChampionatNewsSerializer {
  @Autowired
  ChampionatNewsData championatNewsData;

  private static final String filename =  "postedkeys.ser";

  public void serializePostedKeys() {
    try (ObjectOutputStream oos =
           new ObjectOutputStream(new FileOutputStream(filename))) {
      oos.writeObject(championatNewsData.getPostedKeys());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void deserializePostedKeys() {
    try(
    FileInputStream fis = new FileInputStream(filename);
    ObjectInputStream  ois = new ObjectInputStream (fis))
    {
      Set<String> postedKeys = (Set<String>) ois.readObject();
      championatNewsData.addPostedKeys(postedKeys);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }
}
