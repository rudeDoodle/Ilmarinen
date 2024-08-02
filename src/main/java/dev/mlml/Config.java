package dev.mlml;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final String configFile = "ilmarinen.xml";

    @Getter
    private static final HashMap<String, ServerConfig> servers = new HashMap<>();

    public static ServerConfig getServerConfig(String serverId) {
        if (!servers.containsKey(serverId)) {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.id = serverId;
            serverConfig.prefix = BotConfig.defaultPrefix;
            servers.put(serverId, serverConfig);
            logger.info("Server config for {} not found, creating new one", serverId);
        }

        return servers.get(serverId);
    }

    @Data
    public static class BotConfig {
        @Getter
        private static String token;
        @Getter
        private static String defaultPrefix;
    }

    @Data
    public static class ServerConfig {
        public String id;
        public String prefix;
    }

    public static boolean createConfigIfNotExist() {
        File fXmlFile = new File(configFile);
        if (!fXmlFile.exists()) {
            saveToFile();
            return true;
        }
        return false;
    }

    @SneakyThrows
    public static void saveToFile() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Config");
        doc.appendChild(rootElement);

        Element botConfigElement = doc.createElement("BotConfig");
        rootElement.appendChild(botConfigElement);

        for (Field field : BotConfig.class.getDeclaredFields()) {
            field.setAccessible(true);
            Element fieldElement = doc.createElement(field.getName());
            fieldElement.appendChild(doc.createTextNode(String.valueOf(field.get(null))));
            botConfigElement.appendChild(fieldElement);
        }

        Element serversElement = doc.createElement("Servers");
        rootElement.appendChild(serversElement);

        for (String serverId : servers.keySet()) {
            ServerConfig serverConfig = servers.get(serverId);
            Element serverElement = doc.createElement("Server");

            for (Field field : ServerConfig.class.getDeclaredFields()) {
                field.setAccessible(true);
                Element fieldElement = doc.createElement(field.getName());
                fieldElement.appendChild(doc.createTextNode(String.valueOf(field.get(serverConfig))));
                serverElement.appendChild(fieldElement);
            }

            serversElement.appendChild(serverElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(configFile));

        transformer.transform(source, result);

        logger.info("Config saved to {}", configFile);
    }

    @SneakyThrows
    public static void loadFromFile() {
        File fXmlFile = new File(configFile);
        if (!fXmlFile.exists()) {
            return;
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        Node botConfigNode = doc.getElementsByTagName("BotConfig").item(0);
        if (botConfigNode.getNodeType() == Node.ELEMENT_NODE) {
            Element botConfigElement = (Element) botConfigNode;
            for (Field field : BotConfig.class.getDeclaredFields()) {
                field.setAccessible(true);
                String value = botConfigElement.getElementsByTagName(field.getName()).item(0).getTextContent();
                field.set(null, value);
            }
        }

        NodeList serverNodes = doc.getElementsByTagName("Server");
        servers.clear();
        for (int i = 0; i < serverNodes.getLength(); i++) {
            Node serverNode = serverNodes.item(i);
            if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
                Element serverElement = (Element) serverNode;
                ServerConfig serverConfig = new ServerConfig();
                for (Field field : ServerConfig.class.getDeclaredFields()) {
                    field.setAccessible(true);
                    String value = serverElement.getElementsByTagName(field.getName()).item(0).getTextContent();
                    field.set(serverConfig, value);
                }
                servers.put(serverConfig.id, serverConfig);
            }
        }

        logger.info("Config loaded from {}", configFile);
    }
}
