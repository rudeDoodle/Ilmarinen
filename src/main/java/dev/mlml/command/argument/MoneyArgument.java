package dev.mlml.command.argument;

import lombok.Getter;

@Getter
public class MoneyArgument extends ArgumentBase<Float> {
    public MoneyArgument(String name, String description, boolean isRequired) {
        super(name, description, isRequired);
    }

    @Override
    public Float parse(String input) {
        if (input.equalsIgnoreCase("all")) {
            return Float.MAX_VALUE;
        }
        return Float.parseFloat(input);
    }

    public static class Builder extends ArgumentBase.Builder<MoneyArgument.Builder, Float, MoneyArgument> {
        public Builder(String name) {
            super(name);
        }

        @Override
        public MoneyArgument get() {
            return new MoneyArgument(name, description, isRequired);
        }
    }
}
