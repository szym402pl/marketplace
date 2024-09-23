package me.xiaojibazhanshi.marketplace;

import lombok.Getter;
import me.xiaojibazhanshi.marketplace.commands.BlackMarketCommand;
import me.xiaojibazhanshi.marketplace.commands.MarketplaceCommand;
import me.xiaojibazhanshi.marketplace.commands.SellCommand;
import me.xiaojibazhanshi.marketplace.commands.TransactionsCommand;
import me.xiaojibazhanshi.marketplace.config.ConfigManager;
import me.xiaojibazhanshi.marketplace.data.DatabaseManager;
import me.xiaojibazhanshi.marketplace.guis.BlackMarketGui;
import me.xiaojibazhanshi.marketplace.guis.MarketplaceGui;
import me.xiaojibazhanshi.marketplace.guis.TransactionGui;
import me.xiaojibazhanshi.marketplace.listeners.BlackMarketPurchaseListener;
import me.xiaojibazhanshi.marketplace.listeners.MarketplacePurchaseListener;
import me.xiaojibazhanshi.marketplace.runnables.DataSaveRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Marketplace extends JavaPlugin {
    // TODO: Use the custom events for easier Discord web hook management
    // TODO: Implement a gui refresh runnable

    private DataSaveRunnable dataSaveRunnable;

    private DatabaseManager databaseManager;
    private ConfigManager configManager;

    private BlackMarketGui blackMarketGui;
    private MarketplaceGui marketplaceGui;
    private TransactionGui transactionGui;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this, configManager);

        dataSaveRunnable = new DataSaveRunnable(this, databaseManager);
        dataSaveRunnable.start();

        /*
        * new BlackMarketGui()
        * new MarketplaceGui()
        * new TransactionGui()
        */

        registerCommands();
    }

    @Override
    public void onDisable() {
        databaseManager.saveData();
        databaseManager.closeMongoClient();
        databaseManager.clearCaches();

        dataSaveRunnable.stop();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlackMarketPurchaseListener(), this);
        Bukkit.getPluginManager().registerEvents(new MarketplacePurchaseListener(), this);
    }

    @SuppressWarnings("all") // hate that yellow marking, like damn
    private void registerCommands() {
        getCommand("sell").setExecutor(new SellCommand(configManager));
        getCommand("blackmarket").setExecutor(new BlackMarketCommand());
        getCommand("marketplace").setExecutor(new MarketplaceCommand());
        getCommand("transactions").setExecutor(new TransactionsCommand());
    }
}
