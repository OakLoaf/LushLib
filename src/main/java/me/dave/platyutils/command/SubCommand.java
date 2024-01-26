package me.dave.platyutils.command;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {
    private final String name;
    private final List<SubCommand> subCommands = new ArrayList<>();
    private boolean hasParent = false;

    public SubCommand(Command parent, String name) {
        this.name = name;
    }

    public abstract boolean execute();

    public abstract List<String> tabComplete();
}
