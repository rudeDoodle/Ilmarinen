package dev.mlml.command.impl;

import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import dev.mlml.command.argument.StringArgument;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        keywords = {"help"},
        name = "Help",
        description = "Get help",
        permissions = {Permission.MESSAGE_SEND}
)
public class Help extends Command {
    public Help() {
        super(new StringArgument.Builder("command")
                      .description("Command to get details on")
                      .get()
        );
    }

    @Override
    public void execute(Context ctx) {
        String commandName = (String) ctx.getArgument("command").getValue();

        System.out.println(commandName);
    }
}
