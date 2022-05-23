package com.coin.discordconnection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandManager implements CommandExecutor {
    public abstract List<SubCommand> getSubCommands();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "No Permission");
            return true;
        }
        if (args.length == 0) {
            help(sender);
            return true;
        } else {
            for (SubCommand subCommand : getSubCommands()) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    subCommand.execute(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
            }
        }
        return false;
    }

    private void help(CommandSender sender) {
        StringBuilder helperText = new StringBuilder();
        for (SubCommand subCommand : getSubCommands()) {
            helperText.append("\n").append(subCommand.getUsage()).append("  ").append(subCommand.getDescription());
        }
        sender.sendMessage(helperText.toString());

    }
    public TabCompleter getTabCompleter(){
        List<String> strings = new ArrayList<>();
        for (SubCommand subCommand:getSubCommands()) {
            strings.add(subCommand.getName());
        }
        return (sender, command, alias, args) -> {
            if(args.length==1) {

                return strings;
            }

            return null;
        };
    }


}
