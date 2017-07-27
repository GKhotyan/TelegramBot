package ru.rubilnik.bot.bots.data;

import lombok.Getter;
import ru.rubilnik.bot.bots.functions.FitFunctions;
import ru.rubilnik.bot.bots.service.BotCommand;

import java.util.function.BiFunction;

/**
 * Created by Alexey on 22.07.2017.
 */
public class Pattern {
    @Getter
    private String pattern;
    @Getter
    private PatternType type;
    @Getter
    private BotCommand service;
    @Getter
    private int priority;
    @Getter
    private String description;
    @Getter
    private BiFunction<String, String, Boolean> fitFunction;

    public Pattern(String pattern, PatternType type, BotCommand service, int priority, String description, BiFunction<String, String, Boolean> fitFunction) {
        this.pattern = pattern;
        this.type = type;
        this.service = service;
        this.priority = priority;
        this.description = description;
        this.fitFunction = fitFunction;
    }

    public Pattern(String pattern, PatternType type, BotCommand service, int priority, String description) {
        this(pattern, type, service, priority, description, FitFunctions.defaultFunction);
    }

    public boolean isFit(String text) {
        return fitFunction.apply(pattern, text);
    }

}
