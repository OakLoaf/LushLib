package org.lushplugins.lushlib.utils.timer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.lushplugins.chatcolorhandler.paper.PaperColor;
import org.lushplugins.lushlib.utils.TimeFormatter;

import java.time.Duration;

@SuppressWarnings("unused")
public class BossBarTimer extends Timer {
    private static int currId = 0;
    private final BossBar bossBar;
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
    public BossBarTimer(String id, String title, BossBar.Color barColor, BossBar.Overlay barStyle, Plugin plugin, int totalDuration) {
        super(plugin, totalDuration);

        this.bossBar = BossBar.bossBar(
            PaperColor.handler().translate(title
                .replace("%current_duration%", String.valueOf(this.tick / 20))
                .replace("%remaining_duration%", String.valueOf((this.totalDuration - this.tick) / 20))
                .replace("%total_duration%", String.valueOf(this.totalDuration / 20))),
            1.0f,
            barColor,
            barStyle
        );

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
    public BossBarTimer(String title, BossBar.Color barColor, BossBar.Overlay barStyle, Plugin plugin, int totalDuration) {
        this(String.valueOf(currId++), title, barColor, barStyle, plugin, totalDuration);
    }

    @Override
    protected void onTick() {
        super.onTick();

        if (this.unparsedTitle != null && this.tick % 20 == 0) {
            bossBar.name(PaperColor.handler().translate(this.unparsedTitle
                .replace("%current_duration%", TimeFormatter.formatDuration(Duration.ofSeconds(tick / 20), TimeFormatter.FormatType.SHORT_FORM))
                .replace("%remaining_duration%", TimeFormatter.formatDuration(Duration.ofSeconds((totalDuration - tick) / 20), TimeFormatter.FormatType.SHORT_FORM))
                .replace("%total_duration%", TimeFormatter.formatDuration(Duration.ofSeconds(totalDuration / 20), TimeFormatter.FormatType.SHORT_FORM))));
        }

        // Updates bar progress every 3 ticks
        if (this.tick % 3 != 0) {
            return;
        }

        float progress = (float) this.tick / totalDuration;
        if (progress < 0) {
            progress = 0;
        } else if (progress > 1) {
            progress = 1;
        }

        bossBar.progress(1 - progress);
    }

    @Override
    protected void onFinish() {
        super.onFinish();

        bossBar.viewers().forEach(viewer -> {
            if(viewer instanceof Audience audience) {
                audience.hideBossBar(bossBar);
            }
        });
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void addPlayer(Player player) {
        player.showBossBar(bossBar);
    }

    public void addOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(bossBar::addViewer);
    }

    public void removePlayer(Player player) {
        player.hideBossBar(bossBar);
    }
}
