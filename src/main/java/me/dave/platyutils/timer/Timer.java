package me.dave.platyutils.timer;

import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("unused")
public abstract class Timer {
    private final Plugin plugin;
    private BukkitTask task;
    private final HashMultimap<TaskType, Runnable> runnables = HashMultimap.create();
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
    protected void onStart() {
        runnables.get(TaskType.START).forEach(Runnable::run);
    }

    /**
     * Ran when the timer's duration changing
     */
    protected void onDurationChange() {
        runnables.get(TaskType.DURATION_CHANGE).forEach(Runnable::run);
    }

    /**
     * Ran when the timer ends
     */
    protected void onFinish() {
        runnables.get(TaskType.FINISH).forEach(Runnable::run);
    }

    public void onStart(Runnable runnable) {
        runnables.put(TaskType.START, runnable);
    }

    public void onDurationChange(Runnable runnable) {
        runnables.put(TaskType.DURATION_CHANGE, runnable);
    }

    public void onFinish(Runnable runnable) {
        runnables.put(TaskType.FINISH, runnable);
    }

    public void start() {
        if (task == null) {
            onStart();

            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                duration++;
                onDurationChange();

                if (duration >= totalDuration) {
                    remove();
                }
            }, 20, 20);
        }
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void restart() {
        if (task != null) {
            stop();
        }

        duration = 0;
        start();
    }

    public void remove() {
        onFinish();
        stop();
    }

    private enum TaskType {
        START,
        DURATION_CHANGE,
        FINISH
    }
}
