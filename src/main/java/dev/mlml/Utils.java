package dev.mlml;

public class Utils {
    public static boolean stringIsSnowflake(String input) {
        return input.matches("^\\d{17,19}$");
    }

    public static boolean stringIsUserMention(String input) {
        return input.matches("^<@!?\\d{17,19}>$");
    }

    public static boolean stringIsChannelMention(String input) {
        return input.matches("^<#\\d{17,19}>$");
    }
}
