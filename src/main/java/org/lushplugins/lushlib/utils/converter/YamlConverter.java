package org.lushplugins.lushlib.utils.converter;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.lushlib.utils.DisplayItemStack;
import org.lushplugins.lushlib.utils.IntRange;
import org.lushplugins.lushlib.utils.RegistryUtils;

import java.util.List;

public class YamlConverter {

    public static DisplayItemStack.Builder getDisplayItemBuilder(ConfigurationSection section) {
        DisplayItemStack.Builder itemBuilder = DisplayItemStack.builder();

        if (section.contains("material")) {
            itemBuilder.setType(RegistryUtils.fromString(Registry.MATERIAL, section.getString("material")));
        }
        if (section.contains("amount")) {
            itemBuilder.setAmountRange(IntRange.parseIntRange(section.getString("amount", "1")));
        }
        if (section.contains("display-name")) {
            itemBuilder.setDisplayName(section.getString("display-name"));
        }
        if (section.contains("lore")) {
            itemBuilder.setLore(section.getStringList("lore"));
        }
        if (section.contains("enchanted")) {
            itemBuilder.setEnchantGlow(section.getBoolean("enchanted"));
        }
        if (section.contains("custom-model-data")) {
            itemBuilder.setCustomModelData(section.getInt("custom-model-data"));
        }
        if (section.contains("skull-texture")) {
            itemBuilder.setSkullTexture(section.getString("skull-texture"));
        }

        return itemBuilder;
    }

    public static DisplayItemStack getDisplayItem(ConfigurationSection section) {
        return getDisplayItemBuilder(section).build();
    }

    public static void setDisplayItem(ConfigurationSection section, DisplayItemStack item) {
        Material material = item.getType();
        if (material != null) {
            section.set("material", material.name());
        }

        IntRange amount = item.getAmount();
        if (amount.getMin() != 1 && amount.getMax() != 1) {
            section.set("amount", amount);
        }

        String displayName = item.getDisplayName();
        if (displayName != null) {
            section.set("display-name", displayName);
        }

        List<String> lore = item.getLore();
        if (lore != null) {
            section.set("lore", lore);
        }

        Boolean enchantGlow = item.getEnchantGlow();
        if (enchantGlow != null) {
            section.set("enchanted", enchantGlow);
        }

        int customModelData = item.getCustomModelData();
        if (customModelData != 0) {
            section.set("custom-model-data", customModelData);
        }

        String skullTexture = item.getSkullTexture();
        if (skullTexture != null) {
            section.set("skull-texture", skullTexture);
        }
    }
}
