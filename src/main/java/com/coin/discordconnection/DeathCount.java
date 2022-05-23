package com.coin.discordconnection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathCount {
    private static final File DEATH_COUNT_FILE = new File("./plugins/deathCount.json");
    private static DeathCount instance;
    private Map<String, Integer> data ;

    public DeathCount() {
        data = new HashMap<>();
    }

    public static DeathCount getInstance() {

        if (instance == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                instance = mapper.readValue(DEATH_COUNT_FILE, DeathCount.class);
            } catch (IOException e) {
                instance = new DeathCount();
                instance.save();
            }
        }
        return instance;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }

    @JsonIgnore
    public int getDeathCount(Player player){
        return data.get(player.getUniqueId().toString());
    }

    public void addDeath(Player player){
        data.put(player.getUniqueId().toString(),data.getOrDefault(player.getUniqueId().toString(),0)+1);
        save();
    }

    public void setDeathCount(Player player,int count){
        data.put(player.getUniqueId().toString(),count);
        save();
    }


    private void save() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(DEATH_COUNT_FILE, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
