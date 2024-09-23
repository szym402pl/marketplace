package me.xiaojibazhanshi.marketplace.guis;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xiaojibazhanshi.marketplace.config.ConfigManager;
import me.xiaojibazhanshi.marketplace.data.DatabaseManager;
import me.xiaojibazhanshi.marketplace.objects.Listing;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.xiaojibazhanshi.marketplace.utils.ItemStackUtil.convertToGuiItem;

public class BlackMarketGui {

    private static Gui gui;

    public BlackMarketGui(ConfigManager configManager, DatabaseManager databaseManager) {

    }

    // Start-up stuff

    private void createGui() {

    }

    private void populateGui(List<Listing> listingData) {
        //
    }

    // Normal during-gameplay stuff

    public void addItem(@NotNull ItemStack item) {
        GuiItem converted = convertToGuiItem(item);
        assert converted != null;

        gui.addItem(converted);
        gui.update();
    }

    public void removeItem(@NotNull ItemStack item) {
        GuiItem converted = convertToGuiItem(item);
        assert converted != null;

        gui.removeItem(converted);
        gui.update();
    }

    public void update() {
        gui.update();
    }

    public void openGui(Player player) {
        gui.open(player);
    }

    public void closeGui(Player player) {
        gui.close(player);
    }



}
