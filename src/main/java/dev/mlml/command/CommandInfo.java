package dev.mlml.command;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String[] keywords();
    String name();
    String description();
    Permission[] permissions();
}
