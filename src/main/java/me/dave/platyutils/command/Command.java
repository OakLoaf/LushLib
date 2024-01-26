package me.dave.platyutils.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CommandExecutor, TabCompleter {
    private final List<SubCommand> subCommands = new ArrayList<>();

    public abstract boolean execute();

    public abstract List<String> tabComplete();

    public Command subCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabComplete = new ArrayList<>();

        switch (args.length) {
            case 1 -> tabComplete.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            case 2 -> {
                return List.of("<custom_model_data>");
            }
            case 3 -> {
                return List.of("<name>");
            }
        }

        List<String> wordCompletion = new ArrayList<>();
        boolean wordCompletionSuccess = false;
        int currArg = args.length - 1;
        for (String currTab : tabComplete) {
            if (currTab.regionMatches(true, 0, args[currArg], 0, args[currArg].length())) {
                wordCompletion.add(currTab);
                wordCompletionSuccess = true;
            }

//            if (currTab.startsWith(args[currArg])) {
//                wordCompletion.add(currTab);
//                wordCompletionSuccess = true;
//            }
        }

        if (wordCompletionSuccess) return wordCompletion;
        return tabComplete;
    }
}
