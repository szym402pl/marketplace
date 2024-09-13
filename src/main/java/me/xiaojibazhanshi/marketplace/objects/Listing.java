package me.xiaojibazhanshi.marketplace.objects;

import me.xiaojibazhanshi.marketplace.utils.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * @param item item in the listing
 * @param sellerUUID uuid of the item seller
 * @param price price of the listed item
 * @param listedOn date on which the listing was put up, converted to milliseconds
 * @Function serialize() - returns the serialized listing
 * @Function deserialize(String data) - (returns the listing deserialized from provided data)
 */
public record Listing(UUID sellerUUID, ItemStack item, double price, long listedOn) {

    private String serializedItem() {
        return ItemStackSerializer.serializeItemToBase64(item);
    }

    public String serialize() {
        return sellerUUID.toString() + ";"
                + serializedItem() + ";"
                + price + ";"
                + listedOn;
    }

    public static Listing deserialize(@NotNull String data) {
        String[] parts = data.split(";");
        UUID sellerUUID = UUID.fromString(parts[0]);
        ItemStack item = ItemStackSerializer.deserializeItemFromBase64(parts[1]);
        double price = Double.parseDouble(parts[2]);
        long listedOn = Long.parseLong(parts[3]);

        return new Listing(sellerUUID, item, price, listedOn);
    }

}
