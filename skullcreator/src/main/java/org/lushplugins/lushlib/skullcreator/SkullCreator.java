package org.lushplugins.lushlib.skullcreator;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public interface SkullCreator {

    ItemStack getCustomSkull(String texture);

    void mutateItemMeta(SkullMeta meta, String b64);

    String getB64(ItemStack itemStack);

    String getTexture(OfflinePlayer player);
}
