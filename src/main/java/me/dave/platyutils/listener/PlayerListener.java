package me.dave.platyutils.listener;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.platyutils.PlatyUtils;
import me.dave.platyutils.utils.Updater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class PlayerListener implements EventListener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Updater.getUpdaters().forEach(updater -> {
            if (player.hasPermission(updater.getPermission())) {
                if (updater.isUpdateAvailable() && !updater.isAlreadyDownloaded()) {
                    PlatyUtils.getMorePaperLib().scheduling().asyncScheduler().runDelayed(() -> {
                        String message = updater.getUpdateMessage()
                            .replace("%modrinth_slug%", updater.getModrinthProjectSlug())
                            .replace("%plugin_name%", PlatyUtils.getPlugin().getName())
                            .replace("%download_command%", updater.getDownloadCommand());

                        ChatColorHandler.sendMessage(player, message);
                    }, Duration.of(2, ChronoUnit.SECONDS));
                }
            }
        });
    }
}
