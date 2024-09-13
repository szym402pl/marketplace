package me.xiaojibazhanshi.marketplace.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record PlayerData(UUID uuid, Set<Transaction> transactions) {}
