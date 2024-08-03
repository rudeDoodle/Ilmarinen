package dev.mlml;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.mlml.command.Context;
import net.dv8tion.jda.api.utils.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static boolean stringIsSnowflake(String input) {
        return input.matches("^\\d{17,19}$");
    }

    public static boolean stringIsUserMention(String input) {
        return input.matches("^<@!?\\d{17,19}>$");
    }

    public static boolean stringIsChannelMention(String input) {
        return input.matches("^<#\\d{17,19}>$");
    }

    public static JsonObject sendGetRequest(String urlString) {
        JsonObject result = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
                result = jsonElement.getAsJsonObject();
            } else {
                logger.debug("Failed to send GET request");
            }

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return result;
    }
}
