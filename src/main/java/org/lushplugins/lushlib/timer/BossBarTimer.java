package org.lushplugins.lushlib.timer;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.lushplugins.chatcolorhandler.ChatColorHandler;

@SuppressWarnings("unused")
public class BossBarTimer extends Timer {
    private static int currId = 0;
    private final KeyedBossBar bossBar;
    private final String unparsedTitle;

    // TODO: Add animation speed option
    /**
     * @param id            A unique id for this boss bar
     * @param title         Title of boss bar
     * @param barColor      Color of boss bar
     * @param barStyle      Style of boss bar
     * @param plugin        Plugin running the timer
     * @param totalDuration Total duration of timer in seconds
     */
    public BossBarTimer(String id, String title, BarColor barColor, BarStyle barStyle, Plugin plugin, int totalDuration) {
        super(plugin, totalDuration);

        bossBar = Bukkit.createBossBar(
            new NamespacedKey(plugin, id),
            ChatColorHandler.translate(title
                    .replace("%current_duration%", String.valueOf(this.tick / 20))
                    .replace("%remaining_duration%", String.valueOf((this.totalDuration - this.tick) / 20))
                    .replace("%total_duration%", String.valueOf(this.totalDuration / 20))),
            barColor,
            barStyle);

        // No need to reparse if there are no placeholders
        if (title.contains("%current_duration%") || title.contains("%remaining_duration%") || title.contains("%total_duration%")) {
            this.unparsedTitle = title;
        } else {
            this.unparsedTitle = null;
        }
    }

    /**
     * @param title         Title of boss bar
     * @param barColor      Color of boss bar
     * @param barStyle      Style of boss bar
     * @param plugin        Plugin running the timer
     * @param totalDuration Total duration of timer in seconds
     */
    public BossBarTimer(String title, BarColor barColor, BarStyle barStyle, Plugin plugin, int totalDuration) {
        this(String.valueOf(currId++), title, barColor, barStyle, plugin, totalDuration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bossBar.setProgress(1.0);
    }

    @Override
    protected void onTick() {
        super.onTick();

        if (this.unparsedTitle != null && this.tick % 20 == 0) {
            bossBar.setTitle(ChatColorHandler.translate(this.unparsedTitle
                .replace("%current_duration%", String.valueOf(tick / 20))
                .replace("%remaining_duration%", String.valueOf((totalDuration - tick) / 20))
                .replace("%total_duration%", String.valueOf(totalDuration / 20))));
        }

        // Updates bar progress every 3 ticks
        if (this.tick % 3 != 0) {
            return;
        }

        double progress = (double) this.tick / totalDuration;
        if (progress < 0) {
            progress = 0;
        } else if (progress > 1) {
            progress = 1;
        }

        bossBar.setProgress(1 - progress);
    }

    @Override
    protected void onFinish() {
        super.onFinish();

        bossBar.removeAll();
        Bukkit.removeBossBar(bossBar.getKey());
    }

    public KeyedBossBar getBossBar() {
        return bossBar;
    }

    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void addOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    }

    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }
}
