package gg.tjr.mc.xrayalerts.listeners;

import gg.tjr.mc.xrayalerts.Settings;
import gg.tjr.mc.xrayalerts.Settings.AlertMode;
import gg.tjr.mc.xrayalerts.XRayAlertsPlugin;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OreMineListener implements Listener {

    private final Plugin plugin = XRayAlertsPlugin.getInstance();

    private final Settings settings;
    private final Map<Block, Long> processedBlocks = new HashMap<>();
    private final long processedBlocksCleanupInterval = 1000*60*5;

    public OreMineListener(Settings settings) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupProcessedBlocks();
            }
        }.runTaskTimer(plugin, 20 * 60, 20 * 60);
        this.settings = settings;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.hasPermission("xrayalerts.ignore")) {
            return;
        }

        if (this.settings.isMonitoredBlock(block)) {
            Material blockMaterial = block.getType();
            int count;

            if (this.settings.getAlertMode() == AlertMode.VEIN) {
                if (processedBlocks.containsKey(block)) {
                    return;
                }

                Set<Block> vein = findVein(block, blockMaterial);
                count = vein.size();

                long currentTime = System.currentTimeMillis();
                vein.forEach(b -> processedBlocks.put(b, currentTime));
            } else {
                count = event.getBlock().getDrops(player.getInventory().getItemInMainHand()).size();
            }

            String message = formatPlayerMessage(block, blockMaterial, player, count);
            plugin.getServer().getOnlinePlayers().stream()
                    .filter(this::isAlertable)
                    .forEach(p -> p.sendMessage(message));

            if (this.settings.logAlerts()) {
                String logMessage = formatLogMessage(block, blockMaterial, player, count);
                plugin.getLogger().info(logMessage);
            }
        }
    }

    private boolean isAlertable(Player p) {
        return p.hasPermission("xrayalerts.receive")
            && this.settings.inAlertSet(p);
    }

    private String formatPlayerMessage(Block block, Material blockMaterial, Player player, int count) {
        return formatMessage(this.settings.getMessageFormat(), block, blockMaterial, player, count);
    }

    private String formatLogMessage(Block block, Material blockMaterial, Player player, int count) {
        return formatMessage(this.settings.getLogFormat(), block, blockMaterial, player, count);
    }

    private String formatMessage(String string, Block block, Material blockMaterial, Player player, int count) {
        return string
            .replace("%count%", String.valueOf(count))
            .replace("%item%", blockMaterial.name().toLowerCase().replace("_", " "))
            .replace("%player%", player.getName())
            .replace("%blockX%", String.valueOf(block.getX()))
            .replace("%blockY%", String.valueOf(block.getY()))
            .replace("%blockZ%", String.valueOf(block.getZ()));
    }

    private Set<Block> findVein(Block startBlock, Material material) {
        Set<Block> vein = new HashSet<>();
        Set<Block> toCheck = new HashSet<>();

        toCheck.add(startBlock);

        while (!toCheck.isEmpty()) {
            Block block = toCheck.iterator().next();
            toCheck.remove(block);

            if (block.getType() == material && vein.add(block)) {
                for (Block relative : getAdjacentBlocks(block)) {
                    if (!vein.contains(relative) && !processedBlocks.containsKey(relative)) {
                        toCheck.add(relative);
                    }
                }
            }
        }

        return vein;
    }

    private Set<Block> getAdjacentBlocks(Block block) {
        return Set.of(
            block.getRelative(1, 0, 0),
            block.getRelative(-1, 0, 0),
            block.getRelative(0, 1, 0),
            block.getRelative(0, -1, 0),
            block.getRelative(0, 0, 1),
            block.getRelative(0, 0, -1)
        );
    }

    private void cleanupProcessedBlocks() {
        long currentTime = System.currentTimeMillis();
        processedBlocks.entrySet().removeIf(entry -> currentTime - entry.getValue() > processedBlocksCleanupInterval);
    }
}
