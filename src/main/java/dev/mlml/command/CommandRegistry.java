package dev.mlml.command;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CommandRegistry {
    private static final Logger logger = LoggerFactory.getLogger(CommandRegistry.class);

    private static final Set<Command> commands = new HashSet<>();

    public static void registerClass(Class<? extends Command> commandClass) {
        Command instance = null;
        try {
            instance = commandClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Failed to register command class: " + commandClass.getName());
            return;
        }

        commands.add(instance);
    }

    @Nullable
    public static Command getCommandClassByKeyword(String name) {
        return commands.stream()
                       .filter(command -> Arrays.asList(command.getKeywords()).contains(name))
                       .findFirst()
                       .orElse(null);
    }

    @SneakyThrows
    public static void executeCommand(Message message) {
        if (message.getAuthor().isBot()) {
            return;
        }

        Context ctx = new Context(message);

        if (!ctx.isValidCommand) {
            return;
        }

        Command command = getCommandClassByKeyword(ctx.command);
        if (Objects.isNull(command)) {
            message.reply("Command not found!").complete();
            return;
        }

        if (!command.canExecute(ctx.member, ctx.gChannel)) {
            message.reply("You don't have permission to execute this command!").complete();
            return;
        }

        if (ctx.parse(command)) {
            logger.debug("Failed to parse command: {}", command.getName());
            message.reply("Usage: " +  command.getKeywords()[0] + " " + command.getUsage()).complete();
            return;
        }

        logger.debug("Executing command: {}", command.getName());
        command.execute(ctx);
    }
}
