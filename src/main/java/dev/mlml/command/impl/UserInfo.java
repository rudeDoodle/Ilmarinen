package dev.mlml.command.impl;

import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import dev.mlml.command.argument.UserArgument;
import dev.mlml.economy.EconUser;
import dev.mlml.economy.Economy;
import dev.mlml.economy.IO;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@CommandInfo(
        keywords = {"userinfo", "ui"},
        name = "User Info",
        description = "See your user info",
        permissions = {Permission.MESSAGE_SEND}
)
public class UserInfo extends Command {
    public UserInfo() {
        super(new UserArgument.Builder("user")
                      .description("The user to get info on")
                      .get()
        );
    }

    @Override
    public void execute(Context ctx) {
        User userArg = (User) ctx.getArgument("user").getValue();

        EconUser econUser;

        if (userArg == null) {
            econUser = Economy.getUser(ctx.getMember().getId());
        } else {
            econUser = Economy.getUser(userArg.getId());
        }

        ctx.getMessage().reply(String.format("You have %.2f money", econUser.getMoney())).queue();
        IO.save();
    }
}
