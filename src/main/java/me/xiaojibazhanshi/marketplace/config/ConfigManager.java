package me.xiaojibazhanshi.marketplace.config;

import lombok.AccessLevel;
import lombok.Getter;
import me.xiaojibazhanshi.marketplace.Marketplace;
import me.xiaojibazhanshi.marketplace.objects.helper.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigManager {

    @Getter(AccessLevel.NONE) private FileConfiguration config;
    @Getter(AccessLevel.NONE) private final Marketplace main;

    /* Variables */

    private String connectionString;

    private String sellPermission;
    private String marketplacePermission;
    private String blackMarketPermission;
    private String transactionsPermission;
    private String marketplaceReloadPermission;

    private BlackMarketOptions blackMarketOptions;
    private MarketplaceOptions marketplaceOptions;

    private GuiItemFormat listingFormat;
    private GuiItemFormat transactionFormat;

    private SimpleButtonFormat nextPageFormat;
    private SimpleButtonFormat previousPageFormat;
    private SimpleButtonFormat fillerFormat;

    private GuiOptions blackMarketGuiOptions;
    private GuiOptions marketplaceGuiOptions;
    private GuiOptions transactionGuiOptions;

    /* Constructor */

    public ConfigManager(Marketplace main) {
        this.main = main;
        this.config = this.main.getConfig();

        initializeVariables();
    }

    /* Main methods */

    private void initializeVariables() {
        connectionString = getOrDefault("mongodb-connection-string", String.class, "replace this");

        initializePermissions();
        initializeBlackMarketOptions();
        initializeMarketplaceOptions();
        initializeGuiItemFormats();
        initializeButtonFormats();
        initializeGuiOptions();
    }

    public void reload() {
        main.reloadConfig();
        config = main.getConfig();

        initializeVariables();
    }

    /* Helper methods */

    private void initializeGuiOptions() {
        String prefix = "gui-options.";

        String blackMarketName = getOrDefault(prefix + "black-market-gui.name",
                String.class, "&0Black Market &7| {time-until-refresh} &7until refresh");
        int blackMarketRows = getOrDefault(prefix + "black-market-gui.rows", Integer.class, 3);
        String refreshFormat = getOrDefault(prefix + "black-market-gui.time-until-refresh-format",
                String.class, "&c{h}&7:&c{min}&7:&c{sec}");

        String marketplaceName = getOrDefault(prefix + "black-market-gui.name",
                String.class, "&0Black Market &7| {time-until-refresh} &7until refresh");
        int marketplaceRows = getOrDefault(prefix + "black-market-gui.rows", Integer.class, 3);

        String transactionsName = getOrDefault(prefix + "black-market-gui.name",
                String.class, "&0Black Market &7| {time-until-refresh} &7until refresh");
        int transactionsRows = getOrDefault(prefix + "black-market-gui.rows", Integer.class, 3);

        blackMarketRows = returnValidRowAmount(blackMarketRows);
        marketplaceRows = returnValidRowAmount(marketplaceRows);
        transactionsRows = returnValidRowAmount(transactionsRows);

        transactionGuiOptions = new GuiOptions(transactionsName, transactionsRows, null);
        marketplaceGuiOptions = new GuiOptions(marketplaceName, marketplaceRows, null);
        blackMarketGuiOptions = new GuiOptions(blackMarketName, blackMarketRows, refreshFormat);
    }

    private int returnValidRowAmount(int rows) {
        return rows < 2 || rows > 6 ? 3 : rows;
    }

    private void initializeButtonFormats() {
        String prefix = "gui-options.";

        String nextPageName = getOrDefault(prefix + "page-buttons.next.name", String.class, "&cNext");
        String nextPageMatStr = getOrDefault(prefix + "page-buttons.next.material", String.class, "PAPER");
        List<String> nextPageLore = config.getStringList(prefix + "page-buttons.next.lore");

        String prevPageName = getOrDefault(prefix + "page-buttons.previous.name", String.class, "&cPrevious");
        String prevPageMatStr = getOrDefault(prefix + "page-buttons.previous.material", String.class, "PAPER");
        List<String> prevPageLore = config.getStringList(prefix + "page-buttons.previous.lore");

        String fillerName = getOrDefault(prefix + "gui-filler.name", String.class, " ");
        String fillerMatStr = getOrDefault(prefix + "gui-filler.material", String.class, "GRAY_STAINED_GLASS_PANE");
        List<String> fillerLore = config.getStringList(prefix + "gui-filler.lore");

        Material nextPageMat;
        Material prevPageMat;
        Material fillerMat;

        try {
            nextPageMat = Material.valueOf(nextPageMatStr.toUpperCase());
            prevPageMat = Material.valueOf(prevPageMatStr.toUpperCase());
            fillerMat = Material.valueOf(fillerMatStr.toUpperCase());
        } catch(IllegalArgumentException ex) {
            nextPageMat = Material.PAPER;
            prevPageMat = Material.PAPER;
            fillerMat = Material.GRAY_STAINED_GLASS_PANE;
        }

        nextPageFormat = new SimpleButtonFormat(nextPageName, nextPageMat, nextPageLore);
        previousPageFormat = new SimpleButtonFormat(prevPageName, prevPageMat, prevPageLore);
        fillerFormat = new SimpleButtonFormat(fillerName, fillerMat, fillerLore);
    }

    private void initializeGuiItemFormats() {
        String prefix = "gui-item-format.";

        String listingName = getOrDefault(prefix + "listing.name", String.class, "{item-name}");
        List<String> listingLore = config.getStringList(prefix + "listing.lore");

        String transactionName = getOrDefault(prefix + "transaction.name", String.class, "{item-name}");
        List<String> transactionLore = config.getStringList(prefix + "transaction.lore");

        listingFormat = new GuiItemFormat(listingName, listingLore);
        transactionFormat = new GuiItemFormat(transactionName, transactionLore);
    }

    private void initializeMarketplaceOptions() {
        String prefix = "marketplace-options.";

        List<Material> disallowedItems = getDisallowedItems(prefix);
        int minPrice = getOrDefault(prefix + "price.minimum", Integer.class, 10);
        int maxPrice = getOrDefault(prefix + "price.maximum", Integer.class, 0);
        int maxListings = getOrDefault(prefix + "max-listings-per-player", Integer.class, 2);

        minPrice = Math.max(1, Math.abs(minPrice)); // to avoid errors
        maxPrice = Math.max(2, Math.abs(maxPrice));

        marketplaceOptions = new MarketplaceOptions(disallowedItems, minPrice, maxPrice, maxListings);
    }

    private List<Material> getDisallowedItems(String prefix) {
        List<String> disallowedItemsStr = config.getStringList(prefix + "disallowed-items");
        if (disallowedItemsStr.isEmpty()) return new ArrayList<>();

        List<Material> disallowed = new ArrayList<>();

        for (String line : disallowedItemsStr) {
            try{
                disallowed.add(Material.valueOf(line.toUpperCase()));
            } catch(IllegalArgumentException ignored) {}
        }

        return disallowed;
    }

    private void initializeBlackMarketOptions() {
        String prefix = "black-market-options.";

        int refreshTime = getOrDefault(prefix + "refresh-time-in-minutes", Integer.class, 5);
        int itemAmount = getOrDefault(prefix + "item-amount", Integer.class, 5);
        double buyerDiscount = getOrDefault(prefix + "buyer-discount-in-percent", Double.class, 50.0);
        double sellerReimbursement = getOrDefault(prefix + "seller-reimbursement-in-percent", Double.class, 200.0);

        blackMarketOptions = new BlackMarketOptions(refreshTime, itemAmount, buyerDiscount, sellerReimbursement);
    }

    private void initializePermissions() {
        String prefix = "command-permissions.";

        sellPermission = getOrDefault
                (prefix + "sell", String.class, "marketplace.sell");
        marketplacePermission = getOrDefault
                (prefix + "marketplace", String.class, "marketplace.sell");
        blackMarketPermission = getOrDefault
                (prefix + "blackmarket", String.class, "marketplace.blackmarket");
        transactionsPermission = getOrDefault
                (prefix + "transactions", String.class, "marketplace.transactions");
        marketplaceReloadPermission = getOrDefault
                (prefix + "reload", String.class, "marketplace.reload");
    }

    private <T> T getOrDefault(String path, Class<T> clazz, T defaultValue) {
        Object obj = config.get(path);
        String defaultValString = defaultValue.toString();

        if (obj == null) {
            Bukkit.getLogger().warning("Config value not found for path " + path + "."
                    + " Returning default value: " + defaultValString);
            return defaultValue;
        }

        try {
            return clazz.cast(obj);
        }
        catch (ClassCastException e) {
            String simpleName = clazz.getSimpleName();

            Bukkit.getLogger().severe("Config item at path " + path + " cannot be cast to " + simpleName + "."
                    + " Returning default value: " + defaultValString);

            return defaultValue;
        }
    }

}
