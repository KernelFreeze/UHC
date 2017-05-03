package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;

public class PlayerQuitListener implements Listener
{
    private final GameManager gameManager;
    
    public PlayerQuitListener() {
        this.gameManager = GameManager.getGameManager();
    }
    
    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent playerQuitEvent) {
        final Player player = playerQuitEvent.getPlayer();
        final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        if (this.gameManager.isGameRunning() || this.gameManager.isScattering()) {
            new BukkitRunnable() {
                public void run() {
                    if (!Bukkit.getOfflinePlayer(player.getUniqueId()).isOnline()) {
                        uhcPlayer.setPlayerAlive(false);
                        uhcPlayer.setDied(true);
                        player.setWhitelisted(false);
                        if (player.hasPermission("uhc.spectate")) {
                            PlayerManager.getPlayerManager().setSpectating(true, player);
                            player.setWhitelisted(true);
                        }
                    }
                }
            }.runTaskLater((Plugin) UHC.getInstance(), (long)(1200 * this.gameManager.getKillOnQuitTime()));
        }
        if (uhcPlayer.isPlayerAlive()) {
            if ((this.gameManager.isGameRunning() || this.gameManager.isScattering()) && !uhcPlayer.didPlayerDie() && player.getWorld().equals(this.gameManager.getUHCWorld())) {
                uhcPlayer.setLastArmour(player.getInventory().getArmorContents());
                uhcPlayer.setLastInventory(player.getInventory().getContents());
                if (this.gameManager.isScattering() && player.getWorld().equals(this.gameManager.getSpawnLocation().getWorld())) {
                    this.gameManager.scatterPlayer(player);
                }
                uhcPlayer.setRespawnLocation(player.getLocation());
            }
            if (this.gameManager.getHostName().equalsIgnoreCase(player.getName())) {
                this.gameManager.setHost(null);
            }
            if (this.gameManager.getModerators().contains(player)) {
                this.gameManager.setModerator(player, false);
            }
        }
    }
}
