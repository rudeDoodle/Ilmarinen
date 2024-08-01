package dev.mlml.commands.impl;

import dev.mlml.commands.Arguments;
import dev.mlml.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class Echo extends Command {
    public Echo() {
        name = "echo";
        this.description = "Echoes the input";
        this.aliases = new String[]{"repeat"};
        this.permissions = EnumSet.of(Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Arguments args) {

    }
}
