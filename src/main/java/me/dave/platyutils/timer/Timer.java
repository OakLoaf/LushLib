package me.dave.platyutils.timer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class Timer {
    private final Plugin plugin;
    private BukkitTask task;
    protected int duration = 0;
    protected int totalDuration;

    /**
     * @param plugin Plugin running the timer
     * @param totalDuration Total duration of timer in seconds
     */
    public Timer(Plugin plugin, int totalDuration) {
        this.plugin = plugin;
        this.totalDuration = totalDuration;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * @param duration Sets current duration of timer
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    /**
     * @param totalDuration Total duration of timer in seconds
     */
    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public boolean isActive() {
        return task != null;
    }

    /**
     * Ran when the timer is started
     */
    public abstract void onStart();

    /**
     * Ran when the timer's duration changing
     */
    public abstract void onDurationChange();

    /**
     * Ran when the timer ends
     */
    public abstract void onFinish();

    public void start() {
        if (task == null) {
            onStart();

            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                duration++;
                onDurationChange();

                if (duration >= totalDuration) {
                    onFinish();
                    stop();
                }
            }, 1, 1);
        } else {
            throw new IllegalStateException("Timer is already active");
        }
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        } else {
            throw new IllegalStateException("Timer is not active");
        }
    }

    public void restart() {
        if (task != null) {
            stop();
        }

        duration = 0;
        start();
    }
}
