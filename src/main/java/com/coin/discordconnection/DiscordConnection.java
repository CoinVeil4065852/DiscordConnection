package com.coin.discordconnection;

import com.coin.discordconnection.commands.DiscordCommandManager;
import com.coin.discordconnection.commands.MentionCommand;
import com.coin.discordconnection.commands.ReplyCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.*;


public class DiscordConnection extends JavaPlugin {
    public static DiscordConnection instance;
    public JDAUtils jdaUtils;
    public Config config;
    public DeathCount deathCount;

    @Override
    public void onEnable() {
        instance = this;
        jdaUtils = new JDAUtils();
        DiscordCommandManager discordCommandManager = new DiscordCommandManager();
        getCommand("discord").setExecutor(discordCommandManager);
        getCommand("discord").setTabCompleter(discordCommandManager.getTabCompleter());
        MentionCommand mentionCommand = new MentionCommand();
        getCommand("mention").setExecutor(mentionCommand);
        getCommand("mention").setTabCompleter(mentionCommand.getTabCompleter());
        getCommand("reply").setExecutor(new ReplyCommand());
        deathCount = DeathCount.getInstance();
        config = Config.getInstance();

        getServer().getPluginManager().registerEvents(new BukkitEvents(), this);
        String token = config.getToken();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[DiscordPlugin]:Plugin is enabled");
        if (token != null && !token.isEmpty()) {
            try {
                jdaUtils.createJDA(token);
            } catch (LoginException e) {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[DiscordPlugin]:cant connect to discord,token may be incorrect");
            }
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[DiscordPlugin]:token hasn't been set, use /discord token <token> to set token");
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[DiscordPlugin]:Plugin is disabled");
        jdaUtils.killJDA();
    }
}





