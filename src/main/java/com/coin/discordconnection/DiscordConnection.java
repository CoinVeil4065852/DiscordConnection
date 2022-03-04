package com.coin.discordconnection;

import com.coin.discordconnection.commands.DiscordCommandManager;
import com.coin.discordconnection.commands.ReplyCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class DiscordConnection extends JavaPlugin {
    public static JavaPlugin plugin;
    public static JDA jda;
    public static final File DEATH_COUNT = new File("./plugins/discordPluginDeathCount.txt");
    public static Map<String, Integer> deathCount = new HashMap();

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new BukkitEvents(), this);
        DiscordCommandManager discordCommandManager =new DiscordCommandManager();
        getCommand("discord").setExecutor(discordCommandManager);
        getCommand("discord").setTabCompleter(discordCommandManager.getTabCompleter());
        getCommand("reply").setExecutor(new ReplyCommand());
        try {
            BufferedReader deathCountBR = new BufferedReader(new FileReader(DEATH_COUNT));
            String line = deathCountBR.readLine();
            while (line != null) {
                String[] args = line.split(" ");
                deathCount.put(args[0], Integer.parseInt(args[1]));
                line = deathCountBR.readLine();
            }
        } catch (IOException ignored) {}

        String token = Config.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            startService();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[DiscordPlugin]:token is not set, use /token [token] to set token");
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[DiscordPlugin]:Plugin is disabled");
        jda.removeEventListener();
        jda.shutdownNow();
    }

    public static void startService() {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[DiscordPlugin]:Plugin is enabled");
        try {
            startJDA(Config.getInstance().getToken());
        } catch (LoginException e) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[DiscordPlugin]:cant connect to discord,token may be incorrect");
        }
    }

    public static void startJDA(String token) throws LoginException {
        jda = JDABuilder.createDefault(token).build();
        setActivity();
        jda.addEventListener(new JDAListener());
    }

    public static void setActivity() {
        if (jda == null) return;
        String activityText = Config.getInstance().getActivityText();
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, "[" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "]" + activityText));
    }

    public static void saveDeathCount() {
        try {
            FileWriter fw = new FileWriter(DEATH_COUNT, false);
            StringBuilder s = new StringBuilder();
            for (String name : deathCount.keySet()) {
                s.append(name).append(" ").append(deathCount.get(name)).append("\n");
            }
            fw.write(s.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class JDAListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!event.getChannel().getId().equals(Config.getInstance().getChannelId())) return;
        if (event.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        if (event.getMessage().getContentRaw().equalsIgnoreCase("-ip")) {
            String ip = Helper.getPublicIp();
            if (ip != null) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.GREEN).setTitle(ip).build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.RED).setTitle("ERROR").addField("can't get server's ip", "", true).build()).queue();
            }
            return;
        }
        if (!event.getMessage().getContentRaw().isEmpty()) {
            Message repliedMessage = event.getMessage().getReferencedMessage();

            if (repliedMessage != null) {
                Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + " ➡ Replied " + Helper.messageToText(repliedMessage,ChatColor.GRAY));
            }
            TextComponent textComponent = new net.md_5.bungee.api.chat.TextComponent();
            textComponent.setText(Helper.messageToText(event.getMessage()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Click to Reply")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/reply "+event.getMessage().getId()+" "));
            Bukkit.getConsoleSender().sendMessage(Helper.messageToText(event.getMessage()));
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));

        }
    }




}

class BukkitEvents implements Listener {
    @EventHandler
    public static void onPlayerChat(AsyncPlayerChatEvent event) {
        if(DiscordConnection.jda==null)return;
        String channelID = Config.getInstance().getChannelId();
        if (channelID == null || channelID.isEmpty()) return;
        TextChannel textChannel = DiscordConnection.jda.getTextChannelById(channelID);
        if (textChannel == null) return;
        event.setCancelled(true);
        textChannel.sendMessage("*<" + event.getPlayer().getName() + ">* " + event.getMessage()).queue((message)-> {
            String replyId = message.getId();
            net.md_5.bungee.api.chat.TextComponent textComponent = new net.md_5.bungee.api.chat.TextComponent();
            textComponent.setText("<"+event.getPlayer().getName()+"> "+event.getMessage());
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Reply")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/reply "+replyId+" "));
            Bukkit.getConsoleSender().sendMessage("<"+event.getPlayer().getName()+"> "+event.getMessage());
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));
        });
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {

        String name = event.getEntity().getName();
        int death = DiscordConnection.deathCount.getOrDefault(name, 0);
        DiscordConnection.deathCount.put(name, death + 1);
        DiscordConnection.saveDeathCount();
        String deathMsg = event.getDeathMessage();
        event.setDeathMessage(event.getDeathMessage()+"\n"+Config.getInstance().getDeathCountText()+":"+ChatColor.RED + (death + 1));

        if(DiscordConnection.jda==null)return;
        String channelID = Config.getInstance().getChannelId();
        if (channelID == null || channelID.isEmpty()) return;
        TextChannel textChannel = DiscordConnection.jda.getTextChannelById(channelID);
        if (textChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("*" + deathMsg + "*\n"+Config.getInstance().getDeathCountText()+":" + (death + 1)).setColor(Color.RED);

        textChannel.sendMessageEmbeds(builder.build()).queue();
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        DiscordConnection.setActivity();
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DiscordConnection.plugin, DiscordConnection::setActivity);
    }
}

