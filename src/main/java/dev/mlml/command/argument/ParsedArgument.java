package dev.mlml.command.argument;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ParsedArgument<V> {
    private final ArgumentBase<V> argument;
    private final V value;

    public ParsedArgument(ArgumentBase<V> argument, V value) {
        this.argument = argument;
        this.value = value;
    }

    public boolean skip() {
        return Objects.isNull(value);
    }
}