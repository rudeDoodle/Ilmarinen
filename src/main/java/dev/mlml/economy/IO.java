package dev.mlml.economy;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;

public class IO {
    private static final Logger logger = LoggerFactory.getLogger(IO.class);

    private static final String FILE_PATH = "economy.ilm";

    private static final String OBJECT_SEPARATOR = "\u001D";
    private static final String USER_FIELD_SEPARATOR = "\u001E";
    private static final String RECORD_SEPARATOR = "\u001F";

    /**
     * The structure of the file defined as follows
     * Note: Newlines are added here for readability, but they are not present in the actual file
     * {OBJECT_SEPARATOR}OBJECT_TYPE - The type of the object: global, user, guild
     * {RECORD_SEPARATOR}OBJECT_ID - The ID of the object
     * {USER_FIELD_SEPARATOR}VALUE_KEY - The key of the value
     * {RECORD_SEPARATOR}VALUE_VALUE - The value of the value
     * > Values will all be associated with the object defined by the OBJECT_ID,
     * until a new {OBJECT_SEPARATOR} is seen.
     */

    private enum ObjectType {
        GLOBAL,
        USER,
        GUILD;

        public static ObjectType fromString(String string) {
            for (ObjectType type : values()) {
                if (type.name().equalsIgnoreCase(string)) {
                    return type;
                }
            }

            return null;
        }
    }

    @SneakyThrows
    private static void setField(Object obj, Field field, String value) {
        field.setAccessible(true);
        try {
            if (field.getType() == int.class) {
                field.setInt(obj, Integer.parseInt(value));
            } else if (field.getType() == float.class) {
                field.setFloat(obj, Float.parseFloat(value));
            } else if (field.getType() == double.class) {
                field.setDouble(obj, Double.parseDouble(value));
            } else if (field.getType() == long.class) {
                field.setLong(obj, Long.parseLong(value));
            } else if (field.getType() == boolean.class) {
                field.setBoolean(obj, Boolean.parseBoolean(value));
            } else {
                field.set(obj, value);
            }
        } catch (IllegalAccessException e) {
            logger.error("Failed to set field: " + field.getName());
        }
    }

