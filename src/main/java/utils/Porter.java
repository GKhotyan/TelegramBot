package utils;

/**
 * Created by Alexey on 19.07.2017.
 */

import com.sun.org.apache.xml.internal.security.signature.SignatureProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Porter {

    private static final Set<Character> VOWELS = new HashSet<Character>() {{
        add('а');
        add('е');
        add('ё');
        add('и');
        add('о');
        add('у');
        add('э');
        add('ю');
        add('я');
    }};

    private static HashMap<Character, Character> replacementMap = new HashMap<Character, Character>() {{
        put('о','ё');
        put('а','я');
        put('у','ю');
    }};

    public static String transform(String word) {
        word = word.trim();
        if(word.contains(" ")) {
            word = word.substring(0, word.indexOf(' '));
        }
        if(word.length()>3) {
            word = cutBeforeFirstVowel(word);
            while (word.length() > 5) {
                word = cutBeforeFirstVowel(word.substring(1));
            }
            ;
            if (replacementMap.containsKey(word.charAt(0))) {
                word = replacementMap.get(word.charAt(0)) + word.substring(1);
            }
            return "ху" + word;
        }
        else {
            return "хуй" + word;
        }
    }

    private static String cutBeforeFirstVowel(String word) {
        char[] chars = word.toCharArray();
        int i = 0;
        while (!VOWELS.contains(chars[i])) {
            i++;
        }
        return word.substring(i);
    }

    public static void main(String[] args) {
        String st = "бот молчишь";
        String mess = st.replace("бот", "");
        String red = transform(mess);
        System.out.println(red);

    }

}
