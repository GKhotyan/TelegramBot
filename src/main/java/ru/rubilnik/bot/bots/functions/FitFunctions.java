package ru.rubilnik.bot.bots.functions;

import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Created by Alexey on 23.07.2017.
 */
public class FitFunctions {
    public static BiFunction<String, String, Boolean> defaultFunction = (pattern, message) -> Stream.of(pattern.split(":")).anyMatch(e -> message.toLowerCase().contains(e));
    public static BiFunction<String, String, Boolean> startWithFunction = (pattern, message) -> Stream.of(pattern.split(":")).anyMatch(e -> message.toLowerCase().startsWith(e));
}
