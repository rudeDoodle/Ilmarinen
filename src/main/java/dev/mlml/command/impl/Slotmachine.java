package dev.mlml.command.impl;

import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import dev.mlml.command.Replies;
import dev.mlml.command.argument.FloatArgument;
import dev.mlml.command.argument.ParsedArgument;
import dev.mlml.economy.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@CommandInfo(
        keywords = {"slotmachine", "sm"},
        name = "Slotmachine",
        description = "Play the slot machine",
        category = CommandInfo.Category.Economy,
        cooldown = 5
)
public class Slotmachine extends Command {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Slotmachine.class);

    private final static FloatArgument AMOUNT_ARG = new FloatArgument.Builder("amount")
            .description("The amount of money to bet")
            .require()
            .get();

    private final static List<Payout> payouts = List.of(
            new Payout(":lemon:", 8, 0.1),
            new Payout(":strawberry:", 6, 0.2),
            new Payout(":apple:", 4, 0.3),
            new Payout(":cherries:", 2, 0.4)
    );

    public Slotmachine() {
        super(AMOUNT_ARG);
    }

    @Override
    public void execute(Context ctx) {
        Optional<ParsedArgument<Float>> amountArg = ctx.getArgument(AMOUNT_ARG);

        EmbedBuilder resEmbed = Replies.base(ctx);
        resEmbed.setColor(0x00FF00);

        if (amountArg.isEmpty()) {
            for (Payout p : payouts) {
                resEmbed.addField(p.emoji().repeat(3), "x" + p.payoutMult(), true);
            }
            ctx.reply(resEmbed.build());
            return;
        }

        float amount = amountArg.get().getValue();

        if (amount <= 0) {
            ctx.fail("Invalid amount");
            return;
        }

        EconUser eu = Economy.getUser(ctx.getMember().getId());
        EconGuild eg = Economy.getGuild(ctx.getGuild().getId());
        GamblingInstance gi = new GamblingInstance(eu, eg);

        if (!eu.canAfford(amount)) {
            ctx.fail("You don't have enough money");
            return;
        }

        gi.play(amount);

        gi.play(amount);
        List<Payout> rolled = new ArrayList<>();
        resEmbed.setDescription("Rolling... :slot_machine: :slot_machine: :slot_machine:");
        MessageEmbed initialEmbed = resEmbed.build();
        ctx.getMessage().replyEmbeds(initialEmbed).queue(resMsg -> {
            while (rolled.size() < 3) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while sleeping", e);
                }
                Payout roll = roll();
                rolled.add(roll);
                resEmbed.setDescription(getRolledEmojis(rolled) + ":slot_machine:".repeat(3 - rolled.size()));
                resMsg.editMessageEmbeds(resEmbed.build()).queue();
            }

            if (rolled.get(0).equals(rolled.get(1)) && rolled.get(0).equals(rolled.get(2))) {
                float winnings = rolled.get(0).payoutMult() * amount;
                gi.win(winnings);
                resEmbed.setDescription(resEmbed.getDescriptionBuilder() + "\nYou won " + winnings + "! (Payout: " + rolled.get(
                        0).payoutMult() + "x" + amount + ")");
            } else {
                resEmbed.setDescription(resEmbed.getDescriptionBuilder() + "\nYou lost " + amount + "!");
            }

            resMsg.editMessageEmbeds(resEmbed.build()).queue();
            IO.save();
        });
    }

    private Payout roll() {
        Random rand = new Random();
        double randomValue = rand.nextDouble();
        for (Payout payout : payouts) {
            if (randomValue < payout.probability()) {
                return payout;
            }
            randomValue -= payout.probability();
        }
        return null;
    }

    private String getRolledEmojis(List<Payout> rolled) {
        StringBuilder sb = new StringBuilder();
        for (Payout p : rolled) {
            sb.append(p.emoji());
        }
        return sb.toString();
    }

    private record Payout(String emoji, int payoutMult, double probability) {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Payout payout = (Payout) obj;
            return emoji.equals(payout.emoji);
        }

        @Override
        public int hashCode() {
            return emoji.hashCode();
        }
    }
}
