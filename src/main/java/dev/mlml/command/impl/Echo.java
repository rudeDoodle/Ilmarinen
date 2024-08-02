package dev.mlml.command.impl;

import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import dev.mlml.command.argument.ChannelArgument;
import dev.mlml.command.argument.ParsedArgumentList;
import dev.mlml.command.argument.StringArgument;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.EnumSet;

@CommandInfo(
        keywords = {"echo", "say"},
        name = "Echo",
        description = "Echoes the input",
        permissions = {Permission.MESSAGE_SEND}
)
public class Echo extends Command {
    public Echo() {
        super(
                new ChannelArgument.Builder("channel")
                        .description("The channel to echo in")
                        .get(),
                new StringArgument.Builder("text")
                        .description("The text to echo")
                        .isVArgs()
                        .require()
                        .get()
        );
    }

    @Override
    public void execute(Context ctx) {
        ParsedArgumentList.ParsedArg channelArg = ctx.getArgument("channel");
        String message = (String) ctx.getArgument("text").getValue();

        if (!channelArg.skip()) {
            Channel channel = (Channel) channelArg.getValue();
            if (channel.getType() != ChannelType.TEXT) {
                ctx.getMessage().reply("Invalid channel type").queue();
                return;
            }
            if (!ctx.getMember().hasPermission((TextChannel) channel, Permission.MESSAGE_SEND)) {
                ctx.getMessage().reply("You don't have permission to send messages in that channel").queue();
                return;
            }
            ((TextChannel) channel).sendMessage(message)
                                   .setAllowedMentions(EnumSet.noneOf(Message.MentionType.class))
                                   .queue();
        } else {
            ctx.getMessage().reply(message).setAllowedMentions(EnumSet.noneOf(Message.MentionType.class)).queue();
        }
    }
}
