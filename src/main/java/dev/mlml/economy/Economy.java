package dev.mlml.economy;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Economy {
    @Getter
    @Setter
    private static EconGlobal econGlobal = new EconGlobal();
    @Getter
    private static final Map<String, EconGuild> guilds = new HashMap<>();
    @Getter
    private static final Map<String, EconUser> users = new HashMap<>();

    public static EconGuild getGuild(String id) {
        if (!guilds.containsKey(id)) {
            guilds.put(id, new EconGuild(id));
        }

        return guilds.get(id);
    }

    public static EconUser getUser(String id) {
        if (!users.containsKey(id)) {
            users.put(id, new EconUser(id));
        }

        // TODO: Disable this code at some point
        users.get(id).addAccolade(EconUser.ACCOLADE_BETA_TESTER);

        return users.get(id);
    }

    public static void putUser(String id, EconUser user) {
        users.put(id, user);
    }

    public static void putGuild(String id, EconGuild guild) {
        guilds.put(id, guild);
    }
}
