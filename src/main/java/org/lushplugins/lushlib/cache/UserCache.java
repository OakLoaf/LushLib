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
    protected final Map<UUID, T> userCache;

    public UserCache(JavaPlugin plugin, Map<UUID, T> cache, @Nullable org.bukkit.event.Listener listener) {
        this.userCache = cache;
        if (listener != null) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public UserCache(JavaPlugin plugin, Map<UUID, T> cache) {
        this.userCache = cache;
        plugin.getServer().getPluginManager().registerEvents(new Listener<>(this), plugin);
    }

    public UserCache(JavaPlugin plugin, org.bukkit.event.Listener listener) {
        this(plugin, new HashMap<>(), listener);
    }

    public UserCache(JavaPlugin plugin) {
        this(plugin, new HashMap<>());
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

    public void onUserConnect(PlayerJoinEvent event) {
        this.loadUser(event.getPlayer().getUniqueId(), true);
    }

    public void onUserDisconnect(PlayerQuitEvent event) {
        this.unloadUser(event.getPlayer().getUniqueId());
    }

    public static class Listener<T> implements org.bukkit.event.Listener {
        private final UserCache<T> cache;

        public Listener(UserCache<T> cache) {
            this.cache = cache;
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            this.cache.onUserConnect(event);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            this.cache.onUserDisconnect(event);
        }
    }
}