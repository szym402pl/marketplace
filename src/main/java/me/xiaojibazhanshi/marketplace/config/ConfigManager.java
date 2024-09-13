package me.xiaojibazhanshi.marketplace.config;

import lombok.AccessLevel;
import lombok.Getter;
import me.xiaojibazhanshi.marketplace.Marketplace;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import static me.xiaojibazhanshi.marketplace.config.PathPrefixes.CMD_PERMS;

@Getter
public class ConfigManager {

    // TODO: Add all the config values
    // TODO: Ensure validity of all config values

    @Getter(AccessLevel.NONE) private FileConfiguration config;
    @Getter(AccessLevel.NONE) private final Marketplace main;

    /* Permission variables */

    @Getter(AccessLevel.NONE) private final String permsPrefix = CMD_PERMS.getPath();

    private String sellPermission;
    private String marketplacePermission;
    private String blackMarketPermission;
    private String transactionsPermission;
    private String marketplaceReloadPermission;

    /* Constructor */

    public ConfigManager(Marketplace main) {
        this.main = main;
        this.config = this.main.getConfig();

        initializeVariables();
    }

    /* Main methods */

    private void initializeVariables() {
        initializePermissions();
    }

    public void reload() {
        main.reloadConfig();
        config = main.getConfig();

        initializeVariables();
    }

    /* Helper methods */

    private void initializePermissions() {
        sellPermission = getOrDefaultWithLogging
                (permsPrefix + ".sell", String.class, "marketplace.sell");

        marketplacePermission = getOrDefaultWithLogging
                (permsPrefix + ".marketplace", String.class, "marketplace.sell");

        blackMarketPermission = getOrDefaultWithLogging
                (permsPrefix + ".blackmarket", String.class, "marketplace.blackmarket");

        transactionsPermission = getOrDefaultWithLogging
                (permsPrefix + ".transactions", String.class, "marketplace.transactions");

        marketplaceReloadPermission = getOrDefaultWithLogging
                (permsPrefix + ".reload", String.class, "marketplace.reload");
    }

    private <T> T getOrDefaultWithLogging(String path, Class<T> clazz, T defaultValue) {
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
