package org.lushplugins.lushlib.timer;

import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("unused")
public abstract class Timer {
    private final Plugin plugin;
    private BukkitTask task;
    private final HashMultimap<TaskType, Runnable> runnables = HashMultimap.create();
    protected int tick = 0;
    protected int totalDuration;

    /**
     * @param plugin Plugin running the timer
     * @param totalDuration Total duration of timer in seconds
     */
    public Timer(Plugin plugin, int totalDuration) {
        this.plugin = plugin;
        this.totalDuration = totalDuration * 20;
    }

    /**
     * @return Current tick
     */
    public int getTick() {
        return tick;
    }

    /**
     * @param tick Current tick
     */
    public void setTick(int tick) {
        this.tick = tick;
        onTick();
    }

    /**
     * @return Current duration in seconds
     */
    public int getDuration() {
        return tick / 20;
    }

    /**
     * @param duration Current duration in seconds
     */
    public void setDuration(int duration) {
        this.tick = duration * 20;
        onDurationChange();
    }

    /**
     * @return Total duration in ticks
     */
    public int getTotalDurationTicks() {
        return totalDuration;
    }

    /**
     * @param totalDuration Total duration of timer in ticks
     */
    public void setTotalDurationTicks(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    /**
     * @return Total duration in seconds
     */
    public int getTotalDuration() {
        return totalDuration / 20;
    }

    /**
     * @param totalDuration Total duration of timer in seconds
     */
    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration * 20;
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
     * Ran every tick for the duration of the timer
     */
    protected void onTick() {
        runnables.get(TaskType.TICK).forEach(Runnable::run);
    }

    /**
     * Ran once per second for the duration of the timer
     */
    @Deprecated
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

    public void onTick(Runnable runnable) {
        runnables.put(TaskType.TICK, runnable);
    }

    @Deprecated
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
                setTick(tick + 1);

                if (tick % 20 == 0) {
                    onDurationChange();
                }

                if (tick >= totalDuration) {
                    remove();
                }
            }, 1, 1);
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

        tick = 0;
        start();
    }

    public void remove() {
        onFinish();
        stop();
    }

    private enum TaskType {
        START,
        DURATION_CHANGE,
        TICK,
        FINISH
    }
}
