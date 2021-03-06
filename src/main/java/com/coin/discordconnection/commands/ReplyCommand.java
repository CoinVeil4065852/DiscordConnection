package com.coin.discordconnection.commands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.Helper;
import com.coin.discordconnection.JDAUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReplyCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Config config = Config.getInstance();
        JDAUtils jdaUtils = DiscordConnection.instance.jdaUtils;
        if (!jdaUtils.isConnected() || config.getChatChannelId() == null || config.getChatChannelId().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Discord bot hasn't been connected properly");
            return true;
        }
        if (args.length < 2) return false;

        TextChannel textChannel = jdaUtils.jda.getTextChannelById(config.getChatChannelId());
        if (textChannel == null) {
            sender.sendMessage(ChatColor.RED + "Discord bot hasn't been set properly");
            return true;
        }
        Message messageToReply = textChannel.retrieveMessageById(args[0]).complete();
        if (messageToReply == null) {
            sender.sendMessage(ChatColor.RED + "Incorrect id");
            return true;
        }
        String msg = String.join(" ", (Arrays.copyOfRange(args, 1, args.length)));

        messageToReply.reply("<" + sender.getName() + "> " + msg).queue(message -> {
            String replyId = message.getId();
            TextComponent textComponent = new TextComponent();
            textComponent.setText("<" + sender.getName() + "> " + msg);
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Click to Reply")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reply " + replyId + " "));

            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GRAY + " ??? Replied " + Helper.messageToText(messageToReply, ChatColor.GRAY));
            Bukkit.getConsoleSender().sendMessage("<" + sender.getName() + "> " + msg);

            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.DARK_GRAY + " ??? Replied " + Helper.messageToText(messageToReply, ChatColor.GRAY)));
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));
        });

        return true;
    }
}
