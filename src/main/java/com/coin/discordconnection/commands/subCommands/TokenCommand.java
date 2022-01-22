package com.coin.discordconnection.commands.subCommands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class TokenCommand extends SubCommand {


    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0){
            sender.sendMessage(ChatColor.RED+"Please enter token\n"+getUsage());
            return;
        }
        try {
            DiscordConnection.startJDA(args[0]);
            sender.sendMessage(ChatColor.GREEN + "[DiscordPlugin]:token had been set, use /dcchannel [channelId] to set channel");
            Config.getInstance().setToken(args[0]);
        } catch (LoginException e) {
            sender.sendMessage(ChatColor.RED + "[DiscordPlugin]:token incorrect");
        }
    }

    @Override
    public String getName() {
        return "token";
    }

    @Override
    public String getDescription() {
        return "set the bot's token";
    }

    @Override
    public String getUsage() {
        return "/discord token <token>";
    }
}
