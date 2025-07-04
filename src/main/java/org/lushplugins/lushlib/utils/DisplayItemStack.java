package org.lushplugins.lushlib.utils;

import com.fasterxml.jackson.annotation.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.lushlib.LushLogger;
import org.lushplugins.lushlib.skullcreator.SkullCreatorAPI;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.logging.Level;

@JsonAutoDetect(
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("unused")
public class DisplayItemStack {
    private static final DisplayItemStack EMPTY = DisplayItemStack.builder().build();

    private final Material material;
    private final IntRange amount;
    private final String displayName;
    private final List<String> lore;
    private final Boolean enchantGlow;
    private final int customModelData;
    private final String skullTexture;

    @JsonCreator
    public DisplayItemStack(
        @JsonProperty("material")
        @Nullable Material material,

        @JsonProperty("amount")
        @Nullable IntRange amount,

        @JsonProperty("displayName")
        @JsonAlias("display-name")
        @Nullable String displayName,

        @JsonProperty("lore")
        @Nullable List<String> lore,

        @JsonProperty("enchantGlow")
        @JsonAlias({"enchant-glow", "enchanted"})
        @Nullable Boolean enchantGlow,

        @JsonProperty("customModelData")
        @JsonAlias("custom-model-data")
        int customModelData,

        @JsonProperty("skullTexture")
        @JsonAlias("skull-texture")
        @Nullable String skullTexture
    ) {
        this.material = material;
        this.amount = amount != null ? amount : new IntRange(1);
        this.displayName = displayName;
        this.lore = lore != null ? Collections.unmodifiableList(lore) : null;
        this.enchantGlow = enchantGlow;
        this.customModelData = customModelData;
        this.skullTexture = skullTexture;
    }

    @Nullable
    public Material getType() {
        return material;
    }

    public boolean hasType() {
        return material != null;
    }

    public IntRange getAmount() {
        return amount.clone();
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    @Nullable
    public List<String> getLore() {
        return lore;
    }

    public boolean hasLore() {
        return lore != null;
    }

    @Nullable
    public Boolean getEnchantGlow() {
        return enchantGlow;
    }

    public boolean hasEnchantGlow() {
        return enchantGlow != null;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean hasCustomModelData() {
        return customModelData != 0;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public boolean hasSkullTexture() {
        return skullTexture != null;
    }

    public boolean isBlank() {
        return material == null
            && amount.getMin() == 1
            && amount.getMax() == 1
            && displayName == null
            && lore == null
            && enchantGlow == null
            && customModelData == 0
            && skullTexture == null;
    }

    public boolean hasMeta() {
        return this.hasDisplayName() || this.hasLore() || this.hasEnchantGlow() || this.hasCustomModelData() || this.hasSkullTexture();
    }

    public boolean isSimilar(@NotNull ItemStack itemStack) {
        Material material = this.getType();
        if (material != null && itemStack.getType() != material) {
            return false;
        }

        if (amount.getMin() != 1 || amount.getMax() != 1) {
            if (!this.getAmount().contains(itemStack.getAmount())) {
                return false;
            }
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return !this.hasMeta();
        }

        String displayName = this.getDisplayName();
        if (displayName != null && !itemMeta.getDisplayName().equals(displayName)) {
            return false;
        }

        List<String> lore = this.getLore();
        if (lore != null && (itemMeta.getLore() == null || !itemMeta.getLore().equals(lore))) {
            return false;
        }

        Boolean enchantGlow = this.getEnchantGlow();
        if (enchantGlow != null && !itemMeta.getDisplayName().equals(displayName)) {
            return false;
        }

        int customModelData = this.getCustomModelData();
        if (customModelData != 0 && (!itemMeta.hasCustomModelData() || itemMeta.getCustomModelData() != customModelData)) {
            return false;
        }

        return true;
    }

    public ItemStack asItemStack() {
        return asItemStack(null, true);
    }

    public ItemStack asItemStack(@Nullable Player player) {
        return asItemStack(player, true);
    }

    public ItemStack asItemStack(@Nullable Player player, boolean parseColors) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }

        ItemStack itemStack = new ItemStack(material, amount.next());
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (displayName != null) {
                itemMeta.setDisplayName(parseColors ? ChatColorHandler.translate(displayName, player) : displayName);
            }

            if (lore != null) {
                itemMeta.setLore(parseColors ? lore.stream().map(line -> ChatColorHandler.translate(line, player)).toList() : lore);
            }

            if (enchantGlow != null && enchantGlow) {
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (customModelData != 0) {
                itemMeta.setCustomModelData(customModelData);
            }

            if (itemMeta instanceof SkullMeta skullMeta && skullTexture != null) {
                if (skullTexture.equals("mirror") && player != null) {
                    String playerB64 = SkullCreatorAPI.getTexture(player);
                    if (playerB64 != null) {
                        SkullCreatorAPI.mutateItemMeta(skullMeta, playerB64);
                    }
                } else {
                    SkullCreatorAPI.mutateItemMeta(skullMeta, skullTexture);
                }
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static DisplayItemStack empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return builder((Material) null);
    }

    public static Builder builder(Material material) {
        return new Builder(material);
    }

    public static Builder builder(ItemStack item) {
        Builder itemBuilder = builder(item.getType())
            .setAmount(item.getAmount());

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                itemBuilder.setDisplayName(itemMeta.getDisplayName());
            }

            if (itemMeta.hasLore()) {
                itemBuilder.setLore(itemMeta.getLore());
            }

            if (itemMeta.hasEnchants()) {
                itemBuilder.setEnchantGlow(true);
            }

            if (itemMeta.hasCustomModelData()) {
                itemBuilder.setCustomModelData(itemMeta.getCustomModelData());
            }

            if (itemMeta instanceof SkullMeta) {
                itemBuilder.setSkullTexture(SkullCreatorAPI.getB64(item));
            }
        }

        return itemBuilder;
    }

    public static Builder builder(DisplayItemStack item) {
        return builder()
            .setType(item.getType())
            .setAmountRange(item.getAmount())
            .setDisplayName(item.getDisplayName())
            .setLore(item.getLore())
            .setEnchantGlow(item.getEnchantGlow())
            .setCustomModelData(item.getCustomModelData())
            .setSkullTexture(item.getSkullTexture());
    }

    public static class Builder implements Cloneable {
        private Material material;
        private IntRange amount = new IntRange(1);
        private String displayName = null;
        private List<String> lore = null;
        private Boolean enchantGlow = null;
        private int customModelData = 0;
        private String skullTexture = null;

        /**
         * @see DisplayItemStack#builder()
         */
        @Deprecated(forRemoval = true)
        public Builder() {
            this(null);
        }

        /**
         * @see DisplayItemStack#builder(Material)
         */
        @Deprecated(forRemoval = true)
        public Builder(@Nullable Material material) {
            this.material = material;
        }

        @Nullable
        public Material getType() {
            return material;
        }

        public boolean hasType() {
            return material != null;
        }

        public Builder setType(@Nullable Material material) {
            this.material = material;
            return this;
        }

        public IntRange getAmount() {
            return amount.clone();
        }

        public Builder setAmount(int amount) {
            this.amount = new IntRange(amount);
            return this;
        }

        public Builder setAmountRange(int minAmount, int maxAmount) {
            this.amount = new IntRange(minAmount, maxAmount);
            return this;
        }

        public Builder setAmountRange(IntRange range) {
            this.amount = range;
            return this;
        }

        @Nullable
        public String getDisplayName() {
            return displayName;
        }

        public boolean hasDisplayName() {
            return displayName != null;
        }

        public Builder setDisplayName(@Nullable String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Nullable
        public List<String> getLore() {
            return lore != null ? Collections.unmodifiableList(lore) : null;
        }

        public boolean hasLore() {
            return lore != null;
        }

        public Builder setLore(@Nullable List<String> lore) {
            this.lore = lore;
            return this;
        }

        @Nullable
        public Boolean getEnchantGlow() {
            return enchantGlow;
        }

        public boolean hasEnchantGlow() {
            return enchantGlow != null;
        }

        public Builder setEnchantGlow(@Nullable Boolean enchantGlow) {
            this.enchantGlow = enchantGlow;
            return this;
        }

        public int getCustomModelData() {
            return customModelData;
        }

        public boolean hasCustomModelData() {
            return customModelData != 0;
        }

        public Builder setCustomModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public String getSkullTexture() {
            return skullTexture;
        }

        public boolean hasSkullTexture() {
            return skullTexture != null;
        }

        public Builder setSkullTexture(@Nullable String texture) {
            this.skullTexture = texture;
            return this;
        }

        public Builder replace(CharSequence target, CharSequence replacement) {
            this.displayName = this.displayName.replace(target, replacement);
            this.lore = this.lore.stream()
                .map(line -> line.replace(target, replacement))
                .toList();

            return this;
        }

        public Builder replace(CharSequence target, Callable<String> callableReplacement) {
            if (!this.displayName.contains(target) && this.lore.stream().noneMatch(str -> str.contains(target))) {
                return this;
            }

            try {
                String replacement = callableReplacement.call();
                return replace(target, replacement);
            } catch (Exception e) {
                LushLogger.getLogger().log(Level.SEVERE, "Caught exception whilst parsing replacement: ", e);
            }

            return this;
        }

        public Builder replace(Function<String, String> replacer) {
            this.displayName = replacer.apply(this.displayName);
            this.lore = this.lore.stream()
                .map(replacer)
                .toList();

            return this;
        }

        public DisplayItemStack build() {
            return new DisplayItemStack(
                material,
                amount,
                displayName,
                lore != null ? new ArrayList<>(lore) : null,
                enchantGlow,
                customModelData,
                skullTexture
            );
        }

        public boolean isBlank() {
            return material == null
                && amount.getMin() == 1
                && amount.getMax() == 1
                && displayName == null
                && lore == null
                && enchantGlow == null
                && customModelData == 0
                && skullTexture == null;
        }

        public Builder parseColors(Player player) {
            if (hasDisplayName()) {
                displayName = ChatColorHandler.translate(displayName, player);
            }

            if (hasLore()) {
                lore = lore.stream().map(line -> ChatColorHandler.translate(line, player)).toList();
            }

            return this;
        }

        public Builder overwrite(@NotNull Builder overwrite) {
            Builder result = new Builder();

            if (overwrite.hasType()) {
                result.setType(overwrite.getType());
                result.setCustomModelData(overwrite.getCustomModelData());
            } else {
                result.setType(material);
                result.setCustomModelData(customModelData);
            }

            result.setAmountRange(overwrite.getAmount().getMin() != 1 && overwrite.getAmount().getMax() != 1 ? overwrite.getAmount() : amount);
            result.setDisplayName(overwrite.hasDisplayName() ? overwrite.getDisplayName() : displayName);
            result.setLore(overwrite.hasLore() ? overwrite.getLore() : lore);
            result.setEnchantGlow(overwrite.hasEnchantGlow() ? overwrite.getEnchantGlow() : enchantGlow);
            result.setSkullTexture(overwrite.hasSkullTexture() ? overwrite.getSkullTexture() : skullTexture);

            return result;
        }

        public Builder overwrite(@NotNull Builder... overwrites) {
            Builder result = this;
            for (Builder overwrite : overwrites) {
                result = result.overwrite(overwrite);
            }

            return result;
        }

        @Override
        public Builder clone() {
            try {
                Builder clone = (Builder) super.clone();

                clone.setAmountRange(amount.clone());
                clone.setLore(lore != null ? new ArrayList<>(lore) : null);

                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        /**
         * @see DisplayItemStack#builder(DisplayItemStack)
         */
        @Deprecated(forRemoval = true)
        public static Builder of(DisplayItemStack item) {
            return builder(item);
        }

        /**
         * @see DisplayItemStack#builder(ItemStack)
         */
        @Deprecated(forRemoval = true)
        public static Builder of(ItemStack item) {
            return builder(item);
        }
    }
}
