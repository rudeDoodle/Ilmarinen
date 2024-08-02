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
    public Help(Context ctx) {
        super(new StringArgument.Builder("Command name")
                      .description("The text to echo")
                      .require()
                      .get()
        );
    }

    @Override
    public void execute(Context args) {
        args.getMessage().reply("Helptext placeholder").queue();
    }
}
