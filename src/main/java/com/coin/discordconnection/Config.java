package com.coin.discordconnection;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Config {
    private static final File CONFIG = new File("./plugins/discordPlugin.config");
    private String chatChannelId;
    private String achievementChannelId;
    private String consoleChannelId;


    private String deathMessageChannelId;
    private String token;
    private String activityText = "server is opening";
    private String deathCountText = "Deaths";

    private static Config instance;

    public String getDeathMessageChannelId() {
        return deathMessageChannelId;
    }

    public void setDeathMessageChannelId(String deathMessageChannelId) {
        this.deathMessageChannelId = deathMessageChannelId;
        saveConfig();
    }
    public String getAchievementChannelId() {
        return achievementChannelId;
    }

    public void setAchievementChannelId(String achievementChannelId) {
        this.achievementChannelId = achievementChannelId;
        saveConfig();
    }

    public String getConsoleChannelId() {
        return consoleChannelId;
    }

    public void setConsoleChannelId(String consoleChannelId) {
        this.consoleChannelId = consoleChannelId;
        saveConfig();
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
            }
        }
        return instance;
    }

    public String getChatChannelId() {
        return chatChannelId;
    }

    public void setChatChannelId(String chatChannelId) {
        this.chatChannelId = chatChannelId;
        saveConfig();
    }

    public String getDeathCountText() {
        return deathCountText;
    }

    public void setDeathCountText(String deathCountText) {
        this.deathCountText = deathCountText;
        saveConfig();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        saveConfig();
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
        saveConfig();
    }

    private void saveConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(CONFIG, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
