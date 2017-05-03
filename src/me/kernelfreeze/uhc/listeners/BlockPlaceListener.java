package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.event.block.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class BlockPlaceListener implements Listener
{
    private final GameManager gameManager;
    
    public BlockPlaceListener() {
        this.gameManager = GameManager.getGameManager();
    }
    
    @EventHandler
    public void onBlockPlaceEvent(final BlockPlaceEvent blockPlaceEvent) {
        final Player player = blockPlaceEvent.getPlayer();
        if (player.getWorld().equals(this.gameManager.getSpawnLocation().getWorld()) && !player.hasPermission("uhc.spawnprotection.bypass")) {
            blockPlaceEvent.setCancelled(true);
            player.sendMessage("§cYou cannot place blocks here!");
        }
        if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
            blockPlaceEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerBucketEmptyEvent(final PlayerBucketEmptyEvent playerBucketEmptyEvent) {
        final Player player = playerBucketEmptyEvent.getPlayer();
        if (player.getWorld().equals(this.gameManager.getSpawnLocation().getWorld()) && !player.hasPermission("uhc.spawnprotection.bypass")) {
            playerBucketEmptyEvent.setCancelled(true);
            player.sendMessage("§cYou cannot place blocks here!");
        }
        if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
            playerBucketEmptyEvent.setCancelled(true);
        }
    }
}
