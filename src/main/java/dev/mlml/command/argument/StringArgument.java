package dev.mlml.command.argument;

import lombok.Getter;

@Getter
public class StringArgument extends ArgumentBase<String> {
    final boolean isVArgs;

    public StringArgument(String name, String description, boolean isRequired, boolean isVArgs) {
        super(name, description, isRequired);

        this.isVArgs = isVArgs;
    }

    @Override
    public String parse(String input) {
        return input;
    }

    public static class Builder extends ArgumentBase.Builder<StringArgument.Builder, String, StringArgument> {
        boolean isVArgs = false;

        public Builder(String name) {
            super(name);
        }

        public Builder isVArgs() {
            this.isVArgs = true;
            return getThis();
        }

        @Override
        public StringArgument get() {
            return new StringArgument(name, description, isRequired, isVArgs);
        }
    }
}
