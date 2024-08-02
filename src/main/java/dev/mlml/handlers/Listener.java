package dev.mlml.handlers;

import dev.mlml.command.CommandRegistry;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        logger.info("[{}] {}: {}", event.getChannel(), event.getAuthor(), event.getMessage().getContentRaw());
        if (!event.getMessage().getAttachments().isEmpty()) {
            logger.info("Attachments: {}", event.getMessage().getAttachments());
        }
        CommandRegistry.executeCommand(event.getMessage());
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        System.out.printf("[%s] Message deleted\n", event.getChannel());
    }
}
