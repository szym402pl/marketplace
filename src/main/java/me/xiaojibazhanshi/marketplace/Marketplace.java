package me.xiaojibazhanshi.marketplace;

import me.xiaojibazhanshi.marketplace.commands.BlackMarketCommand;
import me.xiaojibazhanshi.marketplace.commands.MarketplaceCommand;
import me.xiaojibazhanshi.marketplace.commands.SellCommand;
import me.xiaojibazhanshi.marketplace.commands.TransactionsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Marketplace extends JavaPlugin {

    // TODO: Use the custom events for easier Discord web hook management

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getCommand("blackmarket").setExecutor(new BlackMarketCommand());
        getCommand("marketplace").setExecutor(new MarketplaceCommand());
        getCommand("sell").setExecutor(new SellCommand());
        getCommand("transactions").setExecutor(new TransactionsCommand());
    }
}
