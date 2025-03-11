package gg.tjr.mc.xrayalerts.commands;

import gg.tjr.mc.xrayalerts.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class XRayAlertsCommand implements CommandExecutor {

    private final Settings settings;

    public XRayAlertsCommand(Settings settings) {
        this.settings = settings;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("xrayalerts.toggle")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        boolean alertsEnabled = this.settings.toggleAlerts(player);

        String message = alertsEnabled ? "X-Ray alerts enabled." : "X-Ray alerts disabled.";
        player.sendMessage(message);

        return true;
    }
}
