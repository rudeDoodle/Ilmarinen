package dev.mlml.command.argument;

import dev.mlml.Ilmarinen;
import dev.mlml.Utils;
import net.dv8tion.jda.api.entities.User;

public class UserArgument extends ArgumentBase<User> {
    public UserArgument(String name, String description, boolean isRequired) {
        super(name, description, isRequired);
    }

    @Override
    public User parse(String input) {
        if (Utils.stringIsUserMention(input)) {
            String id = input.contains("!")
                        ? input.substring(3, input.length() - 1)
                        : input.substring(2, input.length() - 1);
            return Ilmarinen.getJda().retrieveUserById(id).complete();
        }
        if (Utils.stringIsSnowflake(input)) {
            return Ilmarinen.getJda().retrieveUserById(input).complete();
        }
        return null;
    }

    public static class Builder extends ArgumentBase.Builder<UserArgument.Builder, User, UserArgument> {
        public Builder(String name) {
            super(name);
        }

        @Override
        public UserArgument get() {
            return new UserArgument(name, description, isRequired);
        }
    }
}