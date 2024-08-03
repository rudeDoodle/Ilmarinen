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

    // This method takes in time in milliseconds and returns a formatted string in H:M:S
    public static String timePrettyPrint(long milliseconds) {
        if (milliseconds < 1000) {
            return "1 second";
        }

        long hours = milliseconds / 3600000;
        milliseconds %= 3600000;

        long minutes = milliseconds / 60000;
        milliseconds %= 60000;

        long seconds = milliseconds / 1000;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append(" hour");
            if (hours > 1) {
                sb.append("s");
            }
            sb.append(" ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" minute");
            if (minutes > 1) {
                sb.append("s");
            }
            sb.append(" ");
        }
        if (seconds > 0) {
            sb.append(seconds).append(" second");
            if (seconds > 1) {
                sb.append("s");
            }
            sb.append(" ");
        }

        return sb.toString().trim();
    }
}
