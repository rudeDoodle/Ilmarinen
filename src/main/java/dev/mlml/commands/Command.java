package dev.mlml.commands;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.EnumSet;

public abstract class Command {
    @Getter
    private final String[] keywords;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final EnumSet<Permission> permissions;

    protected final Command subcommand = null;

    public Command(String keyword) {
        this(new String[]{keyword});
    }

    public Command(String[] keywords) {
        this(keywords, "none", "none", EnumSet.noneOf(Permission.class));
    }

    public Command(String keyword, String name) {
        this(new String[]{keyword}, name);
    }

    public Command(String[] keywords, String name) {
        this(keywords, name, "none", EnumSet.noneOf(Permission.class));
    }

    public Command(String keyword, String name, String description) {
        this(new String[]{keyword}, name, description);
    }

    public Command(String[] keywords, String name, String description) {
        this(keywords, name, description, EnumSet.noneOf(Permission.class));
    }

    public Command(String keyword, String name, String description, EnumSet<Permission> permissions) {
        this(new String[]{keyword}, name, description, permissions);
    }

    public Command(String[] keywords, String name, String description, EnumSet<Permission> permissions) {
        this.keywords = keywords;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public abstract void execute(Arguments args);

    public boolean canExecute(Member member, GuildChannel channel) {
        return PermissionUtil.checkPermission(channel.getPermissionContainer(),
                                              member,
                                              permissions.toArray(new Permission[0])
        );
    }

    public boolean canExecute(Member member) {
        return PermissionUtil.checkPermission(member,
                                              permissions.toArray(new Permission[0])
        );
    }
}