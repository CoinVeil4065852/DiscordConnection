package com.coin.discordconnection.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubCommand {
    public abstract void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getUsage();

}
