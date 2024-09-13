package me.xiaojibazhanshi.marketplace.objects;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @param buyerUUID the uuid of the item buyer
 * @param listing the involved item listing - view {@link Listing}
 * @param boughtOn date on which the listing item was bought converted to milliseconds
 * @Function serialize() - returns the serialized transaction
 * @Function deserialize(String data) - returns the transaction (deserialized from provided data)
 */
public record Transaction(UUID buyerUUID, Listing listing, long boughtOn) {

    public String serialize() { // using different serialization strategy, so it doesn't conflict with listing's
        return buyerUUID.toString() + "|"
                + listing.serialize() + "|"
                + boughtOn;
    }

    public static Transaction deserialize(@NotNull String data) {
        String[] parts = data.split("\\|");

        UUID buyerUUID = UUID.fromString(parts[0]);
        Listing listing = Listing.deserialize(parts[1]);
        long boughtOn = Long.parseLong(parts[2]);

        return new Transaction(buyerUUID, listing, boughtOn);
    }

}
