package com.coin.discordconnection;

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
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Collection;
import java.util.stream.Collectors;

public class JDAUtils {
    public JDA jda;
    public Config config = Config.getInstance();
    public JDAUtils() {

    }

    public void createJDA(String token) throws LoginException {

        JDA newJda = JDABuilder.createDefault(token).build();
        killJDA();
        jda = newJda;
        jda.addEventListener(new JDAEvents());
    }

    public void setActivity(int players,int maxPlayers) {
        if (jda == null) return;
        String activityText = config.getActivityText();
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, "[" + players + "/" + maxPlayers + "]" + activityText));
    }

    public void killJDA() {
        if (jda == null) return;
        jda.removeEventListener();
        jda.shutdownNow();
    }

    public String broadcastMessage(String name, String content){
        if (jda == null) return null;
        String channelID = Config.getInstance().getChatChannelId();
        if (channelID == null || channelID.isEmpty()) return null;
        TextChannel textChannel = jda.getTextChannelById(channelID);
        if (textChannel == null) return null;

        return textChannel.sendMessage("*<" + name + ">* " + content).complete().getId();
    }
    public void broadcastDeath(String deathMsg,int death){
        if (jda == null) return;
        String channelID = config.getDeathChannelId();
        if (channelID == null || channelID.isEmpty()) return;
        TextChannel textChannel = jda.getTextChannelById(channelID);
        if (textChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("*" + deathMsg + "*\n" + "Deaths" + ":" + death).setColor(Color.RED);
        textChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void broadcastAdvancement(String name,String advancement){
        if (jda == null) return;
        String channelID = config.getAdvancementChannelId();
        if (channelID == null || channelID.isEmpty()) return;
        TextChannel textChannel = jda.getTextChannelById(channelID);
        if (textChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("*" + name + "* has made the advancement [" + advancement + "]").setColor(Color.GREEN);

        textChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public boolean isConnected(){
        return jda!=null;
    }


}

class JDAEvents extends ListenerAdapter {
    private final String PREFIX = "-";
    private DiscordConnection discordConnection;
    private Config config = Config.getInstance();

    public JDAEvents() {
        discordConnection = DiscordConnection.instance;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        String channelID = event.getChannel().getId();
        if (channelID.equals(config.getChatChannelId())) {
            String content = event.getMessage().getContentRaw();
            if (content.startsWith(PREFIX)) {
                String command = content.replaceFirst(PREFIX, "");
                if (command.equalsIgnoreCase("ip")) {
                    String ip = Helper.getPublicIp();
                    if (ip != null) {
                        event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.GREEN).setTitle(ip).build()).queue();
                    } else {
                        event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.RED).setTitle("ERROR").addField("can't get server's ip", "", true).build()).queue();
                    }
                    return;
                }
                if (command.equalsIgnoreCase("list")) {
                    Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
                    event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle(players.size() + " Players")
                            .setDescription(players.stream().map(player -> player.getName()).collect(Collectors.joining("\n")))
                            .build()).queue();
                }
            }

            Message repliedMessage = event.getMessage().getReferencedMessage();

            if (repliedMessage != null) {
                Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + " ➡ Replied " + Helper.messageToText(repliedMessage, ChatColor.GRAY));
            }
            net.md_5.bungee.api.chat.TextComponent textComponent = new net.md_5.bungee.api.chat.TextComponent();
            textComponent.setText(Helper.messageToText(event.getMessage()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.TextComponent[]{new TextComponent("Click to Reply")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reply " + event.getMessage().getId() + " "));
            Bukkit.getConsoleSender().sendMessage(Helper.messageToText(event.getMessage()));
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));

        }
        if (channelID.equals(config.getConsoleChannelId())) {
            String command = event.getMessage().getContentRaw().replaceFirst("/", "");
            Bukkit.getScheduler().callSyncMethod(discordConnection, () -> {
                try {
                    if (Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command))
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + event.getAuthor().getName() + ChatColor.RESET + " had executed " + ChatColor.DARK_AQUA + "/" + command);
                } catch (CommandException ignored) {
                }
                return null;
            });
        }
    }


}