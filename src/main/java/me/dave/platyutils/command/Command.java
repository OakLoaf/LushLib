package me.dave.platyutils.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends SubCommand implements CommandExecutor, TabCompleter {

    public Command(String name) {
        super(name);
    }

    @Override
    public Command addSubCommand(SubCommand subCommand) {
        super.addSubCommand(subCommand);
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        SubCommand currSubCommand = this;
        for (String arg : args) {
            boolean found = false;

            for (SubCommand subCommand : currSubCommand.getSubCommands()) {
                if (subCommand.getName().equals(arg)) {
                    currSubCommand = subCommand;
                    found = true;
                    break;
                }
            }

            if (!found) {
                break;
            }
        }

        return currSubCommand.execute();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabComplete = new ArrayList<>();

        SubCommand currSubCommand = this;
        for (String arg : args) {
            for (SubCommand subCommand : currSubCommand.getSubCommands()) {
                if (subCommand.getName().equals(arg)) {
                    currSubCommand = subCommand;
                    break;
                }
            }
        }

        currSubCommand.getSubCommands().forEach(subCommand -> tabComplete.add(subCommand.getName()));
        tabComplete.addAll(currSubCommand.tabComplete());

        List<String> wordCompletion = new ArrayList<>();
        boolean wordCompletionSuccess = false;
        int currArg = args.length - 1;
        for (String currTab : tabComplete) {
            if (currTab.startsWith(args[currArg])) {
                wordCompletion.add(currTab);
                wordCompletionSuccess = true;
            }
        }

        if (wordCompletionSuccess) return wordCompletion;
        return tabComplete;
    }
}
