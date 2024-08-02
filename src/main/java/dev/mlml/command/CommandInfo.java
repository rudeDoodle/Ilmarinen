package dev.mlml.command;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String[] keywords();
    String name();
    String description() default "no description";
    Permission[] permissions() default {Permission.MESSAGE_SEND};
    int cooldown() default 0;
    Category category() default Category.Miscellaneous;

    enum Category{
        Util, Fun, Economy, Moderation, Miscellaneous;
    }
}
