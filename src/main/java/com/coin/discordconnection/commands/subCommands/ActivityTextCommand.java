package com.coin.discordconnection.commands.subCommands;

import com.coin.discordconnection.Config;
import com.coin.discordconnection.DiscordConnection;
import com.coin.discordconnection.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ActivityTextCommand extends SubCommand {
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0){
            sender.sendMessage(ChatColor.RED+"Please enter text\n"+getUsage());
            return;
        }
        Config.getInstance().setActivityText(String.join(" ",args));
        DiscordConnection.setActivity();
        sender.sendMessage(ChatColor.GREEN + "[DiscordPlugin]:Activity text had been set");
    }

    @Override
    public String getName() {
        return "activityText";
    }

    @Override
    public String getDescription() {
        return "set the activity text displayed on the bot ";
    }

    @Override
    public String getUsage() {
        return "/discord activitytext <text>";
    }
}
