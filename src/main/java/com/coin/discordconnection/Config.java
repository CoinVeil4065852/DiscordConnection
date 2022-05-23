package com.coin.discordconnection;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Config {
    private static final File CONFIG = new File("./plugins/discordPlugin.config");
    private String chatChannelId;
    private String advancementChannelId;
    private String consoleChannelId;

    private String deathChannelId;
    private String token;
    private String activityText = "server is opening";

    private static Config instance;

    public String getDeathChannelId() {
        return deathChannelId;
    }

    public void setDeathChannelId(String deathChannelId) {
        this.deathChannelId = deathChannelId;
        save();
    }
    public String getAdvancementChannelId() {
        return advancementChannelId;
    }

    public void setAdvancementChannelId(String advancementChannelId) {
        this.advancementChannelId = advancementChannelId;
        save();
    }

    public String getConsoleChannelId() {
        return consoleChannelId;
    }

    public void setConsoleChannelId(String consoleChannelId) {
        this.consoleChannelId = consoleChannelId;
        save();
    }



    public Config() {
    }

    public static Config getInstance() {

        if (instance == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                instance = mapper.readValue(CONFIG, Config.class);
            } catch (IOException e) {
                instance = new Config();
                instance.save();
            }
        }
        return instance;
    }

    public String getChatChannelId() {
        return chatChannelId;
    }

    public void setChatChannelId(String chatChannelId) {
        this.chatChannelId = chatChannelId;
        save();
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        save();
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
        save();
    }

    private void save() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(CONFIG, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
