package ru.rubilnik.bot.bots.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rubilnik.bot.bots.data.PatternType;
import ru.rubilnik.bot.bots.functions.FitFunctions;
import ru.rubilnik.bot.messages.Messenger;

/**
 * Created by Alexey on 22.07.2017.
 */
@AllArgsConstructor
public class Pattern {
    @Getter
    private String pattern;
    @Getter
    private PatternType type;
    @Getter
    private Messenger messenger;
    @Getter
    private int priority;

    public boolean isFit(String text) {
        return FitFunctions.defaultFunction.apply(pattern, text);
    }

}
