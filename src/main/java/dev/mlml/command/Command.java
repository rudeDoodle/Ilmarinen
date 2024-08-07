package dev.mlml.command;

import dev.mlml.Config;
import dev.mlml.command.argument.ArgumentBase;
import dev.mlml.command.argument.StringArgument;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Getter
public abstract class Command {
    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private final String[] keywords;
    private final String name;
    private final String description;
    private final int cooldown;
    private final EnumSet<Permission> permissions;
    private final EnumSet<CommandInfo.ExtendedPermission> extendedPermissions;
    private final CommandInfo.Category category;
    private final List<ArgumentBase<?>> arguments = new ArrayList<>();

    public Command(ArgumentBase<?>... args) {
        CommandInfo ci = this.getClass().getAnnotation(CommandInfo.class);
        if (Objects.isNull(ci)) {
            throw new IllegalStateException(this.getClass().getName() + " did not have a CommandInfo annotation!");
        }

        keywords = ci.keywords();
        name = ci.name();
        description = ci.description();
        cooldown = ci.cooldown();
        permissions = EnumSet.noneOf(Permission.class);
        permissions.addAll(Arrays.asList(ci.permissions()));
        extendedPermissions = EnumSet.noneOf(CommandInfo.ExtendedPermission.class);
        extendedPermissions.addAll(Arrays.asList(ci.extendedPermissions()));
        category = ci.category();


        if (args.length == 0) {
            logger.debug("Created command {} with no arguments", name);
            return;
        }

        boolean seenVArgs = args[0].getClass() == StringArgument.class && ((StringArgument) args[0]).isVArgs();

        for (int i = 1; i < args.length; i++) {
            ArgumentBase<?> arg = args[i];
            if (seenVArgs) {
                logger.error("Variable arguments must be the last argument!");
                throw new IllegalStateException(name + " has an argument after a variable argument!");
            }
            seenVArgs = args[0].getClass() == StringArgument.class && ((StringArgument) args[0]).isVArgs();
        }

        arguments.addAll(List.of(args));

        logger.debug("Created command {} with {} arguments", name, args.length);
    }

    public String getUsage() {
        StringBuilder sb = new StringBuilder();

        for (ArgumentBase<?> arg : arguments) {
            sb.append(arg.isRequired() ? "<" : "[");
            sb.append(arg.getName());
            if (arg.getClass() == StringArgument.class && ((StringArgument) arg).isVArgs()) {
                sb.append("...");
            }
            sb.append(arg.isRequired() ? "> " : "] ");
            sb.append(" ");
        }

        return sb.toString().trim();
    }

    public String getArgDescription() {
        StringBuilder sb = new StringBuilder();

        for (ArgumentBase<?> arg : arguments) {
            sb.append(arg.getName());
            sb.append(": ");
            sb.append(arg.getDescription());
            sb.append("\n");
        }

        return sb.toString().trim();
    }

    public abstract void execute(Context ctx);

    public boolean canExecute(Member member, GuildChannel channel) {
        if (permissions.isEmpty()) {
            return true;
        }

        if (!extendedPermissions.isEmpty()) {
            if (extendedPermissions.contains(CommandInfo.ExtendedPermission.BOT_ADMIN)
                    && Arrays.stream(Config.getBotConfig().getAdmins())
                             .noneMatch(admin -> admin.equals(member.getId()))) {
                return false;
            }
        }

        return PermissionUtil.checkPermission(channel.getPermissionContainer(),
                                              member,
                                              permissions.toArray(new Permission[0])
        );
    }
}