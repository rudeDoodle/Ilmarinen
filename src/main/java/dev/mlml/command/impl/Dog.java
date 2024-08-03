package dev.mlml.command.impl;

import dev.mlml.Utils;
import dev.mlml.command.Command;
import dev.mlml.command.CommandInfo;
import dev.mlml.command.Context;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@CommandInfo(
        keywords = {"dog", "doggo"},
        name = "Dog",
        description = "Get a dog media",
        permissions = {Permission.MESSAGE_SEND},
        category = CommandInfo.Category.Fun,
        cooldown = 5
)
public class Dog extends Command {
    private static final Logger logger = LoggerFactory.getLogger(Dog.class);

    private final String DOG_API_URL = "https://random.dog/woof.json";

    @Override
    public void execute(Context ctx) {

        final DataObject response = Utils.sendGetRequest(DOG_API_URL);

        if (Objects.isNull(response)) {
            ctx.getMessage().reply("Could not fetch media").queue();
            return;
        }

        String mediaURl = response.get("url").toString();
        logger.info("Media URL is: " + mediaURl);
        TextChannel channel = (TextChannel) ctx.getChannel();

        try {
            URL url = new URL(mediaURl);
            String fileName = url.getFile();

            try (InputStream in = url.openStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                FileUpload fileUpload = FileUpload.fromData(baos.toByteArray(), fileName);

                channel.sendMessage("Here is a dog").addFiles(fileUpload).queue();
            }
        } catch (Exception e) {
            channel.sendMessage("Failed to download and send the file.").queue();
        }
    }
}
