package dev.mlml.command.argument;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParsedArgumentList {
    public static final Logger logger = LoggerFactory.getLogger(ParsedArgumentList.class);

    @Getter
    private final List<ParsedArg> arguments = new ArrayList<>();

    public ParsedArgumentList() {

    }

    public ParsedArg add(ArgumentBase<?> argument, String input) {
        ParsedArg parsedArg = new ParsedArg(argument, argument.getValue(input));
        logger.debug("Parsed argument: " + parsedArg);
        arguments.add(parsedArg);
        return parsedArg;
    }

    public record ParsedArg(ArgumentBase<?> argument, Object value) {
        public boolean skip() {
            return Objects.isNull(value);
        }

        public boolean isRequired() {
            return argument.isRequired();
        }

        public String getName() {
            return argument.getName();
        }

        public String getDescription() {
            return argument.getDescription();
        }

        public Object getValue() {
            return value;
        }
    }
}
