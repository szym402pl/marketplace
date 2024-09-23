package me.xiaojibazhanshi.marketplace.runnables;

import me.xiaojibazhanshi.marketplace.Marketplace;
import me.xiaojibazhanshi.marketplace.data.DatabaseManager;
import org.bukkit.scheduler.BukkitRunnable;

public class DataSaveRunnable extends BukkitRunnable {

    private final DatabaseManager databaseManager;
    private final Marketplace main;

    public DataSaveRunnable(Marketplace main, DatabaseManager databaseManager) {
        this.main = main;
        this.databaseManager = databaseManager;
    }

    public void start() {
        this.runTaskTimerAsynchronously(main, 300 * 20, 300 * 20);
    }

    public void stop() {
        this.cancel();
    }

    @Override
    public void run() {
        databaseManager.saveData();
    }
}
