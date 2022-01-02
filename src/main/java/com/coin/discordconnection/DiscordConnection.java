package com.coin.discordconnection;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class DiscordConnection extends JavaPlugin {
    public static JavaPlugin plugin;

    public static String token;
    public static String tcId;
    public static JDA jda;
    public static final File CONFIG = new File("./plugins/discordPlugin.config");
    public static final File DEATH_COUNT = new File("./plugins/discordPluginDeathCount.txt");
    public static Map<String, Integer> deathCount = new HashMap();
    public static String activityText;
    public static final String DEFAULT_ACTIVITY_TEXT = "Server Opening";

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new BukkitEvents(), this);
        getCommand("token").setExecutor(new Commands());
        getCommand("DCChannel").setExecutor(new Commands());
        getCommand("activityText").setExecutor(new Commands());
        try {
            BufferedReader configBR = new BufferedReader(new FileReader(CONFIG));
            token = configBR.readLine();
            tcId = configBR.readLine();
            activityText = configBR.readLine();
            configBR.close();

            BufferedReader deathCountBR = new BufferedReader(new FileReader(DEATH_COUNT));
            String line = deathCountBR.readLine();
            while (line != null) {
                String[] args = line.split(" ");
                deathCount.put(args[0], Integer.parseInt(args[1]));
                line = deathCountBR.readLine();
            }
        } catch (IOException ignored) {


        }
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
            startJDA(token);
        } catch (LoginException e) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[DiscordPlugin]:cant connect to discord");
        }
    }

    public static void startJDA(String token) throws LoginException {
        jda = JDABuilder.createDefault(token).build();
        setActivity();
        jda.addEventListener(new JDAListener());
    }

    public static void setActivity() {
        if (jda == null) return;
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, "[" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "]" + (activityText != null && activityText.isEmpty() ? DEFAULT_ACTIVITY_TEXT : activityText)));
    }

    public static void save() {
        try {
            FileWriter fw = new FileWriter(CONFIG, false);
            fw.write((DiscordConnection.token == null ? "" : DiscordConnection.token) + "\n" + (DiscordConnection.tcId == null ? "" : DiscordConnection.tcId) + "\n" + (activityText == null ? "" : activityText));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDeathCount() {
        try {
            FileWriter fw = new FileWriter(DEATH_COUNT, false);
            String s = "";
            for (String name : deathCount.keySet()) {
                s = s + name + " " + deathCount.get(name) + "\n";
            }
            fw.write(s);
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
        if (!event.getChannel().getId().equals(DiscordConnection.tcId)) return;
        if (event.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        if(event.getMessage().getContentRaw().equalsIgnoreCase("-ip"))
        {
            String ip = getPublicIp();
            if(ip!=null){
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.GREEN).setTitle(ip).build()).queue();
            }else {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.RED).setTitle("ERROR").addField("can't get server's ip","",true).build()).queue();
            }
            return;
        }
        if (event.getMessage().getAttachments().size() > 0) {
            String msg = "";
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                msg = msg + "[" + attachment.getContentType().split("/")[0] + "]";
            }
            DiscordConnection.plugin.getServer().broadcastMessage(ChatColor.BLUE + "[" + event.getAuthor().getName() + "]:" + ChatColor.WHITE + msg);
        }
        if (!event.getMessage().getContentRaw().isEmpty())
            DiscordConnection.plugin.getServer().broadcastMessage(ChatColor.BLUE + "[" + event.getAuthor().getName() + "]:" + ChatColor.WHITE + event.getMessage().getContentStripped());

    }
    private String getPublicIp(){
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }
}

class BukkitEvents implements Listener {
    @EventHandler
    public static void onPlayerChat(AsyncPlayerChatEvent event) {
        if (DiscordConnection.tcId == null || DiscordConnection.tcId.isEmpty()) return;
        TextChannel textChannel = DiscordConnection.jda.getTextChannelById(DiscordConnection.tcId);
        if (textChannel == null) return;
        textChannel.sendMessage("*<" + event.getPlayer().getName() + ">* " + event.getMessage()).queue();

    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {
        String name = event.getEntity().getName();
        int death = DiscordConnection.deathCount.getOrDefault(name, 0);
        DiscordConnection.deathCount.put(name, death + 1);
        DiscordConnection.saveDeathCount();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DiscordConnection.plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().broadcastMessage("死亡累計:" + ChatColor.RED + (death + 1));
            }
        });
        if (DiscordConnection.tcId == null || DiscordConnection.tcId.isEmpty()) return;
        TextChannel textChannel = DiscordConnection.jda.getTextChannelById(DiscordConnection.tcId);
        if (textChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("*" + event.getDeathMessage() + "*\n死亡累計:" + (death + 1)).setColor(Color.RED);

        textChannel.sendMessageEmbeds(builder.build()).queue();
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {

        DiscordConnection.setActivity();
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DiscordConnection.plugin, new Runnable() {
            @Override
            public void run() {
                DiscordConnection.setActivity();
            }
        });


    }
}

class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "[DiscordPlugin]:Permission denied");
            return true;
        }
        if (command.getName().equalsIgnoreCase("token")) {
            try {
                DiscordConnection.startJDA(strings[0]);
                DiscordConnection.tcId = null;
                DiscordConnection.token = strings[0];
                commandSender.sendMessage(ChatColor.GREEN + "[DiscordPlugin]:token had been set, use /dcchannel [channelId] to set channel");
                DiscordConnection.save();
            } catch (LoginException e) {
                commandSender.sendMessage(ChatColor.RED + "[DiscordPlugin]:token incorrect");
            }
        }
        if (command.getName().equalsIgnoreCase("DCChannel")) {
            if (DiscordConnection.jda == null) {
                commandSender.sendMessage(ChatColor.RED + "[DiscordPlugin]:token is not set, use /token [token] to set token");
                return true;
            }

            TextChannel tc = DiscordConnection.jda.getTextChannelById(strings[0]);
            if (tc == null) {
                commandSender.sendMessage(ChatColor.RED + "[DiscordPlugin]:id incorrect");
                return true;
            }

            DiscordConnection.tcId = strings[0];
            commandSender.sendMessage(ChatColor.GREEN + "[DiscordPlugin]channel had been set to " + tc.getGuild().getName() + "/" + tc.getName());
            DiscordConnection.save();
        }
        if (command.getName().equalsIgnoreCase("activityText")) {
            if (DiscordConnection.jda == null) {
                commandSender.sendMessage(ChatColor.RED + "[DiscordPlugin]:token is not set, use /token [token] to set token");
                return true;
            }
            DiscordConnection.activityText = strings[0];
            DiscordConnection.setActivity();
            DiscordConnection.save();
        }
        return true;
    }
}