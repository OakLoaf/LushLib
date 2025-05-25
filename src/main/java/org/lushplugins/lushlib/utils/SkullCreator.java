package org.lushplugins.lushlib.utils;

import org.bukkit.OfflinePlayer;
import org.lushplugins.lushlib.skullcreator.SkullCreatorAPI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @see SkullCreatorAPI
 */
@Deprecated
@SuppressWarnings("unused")
public class SkullCreator {

    public static ItemStack getCustomSkull(String texture) {
        return SkullCreatorAPI.getCustomSkull(texture);
    }

    public static void mutateItemMeta(SkullMeta meta, String b64) {
        SkullCreatorAPI.mutateItemMeta(meta, b64);
    }

    public static String getB64(ItemStack item) {
        return SkullCreatorAPI.getB64(item);
    }

    public static String getTexture(OfflinePlayer player) {
        return SkullCreatorAPI.getTexture(player);
    }
}
