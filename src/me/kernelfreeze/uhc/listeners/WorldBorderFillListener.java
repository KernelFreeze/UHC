package me.kernelfreeze.uhc.listeners;

import com.wimbli.WorldBorder.Events.WorldBorderFillFinishedEvent;
import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldBorderFillListener implements Listener {
    @EventHandler
    public void onWorldBorder(final WorldBorderFillFinishedEvent worldBorderFillFinishedEvent) {
        if (worldBorderFillFinishedEvent.getWorld().equals(GameManager.getGameManager().getUHCWorld())) {
            GameManager.getGameManager().setMapGenerating(false);
        }
        if (!GameManager.getGameManager().wasRestarted()) {
            GameManager.getGameManager().setMapGenerating(false);
            Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getMainColor() + "Successfully loaded all chunks in the UHC world!");
        }
        if (!GameManager.getGameManager().wasRestarted() && UHC.getInstance().getConfig().getBoolean("settings.restart-on-chunk-load-finish")) {
            new BukkitRunnable() {
                public void run() {
                    GameManager.getGameManager().setRestarted(true);
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), GameManager.getGameManager().getRestartCommand().replace("/", ""));
                }
            }.runTaskLater((Plugin) UHC.getInstance(), 100L);
        }
    }
}
