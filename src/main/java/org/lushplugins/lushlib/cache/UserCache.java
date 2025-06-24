package org.lushplugins.lushlib.cache;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class UserCache<T> {
    private final Map<UUID, T> userCache = new HashMap<>();

    public UserCache(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener<>(this), plugin);
    }

    public @Nullable T getCachedUser(UUID uuid) {
        return this.userCache.get(uuid);
    }

    public CompletableFuture<T> getUser(UUID uuid) {
        return getUser(uuid, true);
    }

    public CompletableFuture<T> getUser(UUID uuid, boolean cache) {
        T user = this.userCache.get(uuid);
        return user != null ? CompletableFuture.completedFuture(user) : loadUser(uuid, cache);
    }

    protected abstract CompletableFuture<T> load(UUID uuid);

    public CompletableFuture<T> loadUser(UUID uuid) {
        return loadUser(uuid, true);
    }

    public CompletableFuture<T> loadUser(UUID uuid, boolean cache) {
        return load(uuid).thenApply(user -> {
            if (cache) {
                userCache.put(uuid, user);
            }

            return user;
        });
    }

    public void unloadUser(UUID uuid) {
        userCache.remove(uuid);
    }

    public static class Listener<T> implements org.bukkit.event.Listener {
        private final UserCache<T> cache;

        public Listener(UserCache<T> cache) {
            this.cache = cache;
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            this.cache.loadUser(event.getPlayer().getUniqueId(), true);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            this.cache.unloadUser(event.getPlayer().getUniqueId());
        }
    }
}