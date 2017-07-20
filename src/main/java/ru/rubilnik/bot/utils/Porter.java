package ru.rubilnik.bot.utils;

/**
 * Created by Alexey on 19.07.2017.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
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

    private static String getWord(String sentence) {
        sentence = sentence.replace(",", "");
        sentence = sentence.replace("\"", "");
        sentence = sentence.replace("\'", "");
        sentence = sentence.trim();
        String[] split = sentence.split(" ");
        for(String s: split) {
            if(s.length()>3) return s;
        }
        return sentence;
    }

    public static String transform(String sentence) {
        String word = getWord(sentence);
        if(word.length()>3) {
            word = cutBeforeFirstVowel(word);
            while (word.length() > 5) {
                word = cutBeforeFirstVowel(word.substring(1));
            }
            ;
            if (replacementMap.containsKey(word.charAt(0))) {
                word = replacementMap.get(word.charAt(0)) + word.substring(1);
            }
            return "Ху" + word.toLowerCase().toLowerCase(new Locale("ru"));
        }
        else {
            return "Хуе" + word.toLowerCase().toLowerCase(new Locale("ru"));
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
        String st = "Кто, научил бота ругаться";
        String red = transform(st);
        System.out.println(red);

    }

}
