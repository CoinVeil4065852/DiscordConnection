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

public class ChannelCommand extends SubCommand {
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0){
            sender.sendMessage(ChatColor.RED+"Please enter channel id\n"+getUsage());
            return;
        }
        JDA jda = DiscordConnection.jda;
        if(jda==null){
            sender.sendMessage(ChatColor.RED+"Please set token first\n/discord token <token>");
            return;
        }

        TextChannel textChannel = jda.getTextChannelById(args[0]);
        if(textChannel==null){
            sender.sendMessage(ChatColor.RED+"Id incorrect");
            return;
        }

        Config.getInstance().setChannelId(args[0]);
        sender.sendMessage(ChatColor.GREEN + "[DiscordPlugin]channel had been set to "+textChannel.getGuild()+"/"+textChannel.getName() );
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
        return "/discord channel";
    }
}
