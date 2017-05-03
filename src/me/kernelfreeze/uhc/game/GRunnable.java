package me.kernelfreeze.uhc.game;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.configs.ConfigIntegers;
import me.kernelfreeze.uhc.listeners.GameWinListener;
import me.kernelfreeze.uhc.listeners.GameWinTeamListener;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;

public class GRunnable extends BukkitRunnable
{
    private int seconds;
    private boolean broadcasted;
    private final ScoreboardM sbM;
    private final GameManager gameManager;
    
    public GRunnable() {
        this.seconds = 0;
        this.broadcasted = false;
        this.sbM = new ScoreboardM();
        this.gameManager = GameManager.getGameManager();
    }
    
    public void run() {
        ++this.seconds;
        if (this.seconds % 5 == 0) {
            if (TeamManager.getInstance().isTeamsEnabled()) {
                if (!this.broadcasted && TeamManager.getInstance().getTeamsAlive() == 1) {
                    Bukkit.getPluginManager().callEvent((Event)new GameWinTeamListener(TeamManager.getInstance().getLastTeam()));
                    this.broadcasted = true;
                }
            }
            else if (!this.broadcasted && PlayerManager.getPlayerManager().alivePlayers() == 1) {
                for (final UHCPlayer uhcPlayer : PlayerManager.getPlayerManager().getUHCPlayers().values()) {
                    if (uhcPlayer.isPlayerAlive() && !this.broadcasted) {
                        Bukkit.getServer().getPluginManager().callEvent((Event)new GameWinListener(uhcPlayer.getName(), uhcPlayer));
                        this.broadcasted = true;
                    }
                }
            }
            for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                this.gameManager.checkBorder(player);
                if (UHC.getInstance().getPlayersScoreboard().containsKey(player)) {
                    this.sbM.updateScoreboard(player, this.seconds);
                }
                else {
                    this.sbM.newScoreboard(player);
                }
            }
            if (ConfigIntegers.HEALTIME.get() * 60 == this.seconds) {
                this.gameManager.healAll();
            }
            if (ConfigIntegers.PVPTIME.get() * 60 == this.seconds) {
                this.gameManager.setPvP(true);
            }
            if ((ConfigIntegers.BORDERSHRINKTIME.get() - ConfigIntegers.BORDER_SHRINK_EVERY.get()) * 60 == this.seconds) {
                this.gameManager.enablePermaDay();
                this.gameManager.startBorderShrink();
            }
            if (this.seconds == 30) {
                this.gameManager.setCanUseRescatter();
            }
        }
        else {
            for (final Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                if (this.seconds <= 5) {
                    if (player2.getLocation().getBlockY() < this.gameManager.getUHCWorld().getHighestBlockAt(player2.getLocation()).getLocation().getBlockY()) {
                        player2.teleport(player2.getLocation().getWorld().getHighestBlockAt(player2.getLocation()).getLocation().add(0.0, 1.0, 0.0));
                    }
                    player2.setHealth(20.0);
                }
                this.gameManager.checkBorder(player2);
            }
        }
    }
}
