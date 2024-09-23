package me.xiaojibazhanshi.marketplace.commands;

import me.xiaojibazhanshi.marketplace.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SellCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public SellCommand(ConfigManager configManager) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        return true;
    }
}
