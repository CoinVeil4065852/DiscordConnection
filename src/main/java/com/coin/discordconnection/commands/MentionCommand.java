package com.coin.discordconnection.commands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.Helper;
import com.coin.discordconnection.JDAUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DeflaterInputStream;

public class MentionCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Config config = Config.getInstance();
        JDAUtils jdaUtils = DiscordConnection.instance.jdaUtils;
        if (!jdaUtils.isConnected() || config.getChatChannelId() == null || config.getChatChannelId().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Discord bot hasn't been connected properly");
            return true;
        }

        TextChannel textChannel = jdaUtils.jda.getTextChannelById(config.getChatChannelId());
        if (textChannel == null) {
            sender.sendMessage(ChatColor.RED + "Discord bot hasn't been set properly");
            return true;
        }
        User userToMention = jdaUtils.jda.getUserById(args[0].split("<")[1].split(">")[0]);
        if (userToMention == null) {
            sender.sendMessage(ChatColor.RED + "Incorrect id");
            return true;
        }
        String msg = String.join(" ", (Arrays.copyOfRange(args, 1, args.length)));

        textChannel.sendMessage("<" + sender.getName() + "> " + userToMention.getAsMention() + msg).queue(message -> {
            String replyId = message.getId();
            TextComponent textComponent = new TextComponent();
            textComponent.setText("<" + sender.getName() + "> " + ChatColor.YELLOW + "@" + userToMention.getName() + ChatColor.WHITE + msg);
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Click to Reply")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reply " + replyId + " "));
            Bukkit.getConsoleSender().sendMessage("<" + sender.getName() + "> " + ChatColor.YELLOW + "@" + userToMention.getName() + ChatColor.WHITE + msg);
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));
        });

        return true;
    }

    public TabCompleter getTabCompleter() {
        return (sender, command, alias, args) -> {
            if (args.length == 1) {
                JDA jda = DiscordConnection.instance.jdaUtils.jda;
                TextChannel textChannel = jda.getTextChannelById(Config.getInstance().getChatChannelId());
                if (textChannel == null) return null;
                return textChannel.getMembers().stream().map(member -> member.getUser().getName()+"<"+member.getId()+">" ).collect(Collectors.toList());
            }
            return null;
        };

    }
}
