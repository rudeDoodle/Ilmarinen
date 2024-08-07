package dev.mlml.command;

import dev.mlml.Ilmarinen;
import net.dv8tion.jda.api.EmbedBuilder;

public class Replies {
    private static final int ERROR_COLOR = 0xFF5555;
    private static final int SUCCESS_COLOR = 0x55FF55;
    private static final int INFO_COLOR = 0x5555FF;

    private static final String ERROR_TITLE = ":x: Error";
    private static final String SUCCESS_TITLE = ":white_check_mark: Success";
    private static final String INFO_TITLE = ":information_source: Info";

    public static EmbedBuilder base(Context ctx) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setFooter("Ilmarinen", Ilmarinen.getJda().getSelfUser().getAvatarUrl());
        eb.setAuthor(ctx.getAuthor().getEffectiveName(), null, ctx.getAuthor().getAvatarUrl());
        return eb;
    }

    public static EmbedBuilder fail(Context ctx, String message) {
        EmbedBuilder eb = base(ctx);
        eb.setColor(ERROR_COLOR);
        eb.setTitle(ERROR_TITLE);
        eb.setDescription(message);
        return eb;
    }

    public static EmbedBuilder success(Context ctx, String message) {
        EmbedBuilder eb = base(ctx);
        eb.setColor(SUCCESS_COLOR);
        eb.setTitle(SUCCESS_TITLE);
        eb.setDescription(message);
        return eb;
    }

    public static EmbedBuilder info(Context ctx, String message) {
        EmbedBuilder eb = base(ctx);
        eb.setColor(INFO_COLOR);
        eb.setTitle(INFO_TITLE);
        eb.setDescription(message);
        return eb;
    }
}
