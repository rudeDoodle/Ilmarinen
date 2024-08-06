package dev.mlml.command.argument;

public class BooleanArgument extends ArgumentBase<Boolean> {
    public BooleanArgument(String name, String description, boolean isRequired) {
        super(name, description, isRequired);
    }

    @Override
    public Boolean parse(String input) {
        return Boolean.parseBoolean(input);
    }

    public static class Builder extends ArgumentBase.Builder<BooleanArgument.Builder, Boolean, BooleanArgument> {
        public Builder(String name) {
            super(name);
        }

        @Override
        public BooleanArgument get() {
            return new BooleanArgument(name, description, isRequired);
        }
    }
}
