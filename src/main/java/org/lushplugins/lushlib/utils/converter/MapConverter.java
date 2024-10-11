package org.lushplugins.lushlib.utils.converter;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.lushplugins.lushlib.utils.DisplayItemStack;
import org.lushplugins.lushlib.utils.IntRange;
import org.lushplugins.lushlib.utils.LushLogger;
import org.lushplugins.lushlib.utils.RegistryUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MapConverter {

    public static DisplayItemStack.Builder getDisplayItemBuilder(Map<?, ?> map) {
        DisplayItemStack.Builder itemBuilder = DisplayItemStack.builder();

        if (map.containsKey("material")) {
            itemBuilder.setType(RegistryUtils.fromString(Registry.MATERIAL, getObject(map, "material", String.class)));
        }
        if (map.containsKey("amount")) {
            itemBuilder.setAmountRange(IntRange.parseIntRange(getObjectOrDefault(map, "amount", "1", String.class)));
        }
        if (map.containsKey("display-name")) {
            itemBuilder.setDisplayName(getObject(map, "display-name", String.class));
        }
        if (map.containsKey("lore")) {
            itemBuilder.setLore((List<String>) map.get("lore"));
        }
        if (map.containsKey("enchanted")) {
            itemBuilder.setEnchantGlow(getObject(map, "enchanted", Boolean.class));
        }
        if (map.containsKey("custom-model-data")) {
            itemBuilder.setCustomModelData(getObject(map, "custom-model-data", Integer.class));
        }
        if (map.containsKey("skull-texture")) {
            itemBuilder.setSkullTexture(getObject(map, "skull-texture", String.class));
        }

        return itemBuilder;
    }

    public static DisplayItemStack getDisplayItem(Map<?, ?> map) {
        return getDisplayItemBuilder(map).build();
    }

    public static Map<String, Object> getDisplayItemAsMap(DisplayItemStack item) {
        Map<String, Object> map = new HashMap<>();
        
        Material material = item.getType();
        if (material != null) {
            map.put("material", material.name());
        }

        IntRange amount = item.getAmount();
        if (amount.getMin() != 1 && amount.getMax() != 1) {
            map.put("amount", amount);
        }

        String displayName = item.getDisplayName();
        if (displayName != null) {
            map.put("display-name", displayName);
        }

        List<String> lore = item.getLore();
        if (lore != null) {
            map.put("lore", lore);
        }

        Boolean enchantGlow = item.getEnchantGlow();
        if (enchantGlow != null) {
            map.put("enchanted", enchantGlow);
        }

        int customModelData = item.getCustomModelData();
        if (customModelData != 0) {
            map.put("custom-model-data", customModelData);
        }

        String skullTexture = item.getSkullTexture();
        if (skullTexture != null) {
            map.put("skull-texture", skullTexture);
        }

        return map;
    }

    private static boolean contains(Map<?, ?> map, String path, Class<?> clazz) {
        return getObject(map, path, clazz) != null;
    }

    private static <T> T getObject(Map<?, ?> map, String path, Class<T> clazz) {
        try {
            return clazz.cast(map.get(path));
        } catch (ClassCastException e) {
            LushLogger.getLogger().log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    private static <T> T getObjectOrDefault(Map<?, ?> map, String path, T def, Class<T> clazz) {
        T value = getObject(map, path, clazz);
        return value != null ? value : def;
    }
}
