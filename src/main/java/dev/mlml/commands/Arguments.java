package dev.mlml.commands;

public class Arguments {
    protected final String prefix;
    protected final String[] parts;
    protected final String command;
    protected final String[] args;

    public Arguments(String prefix, String... parts) {
        this.prefix = prefix;
        this.parts = parts;
        this.command = parts[0];
        this.args = new String[parts.length - 1];
        System.arraycopy(parts, 1, this.args, 0, parts.length - 1);
    }
}
