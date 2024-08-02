package dev.mlml.command.argument;

import lombok.Getter;

@Getter
public class StringArgument extends ArgumentBase<String> {

    public StringArgument(String name, String description, boolean isRequired, boolean isVArgs) {
        super(name, description, isRequired, isVArgs);
    }

    @Override
    public String parse(String input) {
        return input;
    }

    public static class Builder extends ArgumentBase.Builder<StringArgument.Builder, String, StringArgument> {

        public Builder(String name) {
            super(name);
        }

        @Override
        public StringArgument get() {
            return new StringArgument(name, description, isRequired, isVArgs);
        }
    }
}
