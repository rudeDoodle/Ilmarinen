package dev.mlml.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandRegistry {
    private static final Set<Class<? extends Command>> commands;

    public static void registerClass(Class<? extends Command> commandClass) {
        commands.add(commandClass);
    }

    public static void executeCommandByName(String name) {
        // Find the command by name and execute it
        Command command = findCommandByName(name);

        executeCommand(command);
    }

    public static Class<? extends Command> findCommandByName(String name) {
        // Find the command by name
        return commands.stream()
                .filter(command -> command.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static void executeCommand(Command command) {
        Command myInstanceForThisSpecificExecution = command.getClass().newInstance();
    }
}
