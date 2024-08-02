package dev.mlml.command.argument;

public class FloatArgument extends ArgumentBase<Float> {
    public FloatArgument(String name, String description, boolean isRequired) {
        super(name, description, isRequired);
    }

    @Override
    public Float parse(String input) {
        return Float.parseFloat(input);
    }

    public static class Builder extends ArgumentBase.Builder<FloatArgument.Builder, String, FloatArgument> {
        public Builder(String name) {
            super(name);
        }

        @Override
        public FloatArgument get() {
            return new FloatArgument(name, description, isRequired);
        }
    }
}
