package gg.tjr.mc.xrayalerts;

import gg.tjr.mc.xrayalerts.commands.XRayAlertsCommand;
import gg.tjr.mc.xrayalerts.listeners.OreMineListener;
import gg.tjr.mc.xrayalerts.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class XRayAlertsPlugin extends JavaPlugin {

    private static XRayAlertsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Settings settings = new Settings(getConfig());

        getCommand("xrayalerts").setExecutor(new XRayAlertsCommand(settings));

        getServer().getPluginManager().registerEvents(new OreMineListener(settings), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(settings), this);

        getLogger().info("Plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        instance = null;

        getLogger().info("Plugin has been disabled.");
    }

    public static XRayAlertsPlugin getInstance() {
        return instance;
    }
}
