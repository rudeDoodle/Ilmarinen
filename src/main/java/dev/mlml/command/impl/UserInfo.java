package dev.mlml.command.impl;

import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import dev.mlml.command.argument.ParsedArgument;
import dev.mlml.command.argument.UserArgument;
import dev.mlml.economy.EconUser;
import dev.mlml.economy.Economy;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@CommandInfo(
        keywords = {"userinfo", "ui"},
        name = "User Info",
        description = "See your user info",
        permissions = {Permission.MESSAGE_SEND},
        category = CommandInfo.Category.Economy
)
public class UserInfo extends Command {
    private static final UserArgument USER_ARG = new UserArgument.Builder("user")
            .description("The user to get info on")
            .get();

    public UserInfo() {
        super(USER_ARG);
    }

    @Override
    public void execute(Context ctx) {
        User user = ctx.getArgument(USER_ARG).map(ParsedArgument::getValue).orElse(ctx.getAuthor());

        EconUser econUser = Economy.getUser(user.getId());

        ctx.succeed(String.format("%s has %.2f money", user.getEffectiveName(), econUser.getMoney()));
    }
}
