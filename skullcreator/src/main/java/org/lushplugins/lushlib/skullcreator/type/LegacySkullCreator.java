package org.lushplugins.lushlib.skullcreator.type;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.skullcreator.SkullCreator;
import org.lushplugins.lushlib.skullcreator.SkullCreatorAPI;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;

public class LegacySkullCreator implements SkullCreator {
    private Method skullMetaSetProfileMethod;
    private Field skullMetaProfileField;

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
        try {
            if (skullMetaSetProfileMethod == null) {
                skullMetaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                skullMetaSetProfileMethod.setAccessible(true);
            }

            skullMetaSetProfileMethod.invoke(meta, makeProfile(b64));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            try {
                if (skullMetaProfileField == null) {
                    skullMetaProfileField = meta.getClass().getDeclaredField("profile");
                    skullMetaProfileField.setAccessible(true);
                }

                skullMetaProfileField.set(meta, makeProfile(b64));
            } catch (IllegalAccessException | NoSuchFieldException e2) {
                SkullCreatorAPI.LOGGER.log(Level.SEVERE, "Caught error whilst mutating skull meta: ", e);
            }
        }
    }

    @Nullable
    public String getB64(ItemStack itemStack) {
        try {
            if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof SkullMeta skullMeta) {
                if (skullMetaProfileField == null) {
                    skullMetaProfileField = skullMeta.getClass().getDeclaredField("profile");
                    skullMetaProfileField.setAccessible(true);
                }
                GameProfile gameProfile = (GameProfile) skullMetaProfileField.get(skullMeta);
                Iterator<Property> iterator = gameProfile.getProperties().get("textures").iterator();
                if (iterator.hasNext()) {
                    Property property = iterator.next();
                    return property.getValue();
                }
            }

            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    public String getTexture(OfflinePlayer player) {
        GameProfile profile;
        try {
            Method getProfileMethod = player.getClass().getDeclaredMethod("getProfile");
            profile = (GameProfile) getProfileMethod.invoke(player);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "";
        }

        Property property;
        try {
            property = profile.getProperties().get("textures").iterator().next();
        } catch(Exception e) {
            return "";
        }

        return property.getValue();
    }

    private GameProfile makeProfile(String b64) {
        UUID id = null;
        try {
            id = new UUID(b64.substring(b64.length() - 20).hashCode(), b64.substring(b64.length() - 10).hashCode());
        } catch (StringIndexOutOfBoundsException ex) {
            if (b64.length() == 0) {
                SkullCreatorAPI.LOGGER.warning("Missing base64 texture found - check your config");
            } else {
                SkullCreatorAPI.LOGGER.warning("Invalid base64 texture (" + b64 + ") found - check your config");
            }
        }

        GameProfile profile = new GameProfile(id, "Player");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }
}
