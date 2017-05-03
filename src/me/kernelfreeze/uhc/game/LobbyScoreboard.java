package me.kernelfreeze.uhc.game;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.*;
import java.util.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;

public class LobbyScoreboard
{
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private Map<Player, Scoreboard> plsScoreboard;
    
    public LobbyScoreboard() {
        this.playerManager = PlayerManager.getPlayerManager();
        this.gameManager = GameManager.getGameManager();
        this.plsScoreboard = UHC.getInstance().getLobbyScoreboardMap();
    }
    
    public void newScoreboard(final Player player) {
        final ChatColor mainColor = this.gameManager.getMainColor();
        if (player != null) {
            final Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            final Objective registerNewObjective = newScoreboard.registerNewObjective("lobby", "dummy");
            registerNewObjective.setDisplayName(this.gameManager.getScoreboardTitle());
            registerNewObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            newScoreboard.registerNewTeam(ChatColor.GRAY.toString()).addEntry("§c");
            registerNewObjective.getScore("§c").setScore(9);
            final Team registerNewTeam = newScoreboard.registerNewTeam(ChatColor.BLUE.toString());
            registerNewTeam.addEntry(mainColor + "Players Onli");
            registerNewTeam.setSuffix("ne: §f" + this.playerManager.online());
            registerNewObjective.getScore(mainColor + "Players Onli").setScore(8);
            newScoreboard.registerNewTeam(ChatColor.LIGHT_PURPLE.toString()).addEntry("§a");
            registerNewObjective.getScore("§a").setScore(7);
            newScoreboard.registerNewTeam(ChatColor.GOLD.toString()).addEntry(mainColor + "> Host:");
            registerNewObjective.getScore(mainColor + "> Host:").setScore(6);
            final Team registerNewTeam2 = newScoreboard.registerNewTeam(ChatColor.AQUA.toString());
            registerNewTeam2.addEntry("§f");
            registerNewTeam2.setSuffix(this.gameManager.getHostName());
            registerNewObjective.getScore("§f").setScore(5);
            newScoreboard.registerNewTeam(ChatColor.DARK_PURPLE.toString()).addEntry("§d");
            registerNewObjective.getScore("§d").setScore(4);
            final Team registerNewTeam3 = newScoreboard.registerNewTeam(ChatColor.BLACK.toString());
            registerNewTeam3.addEntry(mainColor + "Type:");
            registerNewTeam3.setSuffix("§f " + this.gameManager.gameType());
            registerNewObjective.getScore(mainColor + "Type:").setScore(3);
            newScoreboard.registerNewTeam(ChatColor.RED.toString()).addEntry("§e");
            registerNewObjective.getScore("§e").setScore(2);
            final String scoreboardIP = this.gameManager.getScoreboardIP();
            final int n = scoreboardIP.length() / 2;
            final String[] array = { scoreboardIP.substring(0, n), scoreboardIP.substring(n) };
            final Team registerNewTeam4 = newScoreboard.registerNewTeam(ChatColor.DARK_RED.toString());
            registerNewTeam4.addEntry(array[0]);
            registerNewTeam4.setSuffix(array[1]);
            registerNewObjective.getScore(array[0]).setScore(1);
            this.plsScoreboard.put(player, newScoreboard);
            player.setScoreboard((Scoreboard)this.plsScoreboard.get(player));
        }
    }
    
    public void updateScoreboard(final Player player) {
        if (player != null) {
            if (!this.plsScoreboard.containsKey(player)) {
                this.newScoreboard(player);
            }
            else {
                this.plsScoreboard.get(player).getTeam(ChatColor.BLUE.toString()).setSuffix(this.gameManager.getMainColor() + "ne: §f" + this.playerManager.online());
                this.plsScoreboard.get(player).getTeam(ChatColor.AQUA.toString()).setSuffix(this.gameManager.getHostName());
                this.plsScoreboard.get(player).getTeam(ChatColor.BLACK.toString()).setSuffix("§f " + this.gameManager.gameType());
            }
        }
    }
}
