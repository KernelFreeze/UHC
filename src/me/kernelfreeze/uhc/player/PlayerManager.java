package me.kernelfreeze.uhc.player;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import java.util.*;

public class PlayerManager
{
    private static PlayerManager playerManager;
    private final GameManager gameManager;
    private Map<UUID, UHCPlayer> players;
    
    public PlayerManager() {
        this.gameManager = GameManager.getGameManager();
        this.players = new HashMap<UUID, UHCPlayer>();
    }
    
    public static PlayerManager getPlayerManager() {
        if (PlayerManager.playerManager == null) {
            PlayerManager.playerManager = new PlayerManager();
        }
        return PlayerManager.playerManager;
    }
    
    public void setSpectating(final boolean b, final Player player) {
        Bukkit.getScheduler().runTaskLater((Plugin) UHC.getInstance(), (Runnable)new Runnable() {
            final /* synthetic */ UHCPlayer val$uhcPlayer = PlayerManager.this.getUHCPlayer(player.getUniqueId());
            
            @Override
            public void run() {
                this.val$uhcPlayer.setSpec(b);
                if (b) {
                    this.val$uhcPlayer.setPlayerAlive(false);
                    player.setGameMode(GameMode.CREATIVE);
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    if (player.isOnline()) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            p.hidePlayer(player);
                        }
                        player.setHealth(20);
                        player.setFoodLevel(20);
                        player.getActivePotionEffects().clear();
                        player.getInventory().clear();
                        player.getInventory().setArmorContents((ItemStack[])null);
                        player.getInventory().setItem(0, PlayerManager.this.gameManager.getPlayersAlive());
                        player.getInventory().setItem(1, PlayerManager.this.gameManager.getRandomPlayer());
                        player.spigot().setCollidesWithEntities(false);
                        player.teleport(PlayerManager.this.gameManager.getUHCWorld().getSpawnLocation());
                        player.setGameMode(GameMode.CREATIVE);
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage("§aYou are now vanished!");
                    }
                }
                if (!b && player.isOnline()) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.showPlayer(player);
                    }
                    if (PlayerManager.this.gameManager.getModerators().contains(player)) {
                        PlayerManager.this.gameManager.getModerators().remove(player);
                    }
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.getActivePotionEffects().clear();
                    player.getInventory().clear();
                    player.getInventory().setArmorContents((ItemStack[])null);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.spigot().setCollidesWithEntities(true);
                    player.teleport(PlayerManager.this.gameManager.getSpawnLocation());
                    player.sendMessage("§aYou are no longer vanished!");
                }
            }
        }, 25L);
    }
    
    public Map<UUID, UHCPlayer> getUHCPlayers() {
        return this.players;
    }
    
    public UHCPlayer getUHCPlayer(final UUID uuid) {
        return this.players.get(uuid);
    }
    
    public boolean doesUHCPlayerExsists(final UUID uuid) {
        return this.players.containsKey(uuid);
    }
    
    public void createUHCPlayer(final UUID uuid) {
        this.players.put(uuid, new UHCPlayer(this.gameManager.isStatsEnabled(), uuid));
    }
    
    public int alivePlayers() {
        int n = 0;
        final Iterator<UHCPlayer> iterator = this.players.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isPlayerAlive()) {
                ++n;
            }
        }
        return n;
    }
    
    public int online() {
        int n = 0;
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            ++n;
        }
        return n;
    }
    
    public int deadPlayers() {
        int n = 0;
        final Iterator<UHCPlayer> iterator = this.players.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().didPlayerDie()) {
                ++n;
            }
        }
        return n;
    }
    
    public int spectators() {
        int n = 0;
        final Iterator<UHCPlayer> iterator = this.players.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isSpectating()) {
                ++n;
            }
        }
        return n;
    }
    
    public Set<UHCPlayer> uhcPlayersSet(final Set<UUID> set) {
        final HashSet<UHCPlayer> set2 = new HashSet<UHCPlayer>();
        final Iterator<UUID> iterator = set.iterator();
        while (iterator.hasNext()) {
            set2.add(this.getUHCPlayer(iterator.next()));
        }
        return set2;
    }
}
