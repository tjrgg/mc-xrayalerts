package gg.tjr.mc.xrayalerts.listeners;

import gg.tjr.mc.xrayalerts.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Settings settings;

    public PlayerJoinListener(Settings settings) {
        this.settings = settings;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (!settings.isInAlertsSection(player) && player.hasPermission("xrayalerts.receive")) {
            settings.toggleAlerts(player);
        }
    }
}
