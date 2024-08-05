package dev.mlml.command.argument;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ParsedArgumentList {
    private static final Logger logger = LoggerFactory.getLogger(ParsedArgumentList.class);

    @Getter
    private final List<ParsedArgument<?>> arguments = new ArrayList<>();
    private boolean isValid = true;

    public void invalidate() {
        this.isValid = false;
    }

    public <V> ParsedArgument<V> add(ArgumentBase<V> argument, String input) {
        V parsedValue = argument.parse(input);
        ParsedArgument<V> parsedArg = new ParsedArgument<>(argument, parsedValue);
        logger.debug("Parsed argument: {}", parsedArg);
        arguments.add(parsedArg);
        return parsedArg;
    }

    public <V> ParsedArgument<V> add(ArgumentBase<V> argument) {
        ParsedArgument<V> parsedArg = new ParsedArgument<>(argument, null);
        logger.debug("Filled argument: {}", parsedArg);
        arguments.add(parsedArg);
        return parsedArg;
    }


}
