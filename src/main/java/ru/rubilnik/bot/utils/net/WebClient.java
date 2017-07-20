package ru.rubilnik.bot.utils.net;

import org.json.simple.JSONObject;

import java.util.Optional;

/**
 * Created by Alexey on 19.07.2017.
 */
public interface WebClient {
    Optional<JSONObject> httpGet(String url);
}
