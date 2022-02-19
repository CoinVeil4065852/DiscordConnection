package com.coin.discordconnection.commands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.Helper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReplyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (DiscordConnection.jda == null || Config.getInstance().getChannelId() == null || Config.getInstance().getChannelId().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Discord bot hasn't been connected properly");
            return true;
        }
        if (args.length < 2) return false;

        TextChannel textChannel = DiscordConnection.jda.getTextChannelById(Config.getInstance().getChannelId());
        if (textChannel == null) {
            sender.sendMessage(ChatColor.RED + "Discord bot hasn't been set properly");
            return true;
        }
        Message messageToReply = textChannel.retrieveMessageById(args[0]).complete();
        if (messageToReply == null) {
            sender.sendMessage(ChatColor.RED + "Incorrect id");
            return true;
        }

        messageToReply.reply("<" + sender.getName() + "> " +args[1]).queue(message -> {
            String replyId = message.getId();
            TextComponent textComponent = new TextComponent();
            textComponent.setText("<" + sender.getName() + "> " + args[1]);
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to Reply")));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reply " + replyId + " "));

            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GRAY + " ➡ Replied " + Helper.messageToText(messageToReply,ChatColor.GRAY));
            Bukkit.getConsoleSender().sendMessage("<" + sender.getName() + "> " + args[1]);

            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.DARK_GRAY + " ➡ Replied " + Helper.messageToText(messageToReply,ChatColor.GRAY)));
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));
        });

        return true;
    }
}
