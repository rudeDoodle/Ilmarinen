package dev.mlml.command.impl;

import dev.mlml.Config;
import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.CommandRegistry;
import dev.mlml.command.Context;
import dev.mlml.command.argument.StringArgument;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@CommandInfo(
        keywords = {"help"},
        name = "Help",
        description = "Get help",
        permissions = {Permission.MESSAGE_SEND},
        category = CommandInfo.Category.Util
)
public class Help extends Command {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    public Help() {
        super(new StringArgument.Builder("command")
                      .description("Command to get details on")
                      .get()
        );
    }

    @Override
    public void execute(Context ctx) {
        String commandName = (String) ctx.getArgument("command").getValue();

        Command command = CommandRegistry.getCommandByKeyword(commandName);

        if (Objects.isNull(commandName)) {
            StringBuilder sb = new StringBuilder();

            sb.append("Here's a list of all the commands:\n");
            sb.append(String.format("You can send `%shelp [command name]` to get info on a specific command!\n\n",
                                    ctx.getPrefix()
            ));

            for (int i = 0; i < CommandInfo.Category.values().length; i++) {
                boolean isCategoryPrinted = false;
                CommandInfo.Category category = CommandInfo.Category.values()[i];

                if (CommandRegistry.getCommands().isEmpty()) {
                    ctx.getMessage().reply("There are no commands").queue();
                }
                Command[] commandObject = CommandRegistry.getCommands().toArray(new Command[0]);
                for (int j = 0; j < CommandRegistry.getCommands().size(); j++) {
                    if (commandObject[j].getCategory().equals(category)) {
                        if (!isCategoryPrinted) {
                            sb.append(category).append(":\n");
                            isCategoryPrinted = true;
                        }
                        sb.append(commandObject[j].getKeywords()[0]).append(", ");
                    }
                }
                sb.deleteCharAt(sb.length() - 2);
                sb.append("\n");
            }

            sb.toString().trim();
            ctx.getMessage().reply(sb).queue();
            return;
        }

        if (Objects.isNull(command)) {
            ctx.getMessage().reply("Unknown command: " + commandName).queue();
            return;
        }

        commandName = commandName.toLowerCase();

        StringBuilder sb = new StringBuilder();

        sb.append(command.getName() + ": " + command.getDescription() + "\n");
        sb.append("Usage: " + ctx.getPrefix() + commandName + " " + command.getUsage());
        sb.append("\n\n");
        if (!command.getArguments().isEmpty()) {
            sb.append("Arguments:\n" + command.getArgDescription());
        }

        sb.toString().trim();

        ctx.getMessage().reply(sb).queue();
    }
}
