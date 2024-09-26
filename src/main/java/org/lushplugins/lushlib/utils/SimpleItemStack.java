package org.lushplugins.lushlib.utils;

import org.bukkit.Registry;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@SuppressWarnings("unused")
public class SimpleItemStack implements Cloneable {
    private static Material defaultMaterial;

    private Material material = null;
    private IntRange amount = new IntRange(1);
    private String displayName = null;
    private List<String> lore = null;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Boolean enchantGlow = null;
    private int customModelData = 0;
    private String skullTexture = null;

    public SimpleItemStack() {}

    public SimpleItemStack(@Nullable Material material) {
        this.material = material;
    }

    public SimpleItemStack(@Nullable Material material, int amount) {
        this.material = material;
        this.amount = new IntRange(amount);
    }

    public SimpleItemStack(ItemStack itemStack) {
        material = itemStack.getType();
        amount = new IntRange(itemStack.getAmount(), itemStack.getAmount());
        enchantments = itemStack.getEnchantments();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                displayName = itemMeta.getDisplayName();
            }
            if (itemMeta.hasLore()) {
                lore = itemMeta.getLore();
            }
            if (itemMeta.hasEnchants() && itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                enchantGlow = true;
            }
            if (itemMeta instanceof EnchantmentStorageMeta enchantmentMeta) {
                enchantments = enchantmentMeta.getEnchants();
            }
            if (itemMeta.hasCustomModelData()) {
                customModelData = itemMeta.getCustomModelData();
            }
            if (itemMeta instanceof SkullMeta) {
                skullTexture = SkullCreator.getB64(itemStack);
            }
        }
    }

    public SimpleItemStack(ConfigurationSection configurationSection) {
        if (configurationSection.contains("material")) {
            material = RegistryUtils.fromString(Registry.MATERIAL, configurationSection.getString("material"));
        }
        if (configurationSection.contains("amount")) {
            amount = IntRange.parseIntRange(configurationSection.getString("amount", "1"));
        }
        if (configurationSection.contains("display-name")) {
            displayName = configurationSection.getString("display-name");
        }
        if (configurationSection.contains("lore")) {
            lore = configurationSection.getStringList("lore");
        }
        if (configurationSection.contains("enchantments")) {
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            configurationSection.getConfigurationSection("enchantments").getValues(false).forEach((enchantmentRaw, level) -> {
                Enchantment enchantment = RegistryUtils.fromString(Registry.ENCHANTMENT, enchantmentRaw);
                enchantments.put(enchantment, (int) level);
            });

            this.enchantments = enchantments;
        }
        if (configurationSection.contains("enchanted")) {
            this.enchantGlow = configurationSection.getBoolean("enchanted");
        }
        if (configurationSection.contains("custom-model-data")) {
            this.customModelData = configurationSection.getInt("custom-model-data");
        }
        if (configurationSection.contains("skull-texture")) {
            this.skullTexture = configurationSection.getString("skull-texture");
        }
    }

    public SimpleItemStack(@NotNull Map<?, ?> configurationMap) {
        try {
            if (configurationMap.containsKey("material")) {
                material = RegistryUtils.fromString(Registry.MATERIAL, (String) configurationMap.get("material"));
            }
            if (configurationMap.containsKey("amount")) {
                amount = IntRange.valueOf(configurationMap.get("amount"));
            }
            if (configurationMap.containsKey("display-name")) {
                displayName = (String) configurationMap.get("display-name");
            }
            if (configurationMap.containsKey("lore")) {
                lore = (List<String>) configurationMap.get("lore");
            }
            if (configurationMap.containsKey("enchantments")) {
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                ((Map<String, Object>) configurationMap.get("enchantments")).forEach((enchantmentRaw, level) -> {
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentRaw));
                    enchantments.put(enchantment, (int) level);
                });

                this.enchantments = enchantments;
            }
            if (configurationMap.containsKey("enchanted")) {
                enchantGlow = (boolean) configurationMap.get("enchanted");
            }
            if (configurationMap.containsKey("custom-model-data")) {
                customModelData = (int) configurationMap.get("custom-model-data");
            }
            if (configurationMap.containsKey("skull-texture")) {
                skullTexture = (String) configurationMap.get("skull-texture");
            }
        } catch(ClassCastException exc) {
            throw new IllegalArgumentException("Invalid format at '" + configurationMap + "', could not parse data", exc);
        }
    }

    @Nullable
    public Material getType() {
        return material;
    }

    public boolean hasType() {
        return material != null;
    }

    public void setType(@Nullable Material material) {
        this.material = material;
    }

    public IntRange getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = new IntRange(amount);
    }

    public void setAmountRange(int minAmount, int maxAmount) {
        this.amount = new IntRange(minAmount, maxAmount);
    }

    public void setAmountRange(IntRange range) {
        this.amount = range;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    @Nullable
    public List<String> getLore() {
        return lore;
    }

    public boolean hasLore() {
        return lore != null;
    }

    public void setLore(@Nullable List<String> lore) {
        this.lore = lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public boolean hasEnchantments() {
        return !enchantments.isEmpty();
    }

    public void setEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    @Nullable
    public Boolean getEnchantGlow() {
        return enchantGlow;
    }

    public boolean hasEnchantGlow() {
        return enchantGlow != null;
    }

    public void setEnchantGlow(@Nullable Boolean enchantGlow) {
        this.enchantGlow = enchantGlow;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean hasCustomModelData() {
        return customModelData != 0;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public boolean hasSkullTexture() {
        return skullTexture != null;
    }

    public void setSkullTexture(@Nullable String texture) {
        this.skullTexture = texture;
    }

    public boolean isBlank() {
        return
            material == null
                && amount.getMin() == 1
                && amount.getMax() == 1
                && displayName == null
                && lore == null
                && enchantments.isEmpty()
                && enchantGlow == null
                && customModelData == 0
                && skullTexture == null;
    }

    public void parseColors(Player player) {
        if (hasDisplayName()) {
            displayName = ChatColorHandler.translate(displayName, player);
        }
        if (hasLore()) {
            lore = lore.stream().map(line -> ChatColorHandler.translate(line, player)).toList();
        }
    }

    public ItemStack asItemStack() {
        return asItemStack(null, true);
    }

    public ItemStack asItemStack(@Nullable Player player) {
        return asItemStack(player, true);
    }

    public ItemStack asItemStack(@Nullable Player player, boolean parseColors) {
        if (material == null && defaultMaterial != null) {
            material = defaultMaterial;
        }

        ItemStack itemStack = new ItemStack(material, amount.next());
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!(itemMeta instanceof EnchantmentStorageMeta) && !enchantments.isEmpty()) {
            enchantments.forEach((enchantment, level) -> {
                try {
                    itemStack.addEnchantment(enchantment, level);
                } catch (IllegalArgumentException ignored) {}
            });
        }

        if (itemMeta != null) {
            if (displayName != null) {
                itemMeta.setDisplayName(parseColors ? ChatColorHandler.translate(displayName, player) : displayName);
            }
            if (lore != null) {
                itemMeta.setLore(parseColors ? lore.stream().map(line -> ChatColorHandler.translate(line, player)).toList() : lore);
            }
            if (itemMeta instanceof EnchantmentStorageMeta enchantmentMeta) {
                enchantments.forEach((enchantment, level) -> enchantmentMeta.addStoredEnchant(enchantment, level, true));
            } else if (enchantments.isEmpty() && enchantGlow != null && enchantGlow) {
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if (customModelData != 0) {
                itemMeta.setCustomModelData(customModelData);
            }
            if (itemMeta instanceof SkullMeta skullMeta && skullTexture != null) {
                if (skullTexture.equals("mirror") && player != null) {
                    String playerB64 = SkullCreator.getTexture(player);
                    if (playerB64 != null) {
                        SkullCreator.mutateItemMeta(skullMeta, playerB64);
                    }
                } else {
                    SkullCreator.mutateItemMeta(skullMeta, skullTexture);
                }
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public void save(ConfigurationSection configurationSection) {
        if (material != null) {
            configurationSection.set("material", material.name());
        }
        if (amount.getMin() != 1 && amount.getMax() != 1) {
            configurationSection.set("amount", amount);
        }
        if (displayName != null) {
            configurationSection.set("display-name", displayName);
        }
        if (lore != null) {
            configurationSection.set("lore", lore);
        }
        if (!enchantments.isEmpty()) {
            Map<String, Object> enchantmentMap = new HashMap<>();
            enchantments.forEach((enchantment, level) -> enchantmentMap.put(enchantment.toString().toLowerCase(), level));
            configurationSection.set("enchantments", enchantmentMap);
        }
        if (enchantGlow != null) {
            configurationSection.set("enchanted", enchantGlow);
        }
        if (customModelData != 0) {
            configurationSection.set("custom-model-data", customModelData);
        }
        if (skullTexture != null) {
            configurationSection.set("skull-texture", skullTexture);
        }
    }

    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();

        if (material != null) {
            map.put("material", material.name());
        }
        if (amount.getMin() != 1 && amount.getMax() != 1) {
            map.put("amount", amount);
        }
        if (displayName != null) {
            map.put("display-name", displayName);
        }
        if (lore != null) {
            map.put("lore", lore);
        }
        if (!enchantments.isEmpty()) {
            Map<String, Object> enchantmentMap = new HashMap<>();
            enchantments.forEach((enchantment, level) -> enchantmentMap.put(enchantment.toString().toLowerCase(), level));
            map.put("enchantments", enchantmentMap);
        }
        if (enchantGlow != null) {
            map.put("enchanted", enchantGlow);
        }
        if (customModelData != 0) {
            map.put("custom-model-data", customModelData);
        }
        if (skullTexture != null) {
            map.put("skull-texture", skullTexture);
        }

        return map;
    }

    public SimpleItemStack overwrite(@NotNull SimpleItemStack overwrite) {
        SimpleItemStack result = new SimpleItemStack();

        if (overwrite.hasType()) {
            result.setType(overwrite.getType());
            result.setCustomModelData(overwrite.getCustomModelData());
        }
        else {
            result.setType(material);
            result.setCustomModelData(customModelData);
        }

        result.setAmountRange(overwrite.getAmount().getMin() != 1 && overwrite.getAmount().getMax() != 1 ? overwrite.getAmount() : amount);
        result.setDisplayName(overwrite.hasDisplayName() ? overwrite.getDisplayName() : displayName);
        result.setLore(overwrite.hasLore() ? overwrite.getLore() : lore);
        result.setEnchantments(overwrite.hasEnchantments() ? overwrite.getEnchantments() : enchantments);
        result.setEnchantGlow(overwrite.hasEnchantGlow() ? overwrite.getEnchantGlow() : enchantGlow);
        result.setSkullTexture(overwrite.hasSkullTexture() ? overwrite.getSkullTexture() : skullTexture);

        return result;
    }

    public SimpleItemStack overwrite(@NotNull SimpleItemStack... overwrites) {
        SimpleItemStack result = this;
        for (SimpleItemStack overwrite : overwrites) {
            result = result.overwrite(overwrite);
        }

        return result;
    }

    @Override
    public SimpleItemStack clone() {
        try {
            SimpleItemStack clone = (SimpleItemStack) super.clone();

            clone.setType(material);
            clone.setAmountRange(amount);
            clone.setDisplayName(displayName);
            clone.setLore(lore);
            clone.setEnchantments(enchantments);
            clone.setCustomModelData(customModelData);
            clone.setSkullTexture(skullTexture);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static void setDefaultMaterial(Material material) {
        defaultMaterial = material;
    }
}
