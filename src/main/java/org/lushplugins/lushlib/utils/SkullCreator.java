package org.lushplugins.lushlib.utils;

import org.bukkit.OfflinePlayer;
import org.lushplugins.lushlib.utils.skullcreator.LegacySkullCreator;
import org.lushplugins.lushlib.utils.skullcreator.NewSkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("unused")
public class SkullCreator {
    private static final SkullCreator.Interface skullCreator;

    static {
        String version = Bukkit.getBukkitVersion();
        if (version.contains("1.16") || version.contains("1.17") || version.contains("1.18")) {
            skullCreator = new LegacySkullCreator();
        } else {
            skullCreator = new NewSkullCreator();
        }
    }

    public static ItemStack getCustomSkull(String texture) {
        return skullCreator.getCustomSkull(texture);
    }

    public static void mutateItemMeta(SkullMeta meta, String b64) {
        skullCreator.mutateItemMeta(meta, b64);
    }

    public static String getB64(ItemStack itemStack) {
        return skullCreator.getB64(itemStack);
    }

    public static String getTexture(Player player) {
        return skullCreator.getTexture(player);
    }

    public interface Interface {
        ItemStack getCustomSkull(String texture);

        void mutateItemMeta(SkullMeta meta, String b64);

        String getB64(ItemStack itemStack);

        String getTexture(OfflinePlayer player);
    }
}
