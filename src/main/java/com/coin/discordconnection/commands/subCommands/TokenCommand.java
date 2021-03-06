package com.coin.discordconnection.commands.subCommands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.JDAUtils;
import com.coin.discordconnection.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.zip.DeflaterInputStream;

public class TokenCommand extends SubCommand {


    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        JDAUtils jdaUtils = DiscordConnection.instance.jdaUtils;
        if(args.length==0){
            sender.sendMessage(ChatColor.RED+"Please enter token\n usage:"+getUsage());
            return;
        }
        try {
            jdaUtils.createJDA(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Token had been set, use /discord channel <channel> set <id> to set channel");
            Config.getInstance().setToken(args[0]);
        } catch (LoginException e) {
            sender.sendMessage(ChatColor.RED + "Can't connect to discord, token may be incorrect");
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
