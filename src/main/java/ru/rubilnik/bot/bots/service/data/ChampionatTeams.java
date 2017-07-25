package ru.rubilnik.bot.bots.service.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

/**
 * Created by Alexey on 20.07.2017.
 */
public class ChampionatTeams {
    private static final HashMap<String, TeamInfo> MAP = new HashMap<>();

    private static final String PREMIER_ID = "_russiapl/2200";
    private static final String SECOND_CENTER = "_russia2d/2394/";

    static {
        MAP.put("ЦСКА".toLowerCase(), new TeamInfo("ЦСКА", PREMIER_ID, 62852));
        MAP.put("Динамо".toLowerCase(), new TeamInfo("Динамо", PREMIER_ID, 62858));
        MAP.put("Спартак".toLowerCase(), new TeamInfo("Спартак", PREMIER_ID, 62856));
        MAP.put("Зенит".toLowerCase(), new TeamInfo("Зенит", PREMIER_ID, 62850));

        MAP.put("Краснодар".toLowerCase(), new TeamInfo("Краснодар", PREMIER_ID, 62872));
        MAP.put("Ахмат".toLowerCase(), new TeamInfo("Ахмат", PREMIER_ID, 62866));
        MAP.put("Анжи".toLowerCase(), new TeamInfo("Анжи", PREMIER_ID, 62868));
        MAP.put("Локомотив".toLowerCase(), new TeamInfo("Локомотив", PREMIER_ID, 62854));

        MAP.put("СКА-Хабаровск".toLowerCase(), new TeamInfo("СКА-Хабаровск", PREMIER_ID, 62878));
        MAP.put("Арсенал".toLowerCase(), new TeamInfo("Арсенал", PREMIER_ID, 62880));
        MAP.put("Рубин".toLowerCase(), new TeamInfo("Рубин", PREMIER_ID, 62862));
        MAP.put("Тосно".toLowerCase(), new TeamInfo("Тосно", PREMIER_ID, 62876));

        MAP.put("Ростов".toLowerCase(), new TeamInfo("Ростов", PREMIER_ID, 62864));
        MAP.put("Урал".toLowerCase(), new TeamInfo("Урал", PREMIER_ID, 62870));
        MAP.put("Амкар".toLowerCase(), new TeamInfo("Амкар", PREMIER_ID, 62860));
        MAP.put("Уфа".toLowerCase(), new TeamInfo("Уфа", PREMIER_ID, 62874));

        MAP.put("Арарат".toLowerCase(), new TeamInfo("Арарат", SECOND_CENTER, 67332));

    }

    public static TeamInfo getTeam(String team) {
        return MAP.get(team.trim().toLowerCase());
    }

    @AllArgsConstructor
    public static class TeamInfo {
        @Getter
        String name;
        @Getter
        String division;
        @Getter
        int teamId;
    }
}
