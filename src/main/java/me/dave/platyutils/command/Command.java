package me.dave.platyutils.command;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.platyutils.language.LanguageManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
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
        String[] subCommandArgs = args;

        for (int i = 0; i < args.length; i++) {
            if (currSubCommand.getRequiredArgs(i).contains(args[i])) {
                subCommandArgs = Arrays.copyOfRange(args, i + 1, args.length);
                continue;
            }

            boolean found = false;
            for (SubCommand subCommand : currSubCommand.getSubCommands()) {
                if (subCommand.getName().equals(args[i])) {
                    currSubCommand = subCommand;
                    subCommandArgs = Arrays.copyOfRange(args, i + 1, args.length);
                    found = true;
                    break;
                }
            }

            if (!found) {
                break;
            }
        }

        if (currSubCommand.hasRequiredPermissions(sender)) {
            return currSubCommand.execute(sender, command, label, subCommandArgs);
        } else {
            ChatColorHandler.sendMessage(sender, LanguageManager.getMessage("insufficient-permissions"));
            return true;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabComplete = new ArrayList<>();

        SubCommand activeSubCommand = this;
        String[] subCommandArgs = new String[0];
        for (int i = 0; i < args.length; i++) {
            if (activeSubCommand.getRequiredArgs(i).contains(args[i])) {
                continue;
            }

            for (SubCommand subCommand : activeSubCommand.getSubCommands()) {
                if (subCommand.getName().equals(args[i])) {
                    activeSubCommand = subCommand;
                    subCommandArgs = Arrays.copyOfRange(args, i + 1, args.length);
                    break;
                }
            }
        }

        if (activeSubCommand.hasRequiredPermissions(sender)) {
            List<String> subCommandTabComplete = activeSubCommand.tabComplete(sender, command, label, subCommandArgs);
            if (subCommandTabComplete != null) {
                tabComplete.addAll(subCommandTabComplete);
            }

            if (activeSubCommand.hasRequiredArgs() && subCommandArgs.length <= activeSubCommand.getRequiredArgCount()) {
                tabComplete.addAll(activeSubCommand.getRequiredArgs(subCommandArgs.length - 1));
            } else {
                activeSubCommand.getSubCommands().forEach(subCommand -> {
                    if (subCommand.hasRequiredPermissions(sender)) {
                        tabComplete.add(subCommand.getName());
                    }
                });
            }
        }

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
