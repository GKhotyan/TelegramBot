package ru.rubilnik.bot.service;

/**
 * Created by Alexey on 27.07.2017.
 */
public enum EmojiService {

    HAND_YES_WHITE(0x1F44D, 0x1F3FB),
    HAND_NO_WHITE(0x1F44E, 0x1F3FB),
    POO(0x1F4A9, null);

    Integer firstChar;
    Integer secondChar;

    EmojiService(Integer firstChar, Integer secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    public static String fromCodePoints(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }


    @Override
    public String toString() {
        if (this.firstChar != null && this.secondChar != null) {
            return fromCodePoints(this.firstChar, this.secondChar);
        } else if (this.secondChar != null) {
            return fromCodePoints(this.secondChar);
        } else {
            return fromCodePoints(this.firstChar);
        }
    }
}
