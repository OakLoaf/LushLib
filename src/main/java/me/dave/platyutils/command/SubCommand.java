package me.dave.platyutils.command;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {
    private final String name;
    private final List<SubCommand> subCommands = new ArrayList<>();
    private boolean isChild = false;

    public SubCommand(String name) {
        this.name = name;
    }

    // TODO: Add parameters
    public abstract boolean execute();

    // TODO: Add parameters
    public abstract List<String> tabComplete();

    public String getName() {
        return name;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public SubCommand addSubCommand(SubCommand subCommand) {
        if (subCommand.isChild()) {
            throw new IllegalStateException("This sub-command already has a parent");
        }

        subCommands.add(subCommand);
        subCommand.setIsChild(true);
        return this;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setIsChild(boolean isChild) {
        this.isChild = isChild;
    }
}
