package dev.mlml;

import dev.mlml.handlers.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class Ilmarinen {
    public static void main(String[] args) {
        String token = System.getenv("TOKEN");

        if (token == null) {
            System.err.println("Please specify a token as environment variable");
            System.exit(1);
        }

        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS
        );

        JDA jda = JDABuilder.createLight(token, intents)
                            .addEventListeners(new Listener())
                            .build();
    }
}