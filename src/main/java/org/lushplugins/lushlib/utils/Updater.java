package org.lushplugins.lushlib.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Updater {
    private static final ScheduledExecutorService updateExecutor = Executors.newScheduledThreadPool(1);
    private static final HashSet<Updater> updaters = new HashSet<>();

    private final String pluginName;
    private final String modrinthProjectId;
    private final String currentVersion;
    private final String jarName;
    private final String permission;
    private final String downloadCommand;
    private final File updateFolder;
    private String updateMessage = "&#ffe27aA new &#e0c01b%plugin_name% &#ffe27aupdate is now available, type &#e0c01b'%download_command%' &#ffe27ato download it!";

    private boolean enabled;
    private String latestVersion;
    private String downloadUrl;

    private boolean updateAvailable = false;
    private boolean ready = false;
    private boolean alreadyDownloaded = false;

    public Updater(JavaPlugin plugin, String modrinthProjectId, String permission, String downloadCommand) {
        this.pluginName = plugin.getName();
        this.modrinthProjectId = modrinthProjectId;
        String currentVersion = plugin.getDescription().getVersion();
        this.currentVersion = currentVersion.contains("-") ? currentVersion.split("-")[0] : currentVersion;
        this.jarName = plugin.getDescription().getName();
        this.permission = permission;
        this.downloadCommand = downloadCommand;
        this.updateFolder = new File(plugin.getDataFolder().getParentFile(), Bukkit.getUpdateFolder());

        updateExecutor.scheduleAtFixedRate(() -> {
            try {
                check();
            } catch (Exception e) {
                LushLogger.getLogger().info("Unable to check for update: " + e.getMessage());
            }
        }, 2, 600, TimeUnit.SECONDS);

        updaters.add(this);
    }

    public void queueCheck() {
        updateExecutor.schedule(() -> {
            try {
                check();
            } catch (Exception e) {
                LushLogger.getLogger().info("Unable to check for update: " + e.getMessage());
            }
        }, 0, TimeUnit.SECONDS);
    }

    private void check() throws IOException {
        if (!enabled) {
            return;
        }

        URL url = new URL("https://api.modrinth.com/v2/project/" + modrinthProjectId + "/version?featured=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", jarName + "/" + currentVersion);

        if (connection.getResponseCode() != 200) {
            throw new IllegalStateException("Response code was " + connection.getResponseCode());
        }

        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);

        JsonArray versionsJson;
        String bukkitVersion = Bukkit.getBukkitVersion();
        if (bukkitVersion.contains("1.16") || bukkitVersion.contains("1.17")) {
            versionsJson = new JsonParser().parse(reader).getAsJsonArray();
        } else {
            versionsJson = JsonParser.parseReader(reader).getAsJsonArray();
        }

        JsonObject currVersionJson = versionsJson.get(0).getAsJsonObject();

        latestVersion = currVersionJson.get("version_number").getAsString();
        downloadUrl = currVersionJson.get("files").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();

        if (latestVersion.contains("-")) {
            latestVersion = latestVersion.split("-")[0];
        }

        if (latestVersion.isEmpty()) {
            throw new IllegalStateException("Latest version is empty!");
        }

        String[] parts = latestVersion.split("\\.");
        String[] currParts = currentVersion.split("\\.");

        int i = 0;
        for (String part : parts) {
            if (i >= currParts.length) {
                break;
            }

            int newVersion = Integer.parseInt(part);
            int currVersion = Integer.parseInt(currParts[i]);
            if (newVersion > currVersion) {
                if(i != 0) {
                    int newVersionLast = Integer.parseInt(parts[i-1]);
                    int currVersionLast = Integer.parseInt(currParts[i-1]);
                    if (newVersionLast >= currVersionLast) {
                        updateAvailable = true;
                        break;
                    }
                } else {
                    updateAvailable = true;
                    break;
                }
            } else if (newVersion < currVersion) {
                break;
            }

            i++;
        }

        if (updateAvailable && !ready) {
            LushLogger.getLogger().info("An update is available! (" + latestVersion + ") Do /" + downloadCommand + " to download it!");
        } else if (!ready) {
            LushLogger.getLogger().info("You are up to date! (" + latestVersion + ")");
        }

        ready = true;
    }

    public boolean isUpdateAvailable() {
        return enabled && updateAvailable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAlreadyDownloaded() {
        return alreadyDownloaded;
    }

    public CompletableFuture<Boolean> downloadUpdate() {

        if (!isEnabled()) {
            LushLogger.getLogger().warning("Updater is disabled");
            return CompletableFuture.completedFuture(false);
        }

        if (!isUpdateAvailable()) {
            LushLogger.getLogger().warning("No update is available!");
            return CompletableFuture.completedFuture(false);
        }

        if (isAlreadyDownloaded()) {
            LushLogger.getLogger().warning("The update has already been downloaded!");
            return CompletableFuture.completedFuture(false);
        }

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        updateExecutor.schedule(() -> {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("User-Agent", jarName + "/" + currentVersion);
                connection.setInstanceFollowRedirects(true);
                HttpURLConnection.setFollowRedirects(true);

                if (connection.getResponseCode() != 200) {
                    throw new IllegalStateException("Response code was " + connection.getResponseCode());
                }

                ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
                File out = new File(getUpdateFolder(), jarName + "-" + latestVersion + ".jar");
                LushLogger.getLogger().info(out.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(out);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();

                updateAvailable = false;
                alreadyDownloaded = true;
                completableFuture.complete(true);
            } catch (Exception e) {
                e.printStackTrace();
                completableFuture.complete(false);
            }
        }, 0, TimeUnit.SECONDS);

        return completableFuture;
    }

    public void shutdown() {
        updateExecutor.shutdown();
        updaters.remove(this);
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getModrinthProjectId() {
        return modrinthProjectId;
    }

    public String getPermission() {
        return permission;
    }

    public String getDownloadCommand() {
        return downloadCommand;
    }

    public File getUpdateFolder() {
        if (!updateFolder.exists()) {
            updateFolder.mkdir();
        }

        return updateFolder;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public Updater setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
        return this;
    }

    public static HashSet<Updater> getUpdaters() {
        return updaters;
    }

    public static class UpdaterListener implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            updaters.forEach(updater -> {
                if (player.hasPermission(updater.getPermission())) {
                    if (updater.isUpdateAvailable() && !updater.isAlreadyDownloaded()) {
                        updateExecutor.schedule(() -> {
                            String message = updater.getUpdateMessage()
                                .replace("%modrinth_slug%", updater.getModrinthProjectId())
                                .replace("%plugin_name%", updater.getPluginName())
                                .replace("%download_command%", updater.getDownloadCommand());

                            ChatColorHandler.sendMessage(player, message);
                        }, 2, TimeUnit.SECONDS);
                    }
                }
            });
        }
    }
}
