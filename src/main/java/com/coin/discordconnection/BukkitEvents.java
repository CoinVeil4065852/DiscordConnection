package com.coin.discordconnection;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

class BukkitEvents implements Listener {
    public DiscordConnection discordConnection;
    public DeathCount deathCount;
    public Config config = Config.getInstance();

    public BukkitEvents() {
        discordConnection = DiscordConnection.instance;

        deathCount = discordConnection.deathCount;
    }

    public void getPlayers() {

    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        JDAUtils jdaUtils = discordConnection.jdaUtils;
        event.setCancelled(true);
        String replyId = jdaUtils.broadcastMessage(event.getPlayer().getName(), event.getMessage());
        if (replyId == null) return;
        net.md_5.bungee.api.chat.TextComponent textComponent = new net.md_5.bungee.api.chat.TextComponent();
        textComponent.setText("<" + event.getPlayer().getName() + "> " + event.getMessage());
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Click to Reply")}));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reply " + replyId + " "));
        Bukkit.getConsoleSender().sendMessage("<" + event.getPlayer().getName() + "> " + event.getMessage());
        Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        JDAUtils jdaUtils = discordConnection.jdaUtils;
        Player player = event.getEntity();
        deathCount.addDeath(player);
        int death = deathCount.getDeathCount(player);

        String deathMsg = event.getDeathMessage();
        event.setDeathMessage(event.getDeathMessage() + "\n" + "Deaths" + ":" + ChatColor.RED + death);
        jdaUtils.broadcastDeath(deathMsg, death);


    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        JDAUtils jdaUtils = discordConnection.jdaUtils;
        jdaUtils.broadcastAdvancement(event.getPlayer().getName(), event.getAdvancement().getKey().getKey().split("/")[event.getAdvancement().getKey().getKey().split("/").length - 1].replace("_", " "));

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        JDAUtils jdaUtils = discordConnection.jdaUtils;
        setActivity();
        if (!jdaUtils.isConnected()) return;
        event.getPlayer().sendMessage(ChatColor.GREEN + "This server is using " + ChatColor.DARK_AQUA + "[DiscordConnection]\n" + ChatColor.GREEN + "Chat on this server's discord channel in game, you can also click any message to reply!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(discordConnection, () -> {
            setActivity();
        });
    }

    private void setActivity() {
        JDAUtils jdaUtils = discordConnection.jdaUtils;
        jdaUtils.setActivity(discordConnection.getServer().getOnlinePlayers().size(), discordConnection.getServer().getMaxPlayers());
    }
}