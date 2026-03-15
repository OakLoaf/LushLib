package org.lushplugins.lushlib.utils.timer;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.plugin.Plugin;

public class RainbowBossBarTimer extends BossBarTimer {
    private static final BossBar.Color[] COLORS = BossBar.Color.values();

    private int colorIndex = 0;

    public RainbowBossBarTimer(String id, String title, BossBar.Overlay barStyle, Plugin plugin, int totalDuration) {
        super(id, title, BossBar.Color.PINK, barStyle, plugin, totalDuration);
    }

    public RainbowBossBarTimer(String title, BossBar.Overlay barStyle, Plugin plugin, int totalDuration) {
        super(title, BossBar.Color.PINK, barStyle, plugin, totalDuration);
    }

    @Override
    protected void onTick() {
        super.onTick();

        // Updates bar color every 5 ticks
        if (this.tick % 5 == 0) {
            this.getBossBar().color(this.nextColor());
        }
    }

    private BossBar.Color nextColor() {
        return COLORS[++this.colorIndex % COLORS.length];
    }
}
