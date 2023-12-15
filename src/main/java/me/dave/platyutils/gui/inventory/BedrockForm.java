package me.dave.platyutils.gui.inventory;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

@SuppressWarnings("unused")
public class BedrockForm {
    private final Form form;

    public BedrockForm(Form form) {
        this.form = form;
    }

    public void send(Player player) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        floodgatePlayer.sendForm(form);
    }
}
