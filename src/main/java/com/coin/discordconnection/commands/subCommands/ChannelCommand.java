package com.coin.discordconnection.commands.subCommands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.commands.SubCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChannelCommand extends SubCommand {
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + getUsage());
            return;
        }
        if (args[1].equalsIgnoreCase("clear")) {
            switch (args[0]) {
                case "chat":
                    Config.getInstance().setChatChannelId(null);
                    return;
                case "console":
                    Config.getInstance().setConsoleChannelId(null);
                    return;
                case "achievement":
                    Config.getInstance().setAchievementChannelId(null);
                    return;
                case "death":
                    Config.getInstance().setDeathMessageChannelId(null);
                    return;
                case "all":
                    Config.getInstance().setConsoleChannelId(null);
                    Config.getInstance().setChatChannelId(null);
                    Config.getInstance().setAchievementChannelId(null);
                    return;
                default:
                    return;
            }
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (args.length == 2) {
                sender.sendMessage(ChatColor.RED + "Please enter channel id\n" + getUsage());
                return;
            }
            JDA jda = DiscordConnection.jda;
            if (jda == null) {
                sender.sendMessage(ChatColor.RED + "Please set token first\n/discord token <token>");
                return;
            }
            TextChannel textChannel = jda.getTextChannelById(args[2]);
            if (textChannel == null) {
                sender.sendMessage(ChatColor.RED + "Id incorrect");
                return;
            }
            switch (args[0]) {
                case "chat":
                    Config.getInstance().setChatChannelId(args[2]);
                    break;
                case "console":
                    Config.getInstance().setConsoleChannelId(args[2]);
                    break;
                case "achievement":
                    Config.getInstance().setAchievementChannelId(args[2]);
                    break;
                case "death":
                    Config.getInstance().setDeathMessageChannelId(args[2]);
                    break;
                case "all":
                    Config.getInstance().setAchievementChannelId(args[2]);
                    Config.getInstance().setChatChannelId(args[2]);
                    Config.getInstance().setConsoleChannelId(args[2]);
                    Config.getInstance().setDeathMessageChannelId(args[2]);

                    break;
                default:
                    return;
            }
            sender.sendMessage(ChatColor.GREEN + "channel had been set to " + textChannel.getGuild() + "/" + textChannel.getName());
        }


    }


    @Override
    public String getName() {
        return "channel";
    }

    @Override
    public String getDescription() {
        return "set the channel connected by the bot";
    }

    @Override
    public String getUsage() {
        return "/discord channel <channel> <method> (<id>)";
    }


}
