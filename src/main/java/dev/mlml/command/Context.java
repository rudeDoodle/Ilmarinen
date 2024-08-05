package dev.mlml.command;

import dev.mlml.Config;
import dev.mlml.command.argument.ArgumentBase;
import dev.mlml.command.argument.ParsedArgument;
import dev.mlml.command.argument.ParsedArgumentList;
import dev.mlml.command.argument.StringArgument;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public class Context {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    protected final Message message;
    protected final Member member;
    protected final User author;
    protected final GuildChannel gChannel;
    protected final Channel channel;
    protected final Guild guild;

    protected final String prefix;
    protected final String[] parts;
    protected final String command;
    protected final String[] args;

    @Setter
    private Message reply = null;

    public Message reply(String message) {
        reply = this.message.reply(message).complete();
        return reply;
    }

    public Message reply(MessageEmbed message) {
        reply = this.message.replyEmbeds(message).complete();
        return reply;
    }

    public Message fail(String message) {
        reply = this.message.replyEmbeds(Replies.fail(this, message).build()).complete();
        return reply;
    }

    public Message succeed(String message) {
        reply = this.message.replyEmbeds(Replies.success(this, message).build()).complete();
        return reply;
    }

    public Message inform(String message) {
        reply = this.message.replyEmbeds(Replies.info(this, message).build()).complete();
        return reply;
    }

    protected final boolean isValidCommand;

    protected final ParsedArgumentList parsedArguments;

    public Context(Message message) {
        prefix = Config.getServerConfig(message.getGuildId()).prefix;

        this.isValidCommand = message.getContentRaw().startsWith(prefix);

        this.message = message;
        this.member = message.getMember();
        this.author = message.getAuthor();
        this.gChannel = message.getGuildChannel();
        this.channel = message.getChannel();
        this.guild = message.getGuild();

        if (!isValidCommand) {
            this.parts = new String[0];
            this.command = "";
            this.args = new String[0];
            this.parsedArguments = new ParsedArgumentList();
            return;
        }

        this.parsedArguments = new ParsedArgumentList();

        String[] parts = split(message);

        this.parts = parts;
        this.command = parts[0].substring(prefix.length());
        this.args = new String[parts.length - 1];
        System.arraycopy(parts, 1, this.args, 0, parts.length - 1);
    }

    public static String[] split(Message message) {
        return message.getContentRaw().split(" +");
    }

    public boolean parse(Command command) {
        List<? extends ArgumentBase<?>> commandArguments = command.getArguments();
        String[] args = getArgs();

        if (args == null || args.length == 0) {
            for (ArgumentBase<?> arg : commandArguments) {
                if (arg.isRequired()) {
                    logger.debug("Required argument not found: {}", arg.getName());
                    parsedArguments.invalidate();
                    return true;
                }
                parsedArguments.add(arg);
            }
            return false;
        }

        int offset = 0;
        for (int i = 0; i < commandArguments.size(); i++) {
            ArgumentBase<?> arg = commandArguments.get(i);
            logger.debug("Parsing argument: {}, offset: {}", arg.getName(), offset);

            if (arg instanceof StringArgument && ((StringArgument) arg).isVArgs()) {
                List<String> remainingArgs = List.of(args).subList(i + offset, args.length);
                if (remainingArgs.isEmpty()) {
                    return true;
                }
                parsedArguments.add(arg, String.join(" ", remainingArgs));
                break;
            }

            if (i >= args.length + offset) {
                if (arg.isRequired()) {
                    logger.debug("Required argument not found: {}, args.length: {}, offset: {}",
                                 arg.getName(), args.length, offset
                    );
                    parsedArguments.invalidate();
                    return true;
                }
                for (int j = i; j < commandArguments.size(); j++) {
                    parsedArguments.add(commandArguments.get(j));
                }
                return false;
            }

            ParsedArgument<?> next = parsedArguments.add(arg, args[i + offset]);
            if (next.skip()) {
                offset--;
                logger.debug("Seeking back one argument, offset now: {}", offset);
                continue;
            }
            logger.debug("Parsed argument: {}, value: {}", arg.getName(), next.getValue());
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public <V> Optional<ParsedArgument<V>> getArgument(ArgumentBase<V> argument) {
        return parsedArguments.getArguments().stream()
                              .filter(parsedArg -> parsedArg.getArgument().equals(argument))
                              .filter(arg -> Objects.nonNull(arg.getValue()))
                              .map(arg -> (ParsedArgument<V>) arg)
                              .findFirst();
    }

}
