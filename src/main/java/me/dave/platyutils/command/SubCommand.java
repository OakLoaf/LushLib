package me.dave.platyutils.command;

import com.google.common.collect.HashMultimap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public abstract class SubCommand {
    private final String name;
    private final HashMap<String, SubCommand> subCommands = new HashMap<>();
    private final HashMultimap<Integer, Callable<List<String>>> requiredArgs = HashMultimap.create();
    private final List<String> requiredPermissions = new ArrayList<>();
    private boolean isChild = false;

    public SubCommand(String name) {
        this.name = name;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command inside of a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     *     to default to the command executor
     */
    @SuppressWarnings("unused")
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    public String getName() {
        return name;
    }

    public SubCommand getSubCommand(String name) {
        return subCommands.get(name);
    }

    public Collection<SubCommand> getSubCommands() {
        return subCommands.values();
    }

    @SuppressWarnings("UnusedReturnValue")
    public SubCommand addSubCommand(SubCommand subCommand) {
        if (subCommand.isChild() || subCommands.containsKey(subCommand.getName())) {
            throw new IllegalStateException("This sub-command already has a parent");
        }

        subCommands.put(subCommand.getName(), subCommand);
        subCommand.setIsChild(true);
        return this;
    }

    public void removeSubCommand(String subCommand) {
        subCommands.remove(subCommand);
    }

    public boolean hasRequiredArgs() {
        return requiredArgs.size() > 0;
    }

    public List<String> getRequiredArgs(int index) {
        List<String> args = new ArrayList<>();

        for (Callable<List<String>> callable: this.requiredArgs.get(index)) {
            try {
                args.addAll(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return args;
    }

    public int getRequiredArgCount() {
        return requiredArgs.size();
    }

    public void addRequiredArgs(int index, Callable<List<String>> argsCallable) {
        requiredArgs.put(index, argsCallable);
    }

    public void removeRequiredArgs(int index) {
        requiredArgs.removeAll(index);
    }

    public boolean hasRequiredPermissions(CommandSender sender) {
        for (String permission : requiredPermissions) {
            if (!sender.hasPermission(permission)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    public SubCommand addRequiredPermission(String permission) {
        requiredPermissions.add(permission);
        return this;
    }

    public void removeRequiredPermission(String permission) {
        requiredPermissions.remove(permission);
    }

    public boolean isChild() {
        return isChild;
    }

    public void setIsChild(boolean isChild) {
        this.isChild = isChild;
    }
}
