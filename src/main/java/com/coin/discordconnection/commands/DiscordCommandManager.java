package com.coin.discordconnection.commands;

import com.coin.discordconnection.commands.subCommands.ActivityTextCommand;
import com.coin.discordconnection.commands.subCommands.ChannelCommand;
import com.coin.discordconnection.commands.subCommands.TokenCommand;

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

}
