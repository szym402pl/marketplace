package me.xiaojibazhanshi.marketplace.objects;

import java.util.UUID;

/**
 * @param buyerUUID the uuid of the item buyer
 * @param listing the involved item listing - view {@link Listing}
 * @param boughtOn date on which the listing item was bought converted to milliseconds
 */
public record Transaction(UUID buyerUUID, Listing listing, long boughtOn) {}