    private static String serializeGlobal(EconGlobal econGlobal) {
        StringBuilder sb = new StringBuilder();

        sb.append(OBJECT_SEPARATOR)
          .append(ObjectType.GLOBAL)
          .append(RECORD_SEPARATOR)
          .append("global");

        Field[] fields = EconGlobal.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Serialize.class)) {
                continue;
            }

            try {
                field.setAccessible(true);

                sb.append(USER_FIELD_SEPARATOR)
                  .append(field.getName())
                  .append(RECORD_SEPARATOR)
                  .append(field.get(econGlobal));
            } catch (IllegalAccessException e) {
                logger.error("Failed to serialize field: " + field.getName());
            }
        }

        return sb.toString();
    }

    private static EconGlobal deserializeGlobal(String string) {
        String[] parts = string.split(USER_FIELD_SEPARATOR);
        EconGlobal econGlobal = new EconGlobal();

        for (int i = 1; i < parts.length; i++) {
            String[] kv = parts[i].split(RECORD_SEPARATOR);
            String key = kv[0];
            String value = kv[1];

            try {
                Field field = EconGlobal.class.getDeclaredField(key);
                setField(econGlobal, field, value);
            } catch (NoSuchFieldException e) {
                logger.error("Failed to deserialize field: " + key);
            }
        }

        return econGlobal;
    }

    private static String serializeUser(EconUser econUser) {
        StringBuilder sb = new StringBuilder();

        sb.append(OBJECT_SEPARATOR)
          .append(ObjectType.USER)
          .append(RECORD_SEPARATOR)
          .append(econUser.getId());

        Field[] fields = EconUser.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Serialize.class)) {
                continue;
            }

            try {
                field.setAccessible(true);

                sb.append(USER_FIELD_SEPARATOR)
                  .append(field.getName())
                  .append(RECORD_SEPARATOR)
                  .append(field.get(econUser));
            } catch (IllegalAccessException e) {
                logger.error("Failed to serialize field: " + field.getName());
            }
        }

        return sb.toString();
    }

    private static EconUser deserializeUser(String string) {
        String[] parts = string.split(USER_FIELD_SEPARATOR);
        String[] kv = parts[0].split(RECORD_SEPARATOR);
        String id = kv[1];
        EconUser econUser = new EconUser(id);

        logger.debug("Deserialized user: " + econUser);

        for (int i = 1; i < parts.length; i++) {
            kv = parts[i].split(RECORD_SEPARATOR);
            String key = kv[0];
            String value = kv[1];

            try {
                Field field = EconUser.class.getDeclaredField(key);
                setField(econUser, field, value);
            } catch (NoSuchFieldException e) {
                logger.error("Failed to deserialize field: " + key);
            }
        }

        logger.debug("Deserialized user: " + econUser);

        return econUser;
    }

    private static String serializeGuild(EconGuild econGuild) {
        StringBuilder sb = new StringBuilder();

        sb.append(OBJECT_SEPARATOR)
          .append(ObjectType.GUILD)
          .append(RECORD_SEPARATOR)
          .append(econGuild.getId());

        Field[] fields = EconGuild.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Serialize.class)) {
                continue;
            }

            try {
                field.setAccessible(true);

                sb.append(USER_FIELD_SEPARATOR)
                  .append(field.getName())
                  .append(RECORD_SEPARATOR)
                  .append(field.get(econGuild));
            } catch (IllegalAccessException e) {
                logger.error("Failed to serialize field: " + field.getName());
            }
        }

        return sb.toString();
    }

    private static EconGuild deserializeGuild(String string) {
        String[] parts = string.split(USER_FIELD_SEPARATOR);
        String[] kv = parts[0].split(RECORD_SEPARATOR);
        String id = kv[1];
        EconGuild econGuild = new EconGuild(id);

        for (int i = 1; i < parts.length; i++) {
            kv = parts[i].split(RECORD_SEPARATOR);
            String key = kv[0];
            String value = kv[1];

            try {
                Field field = EconGuild.class.getDeclaredField(key);
                setField(econGuild, field, value);
            } catch (NoSuchFieldException e) {
                logger.error("Failed to deserialize field: " + key);
            }
        }

        return econGuild;
    }

    public static void save() {
        StringBuilder sb = new StringBuilder();

        sb.append(serializeGlobal(Economy.getEconGlobal()));

        for (Map.Entry<String, EconUser> user : Economy.getUsers().entrySet()) {
            sb.append(serializeUser(user.getValue()));
        }

        for (Map.Entry<String, EconGuild> guild : Economy.getGuilds().entrySet()) {
            sb.append(serializeGuild(guild.getValue()));
        }

        try {
            Files.write(new File(FILE_PATH).toPath(), sb.toString().getBytes());
        } catch (Exception e) {
            logger.error("Failed to save economy data");
        }
    }

    @SneakyThrows
    public static void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        StringBuilder cb = new StringBuilder();
        for (String line : Files.readAllLines(file.toPath())) {
            cb.append(line);
        }

        Economy.getUsers().clear();
        Economy.getGuilds().clear();

        String content = cb.toString();

        String[] objects = content.split(OBJECT_SEPARATOR);
        for (String object : objects) {
            if (object.isEmpty()) {
                continue;
            }
            logger.debug("Deserializing object: " + object);
            String[] parts = object.split(USER_FIELD_SEPARATOR);
            String[] kv = parts[0].split(RECORD_SEPARATOR);
            logger.debug("Deserializing object: " + kv[0] + " with ID: " + kv[1]);
            ObjectType type = ObjectType.fromString(kv[0]);
            if (type == null) {
                logger.warn("Unknown object type: " + kv[0]);
                continue;
            }

            String id = kv[1];

            logger.debug("Deserializing object: " + type + " with ID: " + id);

            switch (type) {
                case GLOBAL:
                    Economy.setEconGlobal(deserializeGlobal(object));
                    break;
                case USER:
                    EconUser econUser = deserializeUser(object);
                    Economy.putUser(id, econUser);
                    break;
                case GUILD:
                    EconGuild econGuild = deserializeGuild(object);
                    Economy.putGuild(id, econGuild);
                    break;
            }
        }
    }
}
