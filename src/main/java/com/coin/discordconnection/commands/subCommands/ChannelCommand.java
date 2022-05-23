package com.coin.discordconnection.commands.subCommands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.commands.ChannelType;
import com.coin.discordconnection.commands.SubCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChannelCommand extends SubCommand {

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Config config = Config.getInstance();
        JDA jda =DiscordConnection.instance.jdaUtils.jda;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + getUsage());
            return;
        }
        if (args[1].equalsIgnoreCase("clear")) {
            if(args[0].equalsIgnoreCase(ChannelType.CHAT)){
                config.setChatChannelId(null);
            }else if(args[0].equalsIgnoreCase(ChannelType.CONSOLE)){
                config.setConsoleChannelId(null);
            }else if(args[0].equalsIgnoreCase(ChannelType.ADVANCEMENT)){
                config.setAdvancementChannelId(null);
            }else if(args[0].equalsIgnoreCase(ChannelType.DEATH)){
                config.setDeathChannelId(null);
            }else if(args[0].equalsIgnoreCase(ChannelType.ALL)){
                config.setConsoleChannelId(null);
                config.setChatChannelId(null);
                config.setAdvancementChannelId(null);
                config.setDeathChannelId(null);
            }else {
                sender.sendMessage(ChatColor.RED + "No channel found\navailable options: "+ChannelType.CHAT+", "+ChannelType.CONSOLE+", "+ChannelType.ADVANCEMENT+", "+ChannelType.DEATH+", "+ChannelType.ALL );
                return ;
            }
            sender.sendMessage(ChatColor.GREEN + "Channel had been clear" );
        }else if (args[1].equalsIgnoreCase("set")) {

            if (jda == null) {
                sender.sendMessage(ChatColor.RED + "Please set token first\n/discord token <token>");
                return;
            }
            try {
                TextChannel textChannel = jda.getTextChannelById(args[2]);
                if (textChannel == null) {
                    sender.sendMessage(ChatColor.RED + "Id incorrect");
                    return;
                }
                if (args.length == 2) {
                    sender.sendMessage(ChatColor.RED + "Please enter channel id\nusage: " + getUsage());
                    return;
                }
                if(args[0].equalsIgnoreCase(ChannelType.CHAT)){
                    config.setChatChannelId(args[2]);
                }else if(args[0].equalsIgnoreCase(ChannelType.CONSOLE)){
                    config.setConsoleChannelId(args[2]);
                }else if(args[0].equalsIgnoreCase(ChannelType.ADVANCEMENT)){
                    config.setAdvancementChannelId(args[2]);
                }else if(args[0].equalsIgnoreCase(ChannelType.DEATH)){
                    config.setDeathChannelId(args[2]);
                }else if(args[0].equalsIgnoreCase(ChannelType.ALL)){
                    config.setAdvancementChannelId(args[2]);
                    config.setChatChannelId(args[2]);
                    config.setConsoleChannelId(args[2]);
                    config.setDeathChannelId(args[2]);
                }else {
                    sender.sendMessage(ChatColor.RED + "No channel found\navailable options: "+ChannelType.CHAT+", "+ChannelType.CONSOLE+", "+ChannelType.ADVANCEMENT+", "+ChannelType.DEATH+", "+ChannelType.ALL);
                    return ;
                }
                sender.sendMessage(ChatColor.GREEN + "Channel had been set to " + textChannel.getGuild() + "/" + textChannel.getName());
            }catch (Exception e){
                sender.sendMessage(ChatColor.RED + "Id incorrect");
            }



        }else {
            sender.sendMessage(ChatColor.RED + "No method found\navailable options: set, clear");
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
