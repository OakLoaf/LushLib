package org.lushplugins.lushlib.utils;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class SimpleItemStack implements Cloneable {
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
        return asItemStack(null);
    }

    public ItemStack asItemStack(@Nullable Player player) {
        ItemStack itemStack = new ItemStack(material, amount.next());
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!enchantments.isEmpty()) {
            enchantments.forEach(itemStack::addEnchantment);
        }

        if (itemMeta != null) {
            if (displayName != null) {
                itemMeta.setDisplayName(displayName);
            }
            if (lore != null) {
                itemMeta.setLore(lore);
            }
            if (enchantments.isEmpty() && enchantGlow != null && enchantGlow) {
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
            configurationSection.set("enchantments", enchantments);
        }
        if (enchantGlow != null) {
            Map<String, Object> enchantmentMap = new HashMap<>();
            enchantments.forEach((enchantment, level) -> enchantmentMap.put(enchantment.toString().toLowerCase(), level));
            configurationSection.set("enchanted", enchantmentMap);
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

    public static SimpleItemStack overwrite(@NotNull SimpleItemStack original, @NotNull SimpleItemStack overwrite) {
        SimpleItemStack result = new SimpleItemStack();

        if (overwrite.hasType()) {
            result.setType(overwrite.getType());
            result.setCustomModelData(overwrite.getCustomModelData());
        }
        else {
            result.setType(original.getType());
            result.setCustomModelData(original.getCustomModelData());
        }

        result.setAmountRange(overwrite.getAmount().getMin() != 1 && overwrite.getAmount().getMax() != 1 ? overwrite.getAmount() : original.getAmount());
        result.setDisplayName(overwrite.hasDisplayName() ? overwrite.getDisplayName() : original.getDisplayName());
        result.setLore(overwrite.hasLore() ? overwrite.getLore() : original.getLore());
        result.setEnchantments(overwrite.hasEnchantments() ? overwrite.getEnchantments() : original.getEnchantments());
        result.setEnchantGlow(overwrite.hasEnchantGlow() ? overwrite.getEnchantGlow() : original.getEnchantGlow());
        result.setSkullTexture(overwrite.hasSkullTexture() ? overwrite.getSkullTexture() : original.getSkullTexture());

        return result;
    }

    public static SimpleItemStack overwrite(@NotNull SimpleItemStack original, @NotNull SimpleItemStack... overwrites) {
        SimpleItemStack result = original;

        for (SimpleItemStack overwrite : overwrites) {
            result = overwrite(result, overwrite);
        }

        return result;
    }

    public static SimpleItemStack from(@NotNull ItemStack itemStack) {
        SimpleItemStack simpleItemStack = new SimpleItemStack();
        simpleItemStack.setType(itemStack.getType());
        simpleItemStack.setAmount(itemStack.getAmount());
        simpleItemStack.setEnchantments(itemStack.getEnchantments());

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                simpleItemStack.setDisplayName(itemMeta.getDisplayName());
            }
            if (itemMeta.hasLore()) {
                simpleItemStack.setLore(itemMeta.getLore());
            }
            if (itemMeta.hasEnchants() && itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                simpleItemStack.setEnchantGlow(true);
            }
            if (itemMeta.hasCustomModelData()) {
                simpleItemStack.setCustomModelData(itemMeta.getCustomModelData());
            }
            if (itemMeta instanceof SkullMeta) {
                simpleItemStack.setSkullTexture(SkullCreator.getB64(itemStack));
            }
        }
        return simpleItemStack;
    }

    public static SimpleItemStack from(@NotNull ConfigurationSection configurationSection) {
        SimpleItemStack simpleItemStack = new SimpleItemStack();

        if (configurationSection.contains("material")) {
            StringUtils.getEnum(configurationSection.getString("material", null), Material.class).ifPresent(simpleItemStack::setType);
        }
        if (configurationSection.contains("amount")) {
            simpleItemStack.setAmountRange(IntRange.parseIntRange(configurationSection.getString("amount", "1")));
        }
        if (configurationSection.contains("display-name")) {
            simpleItemStack.setDisplayName(configurationSection.getString("display-name", null));
        }
        if (configurationSection.contains("lore")) {
            simpleItemStack.setLore(configurationSection.getStringList("lore"));
        }
        if (configurationSection.contains("enchantments")) {
            Map<Enchantment, Integer> enchantments = new HashMap<>();

            configurationSection.getConfigurationSection("enchantments").getValues(false).forEach((enchantmentRaw, level) -> {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentRaw));
                enchantments.put(enchantment, (int) level);
            });

            simpleItemStack.setEnchantments(enchantments);
        }
        if (configurationSection.contains("enchanted")) {
            simpleItemStack.setEnchantGlow(configurationSection.getBoolean("enchanted", false));
        }
        if (configurationSection.contains("custom-model-data")) {
            simpleItemStack.setCustomModelData(configurationSection.getInt("custom-model-data"));
        }
        if (configurationSection.contains("skull-texture")) {
            simpleItemStack.setSkullTexture(configurationSection.getString("skull-texture", null));
        }

        return simpleItemStack;
    }

    @SuppressWarnings("unchecked")
    public static SimpleItemStack from(@NotNull Map<?, ?> configurationMap) {
        SimpleItemStack simpleItemStack = new SimpleItemStack();

        try {
            if (configurationMap.containsKey("material")) {
                StringUtils.getEnum((String) configurationMap.get("material"), Material.class).ifPresent(simpleItemStack::setType);
            }
            if (configurationMap.containsKey("amount")) {
                simpleItemStack.setAmountRange(IntRange.valueOf(configurationMap.get("amount")));
            }
            if (configurationMap.containsKey("display-name")) {
                simpleItemStack.setDisplayName((String) configurationMap.get("display-name"));
            }
            if (configurationMap.containsKey("lore")) {
                simpleItemStack.setLore((List<String>) configurationMap.get("lore"));
            }
            if (configurationMap.containsKey("enchantments")) {
                Map<Enchantment, Integer> enchantments = new HashMap<>();

                ((Map<String, Object>) configurationMap.get("enchantments")).forEach((enchantmentRaw, level) -> {
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentRaw));
                    enchantments.put(enchantment, (int) level);
                });

                simpleItemStack.setEnchantments(enchantments);
            }
            if (configurationMap.containsKey("enchanted")) {
                simpleItemStack.setEnchantGlow((boolean) configurationMap.get("enchanted"));
            }
            if (configurationMap.containsKey("custom-model-data")) {
                simpleItemStack.setCustomModelData((int) configurationMap.get("custom-model-data"));
            }
            if (configurationMap.containsKey("skull-texture")) {
                simpleItemStack.setSkullTexture((String) configurationMap.get("skull-texture"));
            }
        } catch(ClassCastException exc) {
            throw new IllegalArgumentException("Invalid format at '" + configurationMap + "', could not parse data", exc);
        }

        return simpleItemStack;
    }

    @Override
    public SimpleItemStack clone() {
        try {
            return (SimpleItemStack) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
