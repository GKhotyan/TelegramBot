package ru.rubilnik.bot.bots.data;

import lombok.Getter;

/**
 * Created by Alexey on 20.07.2017.
 */
public enum PatternType {

    UNKNOWN(false, false), LIVE(true, true), AVDOTYA(true, true), NEWS(true, true), ANEKDOT(true, true), SWEAR(true, false), ABOUT_TEAM(false, true), REPLACE(true, true), PHOTO(true, true), COMMANDS(false, true);

    @Getter
    private boolean count;
    @Getter
    private boolean description;

    PatternType(boolean count, boolean description) {
        this.count = count;
        this.description = description;
    }
}
