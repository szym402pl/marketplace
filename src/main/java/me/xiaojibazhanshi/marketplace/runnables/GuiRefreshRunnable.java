package me.xiaojibazhanshi.marketplace.runnables;

import me.xiaojibazhanshi.marketplace.Marketplace;
import me.xiaojibazhanshi.marketplace.data.DatabaseManager;
import me.xiaojibazhanshi.marketplace.guis.BlackMarketGui;
import me.xiaojibazhanshi.marketplace.guis.MarketplaceGui;
import org.bukkit.scheduler.BukkitRunnable;

public class GuiRefreshRunnable extends BukkitRunnable {

    private final Marketplace main;
    private final BlackMarketGui blackMarketGui;
    private final MarketplaceGui marketplaceGui;

    public GuiRefreshRunnable(Marketplace main, BlackMarketGui blackMarketGui, MarketplaceGui marketplaceGui) {
        this.blackMarketGui = blackMarketGui;
        this.marketplaceGui = marketplaceGui;
        this.main = main;
    }

    public void start() {
        this.runTaskTimer(main, 5 * 20, 5 * 20);
    }

    public void stop() {
        this.cancel();
    }

    @Override
    public void run() {
        blackMarketGui.update();
        marketplaceGui.update();
    }
}
