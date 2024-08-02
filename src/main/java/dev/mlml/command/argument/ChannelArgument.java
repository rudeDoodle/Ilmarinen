package dev.mlml.command.argument;


import dev.mlml.Ilmarinen;
import dev.mlml.Utils;
import net.dv8tion.jda.api.entities.channel.Channel;

public class ChannelArgument extends ArgumentBase<Channel> {
    public ChannelArgument(String name, String description, boolean isRequired, Channel defaultValue) {
        super(name, description, isRequired, defaultValue);
    }

    @Override
    public Channel parse(String input) {
        if (Utils.stringIsChannelMention(input)) {
            return Ilmarinen.getJda().getTextChannelById(input.substring(2, input.length() - 1));
        }
        if (Utils.stringIsSnowflake(input)) {
            return Ilmarinen.getJda().getTextChannelById(input);
        }
        return null;
    }

    public static class Builder extends ArgumentBase.Builder<ChannelArgument.Builder, Channel, ChannelArgument> {
        public Builder(String name) {
            super(name);
        }

        @Override
        public ChannelArgument get() {
            return new ChannelArgument(name, description, isRequired, defaultValue);
        }
    }
}