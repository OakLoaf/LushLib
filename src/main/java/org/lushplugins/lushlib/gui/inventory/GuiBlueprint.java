package org.lushplugins.lushlib.gui.inventory;

import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class GuiBlueprint {
    private final String title;
    private final List<GuiLayer> layers;

    public GuiBlueprint(String title, List<GuiLayer> layers) {
        this.title = title;
        this.layers = new ArrayList<>(layers);
    }

    public GuiBlueprint(String title, GuiLayer layer) {
        this(title, Collections.singletonList(layer));
    }

    public GuiBlueprint(String title) {
        this(title, Collections.emptyList());
    }

    public String getTitle() {
        return this.title;
    }

    public List<GuiLayer> getLayers() {
        return this.layers;
    }

    public void addLayer(GuiLayer layer) {
        this.layers.add(layer);
    }

    // TODO
    public Gui constructFor(Player player, Function<String, String> parser) {
        return new Gui(this.layers, this.title, player);
    }

    /**
     * @see GuiBlueprint#constructSimple(Player)
     */
    @Deprecated
    public Gui constructFor(Player player) {
        return new Gui(this.layers, this.title, player);
    }

    public <T extends Gui> T construct(Player player, TriFunction<List<GuiLayer>, String, Player, T> constructor) {
        return constructor.apply(this.layers, this.title, player);
    }

    public Gui constructSimple(Player player) {
        return construct(player, Gui::new);
    }
}
