package ru.rubilnik.bot.utils.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Alexey on 19.07.2017.
 */
@Component
public class JsonClient implements WebClient {

    public Optional<JSONObject> httpGet(String url) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("content-type", "application/json");

            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(json);

            if (resultObject instanceof JSONArray) {
                JSONArray array = (JSONArray) resultObject;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ru/rubilnik/bot/data", array);
                return Optional.of(jsonObject);

            } else if (resultObject instanceof JSONObject) {
                return Optional.of((JSONObject) resultObject);
            }


        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

}
