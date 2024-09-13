package me.xiaojibazhanshi.marketplace.objects;


import java.util.Set;
import java.util.UUID;


/**
 * @param uuid uuid of the player
 * @param transactions a set of (serialized) transactions made by the player
 */
public record PlayerData(UUID uuid, Set<String> transactions) {}
