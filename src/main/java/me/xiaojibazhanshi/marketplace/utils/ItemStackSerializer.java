package me.xiaojibazhanshi.marketplace.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ItemStackSerializer {

    // this legit took me an hour to figure out

    private ItemStackSerializer() {}

    public static ItemStack deserializeItemFromBase64(String base64) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            Object obj = objectInputStream.readObject();

            if (!(obj instanceof Map<?, ?> map)) {
                throw new ClassCastException("Deserialized item is not a Map (Item wasn't serialized properly)!");
            }

            if (!map.keySet().stream().allMatch(key -> key instanceof String)) {
                throw new ClassCastException("Serialized map keys are not of type String!");
            }

            Map<String, Object> itemMap = new HashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    throw new ClassCastException("Serialized map contains non-String keys!");
                }

                itemMap.put((String) entry.getKey(), entry.getValue());
            }

            return ItemStack.deserialize(itemMap);
        }
        catch (IOException | ClassNotFoundException e) {
            Bukkit.getLogger().severe("[Marketplace] Error: An ItemStack couldn't be deserialized from a String!");
            return null;
        }
    }

    public static String serializeItemToBase64(ItemStack itemStack) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(itemStack.serialize());
            objectOutputStream.flush();

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }
        catch (IOException e) {
            Bukkit.getLogger().severe("[Marketplace] Error: An ItemStack couldn't be serialized into a String!");
            return null;
        }
    }


}
