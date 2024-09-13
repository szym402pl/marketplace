package me.xiaojibazhanshi.marketplace.objects;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;


/**
 * @param item item in the listing
 * @param sellerUUID uuid of the item seller
 * @param price price of the listed item
 * @param listedOn date on which the listing was put up, converted to milliseconds
 */
public record Listing(ItemStack item, UUID sellerUUID, double price, long listedOn) {}
