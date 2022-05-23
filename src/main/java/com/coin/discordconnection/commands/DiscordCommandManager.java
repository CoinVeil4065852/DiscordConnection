package com.coin.discordconnection.commands;

import com.coin.discordconnection.commands.subCommands.ActivityTextCommand;
import com.coin.discordconnection.commands.subCommands.ChannelCommand;
import com.coin.discordconnection.commands.subCommands.TokenCommand;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DiscordCommandManager extends CommandManager{
    @Override
    public List<SubCommand> getSubCommands() {
        List<SubCommand> subCommands = new ArrayList<>(
        );
        subCommands.add(new TokenCommand());
        subCommands.add(new ActivityTextCommand());
        subCommands.add(new ChannelCommand());
        return subCommands;
    }

    @Override
    public TabCompleter getTabCompleter() {
        List<String> strings = new ArrayList<>();
        for (SubCommand subCommand:getSubCommands()) {
            strings.add(subCommand.getName());
        }
        return (sender, command, alias, args) -> {
            if(args.length==1) {

                return strings;
            }
            if(args[0].equalsIgnoreCase("channel")){
                if(args.length==2){
                    List<String> subCommands = new ArrayList<>();
                    subCommands.add(ChannelType.CONSOLE);
                    subCommands.add(ChannelType.ADVANCEMENT);
                    subCommands.add(ChannelType.CHAT);
                    subCommands.add(ChannelType.DEATH);
                    subCommands.add(ChannelType.ALL);

                    return subCommands;
                }
                if(args.length==3){
                    List<String> subCommands = new ArrayList<>();
                    subCommands.add("clear");
                    subCommands.add("set");
                    return subCommands;
                }

            }

            return null;
        };

    }
}
