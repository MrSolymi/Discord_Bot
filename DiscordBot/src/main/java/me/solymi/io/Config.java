package me.solymi.io;

import com.cedarsoftware.util.io.JsonWriter;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Config {
    public static final File CONFIG_FILE = new File("config.json");
    JSONObject config;

    public Config() {
        if (!CONFIG_FILE.exists()) {
            try {
                createConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readConfig();
    }

    private void createConfig() throws IOException {
        JSONObject config = new JSONObject();
        config.put("token", "");
        //config.put("sd_url", "http://localhost:7860/");
        //config.put("tenor_api_key", "API-KEY-HERE");
        //JSONObject channelIDS = new JSONObject();
        //JSONObject tmpChanelGuildConfig = new JSONObject();
        //tmpChanelGuildConfig.put("vc", "PUT-VOICE-CHANNEL-ID-HERE");
        //tmpChanelGuildConfig.put("general-messages-tc", "PUT-TEXT-CHANNEL-ID-HERE");
        //channelIDS.put("GUILD-ID-HERE", tmpChanelGuildConfig);
        //config.put("channel-ids", channelIDS);
        //JSONArray admins = new JSONArray();
        //admins.put("ADMIN-ID-HERE");
        //config.put("admins", admins);
        FileUtils.writeStringToFile(CONFIG_FILE, config.toString(4), StandardCharsets.UTF_8);
    }

    private void readConfig() {
        try {
            config = new JSONObject(FileUtils.readFileToString(CONFIG_FILE, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeConfig() throws IOException {
        FileUtils.writeStringToFile(CONFIG_FILE, JsonWriter.formatJson(config.toString()), StandardCharsets.UTF_8);
    }

    public String getToken() {
        readConfig();
        return config.getString("token");
    }
}
