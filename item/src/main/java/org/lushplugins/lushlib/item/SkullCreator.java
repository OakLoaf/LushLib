package org.lushplugins.lushlib.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SkullCreator {
    public static final Logger LOGGER = Logger.getLogger("SkullCreator");

    public static ItemStack getCustomSkull(String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        if (item.getItemMeta() instanceof SkullMeta meta) {
            mutateItemMeta(meta, texture);
            item.setItemMeta(meta);
            return item;
        } else {
            return null;
        }
    }

    public static void mutateItemMeta(SkullMeta meta, String b64) {
        PlayerProfile ownerProfile = meta.getPlayerProfile() != null ? meta.getPlayerProfile() : makeProfile(b64);
        meta.setPlayerProfile(ownerProfile);
    }

    public static @Nullable String getTextureValue(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return null;
        }

        if (!(itemStack.getItemMeta() instanceof SkullMeta skullMeta)) {
            return null;
        }

        PlayerProfile ownerProfile = skullMeta.getPlayerProfile();
        if (ownerProfile == null) {
            return null;
        }

        try {
            URL skinUrl = skullMeta.getPlayerProfile().getTextures().getSkin();
            return skinUrl != null ? getBase64FromUrl(skinUrl) : null;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Caught error whilst parsing skull item:", e);
            return null;
        }
    }

    public static String getTexture(OfflinePlayer player) {
        URL skinUrl = player.getPlayerProfile().getTextures().getSkin();
        return skinUrl != null ? getBase64FromUrl(skinUrl) : null;
    }

    private static PlayerProfile makeProfile(String b64) {
        UUID id = null;
        try {
            id = new UUID(b64.substring(b64.length() - 20).hashCode(), b64.substring(b64.length() - 10).hashCode());
        } catch (StringIndexOutOfBoundsException ex) {
            if (b64.isEmpty()) {
                LOGGER.warning("Missing base64 texture found - check your config");
            } else {
                LOGGER.warning("Invalid base64 texture (" + b64 + ") found - check your config");
            }
        }

        PlayerProfile profile = Bukkit.createProfile(id, "Player");
        try {
            PlayerTextures profileTextures = profile.getTextures();
            profileTextures.setSkin(getUrlFromBase64(b64));
            profile.setTextures(profileTextures);
        } catch (MalformedURLException | NullPointerException e) {
            e.printStackTrace();
        }

        return profile;
    }

    private static URL getUrlFromBase64(String base64) throws MalformedURLException {
        String dataRaw = new String(Base64.getDecoder().decode(base64)).toLowerCase();
        JsonObject data = JsonParser.parseString(dataRaw).getAsJsonObject();

        String decoded;
        try {
            decoded = data.get("textures").getAsJsonObject().get("skin").getAsJsonObject().get("url").getAsString();
        } catch (NullPointerException e) {
            LOGGER.severe(base64 + " does not appear to be a valid texture.");
            return null;
        }

        return URI.create(decoded).toURL();
    }

    private static String getBase64FromUrl(URL url) {
        byte[] urlBytes = ("{textures:{skin:{url:\"" + url.toString() + "\"}}}").getBytes();
        return new String(Base64.getEncoder().encode(urlBytes));
    }
}
