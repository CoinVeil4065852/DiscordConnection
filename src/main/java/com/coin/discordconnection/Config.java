package com.coin.discordconnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static final File CONFIG = new File("./plugins/discordPlugin.config");
    private String channelId;
    private String token;
    private String activityText ="server is opening";
    private String deathCountText ="Deaths";

    private static Config instance;

    public Config(String channelId, String token, String activityText) {
        this.channelId = channelId;
        this.token = token;
        this.activityText = activityText;
    }
    public Config(){
    }

    public static Config getInstance() {
        ObjectMapper mapper = new ObjectMapper();
        if(instance==null){
            try {
                instance=mapper.readValue(CONFIG,Config.class);
            } catch (IOException e) {
                instance=new Config();
            }
        }
        return instance;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    private void saveConfig(){
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(CONFIG,this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
