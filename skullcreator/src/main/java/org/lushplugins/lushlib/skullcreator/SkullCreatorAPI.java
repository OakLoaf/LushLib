package org.lushplugins.lushlib.skullcreator;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.lushplugins.lushlib.skullcreator.type.LegacySkullCreator;
import org.lushplugins.lushlib.skullcreator.type.ModernSkullCreator;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class SkullCreatorAPI {
    private static final SkullCreator INSTANCE;
    public static final Logger LOGGER = Logger.getLogger("SkullCreator");

    static {
        String version = Bukkit.getBukkitVersion();
        if (version.contains("1.16") || version.contains("1.17") || version.contains("1.18")) {
            INSTANCE = new LegacySkullCreator();
        } else {
            INSTANCE = new ModernSkullCreator();
        }
    }

    public static ItemStack getCustomSkull(String texture) {
        return INSTANCE.getCustomSkull(texture);
    }

    public static void mutateItemMeta(SkullMeta meta, String b64) {
        INSTANCE.mutateItemMeta(meta, b64);
    }

    public static String getB64(ItemStack itemStack) {
        return INSTANCE.getB64(itemStack);
    }

    public static String getTexture(OfflinePlayer player) {
        return INSTANCE.getTexture(player);
    }
}
