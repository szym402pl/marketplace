package me.xiaojibazhanshi.marketplace.objects.helper;

import org.bukkit.Material;

import java.util.List;

public record MarketplaceOptions(List<Material> disallowedItems, int minPrice, int maxPrice, int maxListingPerPlayer) {}
