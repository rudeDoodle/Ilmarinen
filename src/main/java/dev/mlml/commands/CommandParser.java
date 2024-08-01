package dev.mlml.commands;

public class CommandParser {
    public static Arguments parse(String raw) {
        return new Arguments(raw);
    }
}
