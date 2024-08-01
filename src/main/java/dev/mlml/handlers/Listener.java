package dev.mlml.handlers;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.printf("[%s] %#s: %s\n",
                          event.getChannel(),
                          event.getAuthor(),
                          event.getMessage().getContentDisplay());
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        System.out.printf("[%s] Message deleted\n", event.getChannel());
    }
}
