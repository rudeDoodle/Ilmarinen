package dev.mlml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final String configFile = "ilmarinen.json";

    @Getter
    private static final HashMap<String, ServerConfig> servers = new HashMap<>();

    @Getter
    private static BotConfig botConfig = new BotConfig();

    public static ServerConfig getServerConfig(String serverId) {
        if (!servers.containsKey(serverId)) {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.id = serverId;
            serverConfig.prefix = botConfig.defaultPrefix;
            servers.put(serverId, serverConfig);
            logger.info("Server config for {} not found, creating new one", serverId);
        }

        return servers.get(serverId);
    }

    @Data
    public static class BotConfig {
        private String token;
        private String defaultPrefix = "!";
        private String[] admins = new String[0];
    }

    @Data
    public static class ServerConfig {
        public String id;
        public String prefix;
    }

    public static boolean createConfigIfNotExist() {
        File fJsonFile = new File(configFile);
        if (!fJsonFile.exists()) {
            saveToFile();
            return true;
        }
        return false;
    }

    @SneakyThrows
    public static void saveToFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        ConfigData configData = new ConfigData();
        configData.setBotConfig(botConfig);
        configData.setServers(new HashMap<>(servers));

        try {
            mapper.writeValue(new File(configFile), configData);
            logger.info("Config saved to {}", configFile);
        } catch (IOException e) {
            logger.error("Failed to save config to {}", configFile, e);
        }
    }

    @SneakyThrows
    public static void loadFromFile() {
        File fJsonFile = new File(configFile);
        if (!fJsonFile.exists()) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ConfigData configData = mapper.readValue(fJsonFile, ConfigData.class);

            botConfig = configData.getBotConfig();
            servers.clear();
            servers.putAll(configData.getServers());

            logger.info("Config loaded from {}", configFile);
        } catch (IOException e) {
            logger.error("Failed to load config from {}", configFile, e);
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ConfigData {
        @JsonProperty("botConfig")
        private BotConfig botConfig;

        @JsonProperty("servers")
        private HashMap<String, ServerConfig> servers;
    }
}