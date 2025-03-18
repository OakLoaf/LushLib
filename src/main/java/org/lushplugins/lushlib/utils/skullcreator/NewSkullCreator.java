package org.lushplugins.lushlib.utils.skullcreator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.LushLogger;
import org.lushplugins.lushlib.utils.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;

public class NewSkullCreator implements SkullCreator.Interface {

    public ItemStack getCustomSkull(String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        if (item.getItemMeta() instanceof SkullMeta meta) {
            mutateItemMeta(meta, texture);
            item.setItemMeta(meta);
            return item;
        } else {
            return null;
        }
    }

    public void mutateItemMeta(SkullMeta meta, String b64) {
        PlayerProfile ownerProfile = meta.getOwnerProfile() != null ? meta.getOwnerProfile() : makeProfile(b64);
        meta.setOwnerProfile(ownerProfile);
    }

    @Deprecated
    public @Nullable String getB64(@NotNull ItemStack itemStack) {
        return getTextureValue(itemStack);
    }

    public @Nullable String getTextureValue(@NotNull ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return null;
        }

        if (!(itemStack.getItemMeta() instanceof SkullMeta skullMeta)) {
            return null;
        }

        PlayerProfile ownerProfile = skullMeta.getOwnerProfile();
        if (ownerProfile == null) {
            return null;
        }

        try {
            URL skinUrl = skullMeta.getOwnerProfile().getTextures().getSkin();
            return skinUrl != null ? getBase64FromUrl(skinUrl) : null;
        } catch (Exception e) {
            LushLogger.getLogger().log(Level.WARNING, "Caught error whilst parsing skull item:", e);
            return null;
        }
    }

    public String getTexture(OfflinePlayer player) {
        URL skinUrl = player.getPlayerProfile().getTextures().getSkin();
        return skinUrl != null ? getBase64FromUrl(skinUrl) : null;
    }

    private PlayerProfile makeProfile(String b64) {
        UUID id = null;
        try {
            id = new UUID(b64.substring(b64.length() - 20).hashCode(), b64.substring(b64.length() - 10).hashCode());
        } catch (StringIndexOutOfBoundsException ex) {
            if (b64.length() == 0) {
                LushLogger.getLogger().warning("Missing base64 texture found - check your config");
            } else {
                LushLogger.getLogger().warning("Invalid base64 texture (" + b64 + ") found - check your config");
            }
        }

        PlayerProfile profile = Bukkit.createPlayerProfile(id, "Player");
        try {
            PlayerTextures profileTextures = profile.getTextures();
            profileTextures.setSkin(getUrlFromBase64(b64));
            profile.setTextures(profileTextures);
        } catch (MalformedURLException | NullPointerException e) {
            e.printStackTrace();
        }

        return profile;
    }

    private URL getUrlFromBase64(String base64) throws MalformedURLException {
        String dataRaw = new String(Base64.getDecoder().decode(base64)).toLowerCase();
        JsonObject data = JsonParser.parseString(dataRaw).getAsJsonObject();

        String decoded;
        try {
            decoded = data.get("textures").getAsJsonObject().get("skin").getAsJsonObject().get("url").getAsString();
        } catch (NullPointerException e) {
            LushLogger.getLogger().severe(base64 + " does not appear to be a valid texture.");
            return null;
        }

        return new URL(decoded);
    }

    private String getBase64FromUrl(URL url) {
        byte[] urlBytes = ("{textures:{skin:{url:\"" + url.toString() + "\"}}}").getBytes();
        return new String(Base64.getEncoder().encode(urlBytes));
    }
}
