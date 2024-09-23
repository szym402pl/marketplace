package me.xiaojibazhanshi.marketplace.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ColorUtil {

    private ColorUtil() {}

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> getColoredList(List<String> lore) {
        if (lore.isEmpty()) return lore;

        return List.copyOf(lore).stream().map(ColorUtil::color).toList();
    }

    public static ItemStack colorItemsNameAndLore(ItemStack item) {
        if (!item.hasItemMeta()) return item;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        if (meta.hasDisplayName()) {
            String coloredName = color(meta.getDisplayName());
            meta.setDisplayName(coloredName);
        }

        if (meta.getLore() != null && meta.hasLore()) { // added the first statement so the line below isn't yellow
            List<String> coloredLore = getColoredList(meta.getLore());
            meta.setLore(coloredLore);
        }

        item.setItemMeta(meta);
        return item;
    }

}
