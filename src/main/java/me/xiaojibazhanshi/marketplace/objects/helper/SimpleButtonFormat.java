package me.xiaojibazhanshi.marketplace.objects.helper;

import org.bukkit.Material;

import java.util.List;

public record SimpleButtonFormat(String name, Material material, List<String> lore) {
}
