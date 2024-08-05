package dev.mlml.command.impl;

import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import dev.mlml.command.argument.FloatArgument;
import dev.mlml.command.argument.StringArgument;
import dev.mlml.economy.EconGuild;
import dev.mlml.economy.EconUser;
import dev.mlml.economy.Economy;

@CommandInfo(
        keywords = {"coinflip", "cf"},
        name = "Coinflip",
        description = "Flip a coin",
        category = CommandInfo.Category.Economy
)
public class Coinflip extends Command {
    public Coinflip() {
        super(
                new StringArgument.Builder("side")
                        .description("The side of the coin to bet on")
                        .require()
                        .get(),
                new FloatArgument.Builder("amount")
                        .description("The amount of money to bet")
                        .require()
                        .get()
        );
    }

    private static String invert(String side) {
        return side.equals("heads") ? "tails" : "heads";
    }

    @Override
    public void execute(Context ctx) {
        float amount = (float) ctx.getArgument("amount").getValue();
        String side = (String) ctx.getArgument("side").getValue();

        side = side.toLowerCase();

        if (!side.equals("heads") && !side.equals("tails")) {
            ctx.getMessage().reply("Invalid side!").queue();
            return;
        }

        EconGuild eg = Economy.getGuild(ctx.getGuild().getId());
        EconUser eu = Economy.getUser(ctx.getMember().getId());

        if (!eu.canAfford(amount)) {
            ctx.getMessage().reply("You can't afford that!").queue();
            return;
        }

        eu.play(amount);
        eg.play(amount);

        float random = (float) Math.random();
        StringBuilder sb = new StringBuilder();
        sb.append("The coin landed on ");
        if (random < 0.5) {
            sb.append(side);
            sb.append("! You won ");
            sb.append(amount * 2);
            eu.win(amount * 2);
            eg.win(amount * 2);
        } else {
            sb.append(invert(side));
            sb.append("! You lost!");
        }

        ctx.getMessage().reply(sb.toString()).queue();
    }
}
