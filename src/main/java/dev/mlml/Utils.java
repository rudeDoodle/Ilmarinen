package dev.mlml;

import net.dv8tion.jda.api.utils.data.DataObject;
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

    public static DataObject sendGetRequest(String urlString) {
        DataObject result = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = DataObject.fromJson(in);
                in.close();
            } else {
                logger.debug("Failed to send GET request");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return result;
    }

    public static DataObject sendPostRequest(String urlString, String body) {
        DataObject result = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            connection.getOutputStream().write(body.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = DataObject.fromJson(in);
                in.close();
            } else {
                logger.debug("Failed to send POST request");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return result;
    }
}
