package com.coin.discordconnection.commands.subCommands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class DeathCountTextCommand extends SubCommand {


    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0){
            sender.sendMessage(ChatColor.RED+"Please enter text\n"+getUsage());
            return;
        }

            Config.getInstance().setDeathCountText(Arrays.toString(Arrays.copyOfRange(args,1,args.length)));

    }

    @Override
    public String getName() {
        return "DeathCountText";
    }

    @Override
    public String getDescription() {
        return "set the DeathCountText";
    }

    @Override
    public String getUsage() {
        return "/discord deathcounttext <text>";
    }
}
