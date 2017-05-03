package me.kernelfreeze.uhc.game;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.teams.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.*;
import me.kernelfreeze.uhc.teams.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class ScoreboardM
{
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private Map<Player, Scoreboard> playersScoreboard;
    
    public ScoreboardM() {
        this.playerManager = PlayerManager.getPlayerManager();
        this.gameManager = GameManager.getGameManager();
        this.playersScoreboard = UHC.getInstance().getPlayersScoreboard();
    }
    
    public void newScoreboard(final Player player) {
        final ChatColor mainColor = this.gameManager.getMainColor();
        if (player != null) {
            final Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            final Objective registerNewObjective = newScoreboard.registerNewObjective("h", "health");
            final Objective registerNewObjective2 = newScoreboard.registerNewObjective("h1", "health");
            final Objective registerNewObjective3 = newScoreboard.registerNewObjective("uhc", "dummy");
            registerNewObjective3.setDisplayName(this.gameManager.getScoreboardTitle());
            registerNewObjective3.setDisplaySlot(DisplaySlot.SIDEBAR);
            registerNewObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            registerNewObjective2.setDisplayName("§4\u2764");
            registerNewObjective2.setDisplaySlot(DisplaySlot.BELOW_NAME);
            newScoreboard.registerNewTeam(ChatColor.GRAY.toString()).addEntry("§c");
            registerNewObjective3.getScore("§c").setScore(13);
            final Team registerNewTeam = newScoreboard.registerNewTeam(ChatColor.GOLD.toString());
            registerNewTeam.addEntry(mainColor + "Time:");
            registerNewTeam.setSuffix(" §f00:00:00");
            registerNewObjective3.getScore(mainColor + "Time:").setScore(12);
            newScoreboard.registerNewTeam(ChatColor.DARK_AQUA.toString()).addEntry("§d");
            registerNewObjective3.getScore("§d").setScore(11);
            final Team registerNewTeam2 = newScoreboard.registerNewTeam(ChatColor.AQUA.toString());
            registerNewTeam2.addEntry(mainColor + "Players Ali");
            registerNewTeam2.setSuffix(mainColor + "ve: §f" + this.playerManager.alivePlayers());
            registerNewObjective3.getScore(mainColor + "Players Ali").setScore(10);
            newScoreboard.registerNewTeam(ChatColor.DARK_GRAY.toString()).addEntry("§e");
            registerNewObjective3.getScore("§e").setScore(7);
            final Team registerNewTeam3 = newScoreboard.registerNewTeam(ChatColor.BLACK.toString());
            registerNewTeam3.addEntry(mainColor + "Spectators");
            registerNewTeam3.setSuffix(mainColor + ": §f" + this.playerManager.spectators());
            registerNewObjective3.getScore(mainColor + "Spectators").setScore(8);
            final Team registerNewTeam4 = newScoreboard.registerNewTeam(ChatColor.BLUE.toString());
            registerNewTeam4.addEntry(mainColor + "Your Kills:");
            registerNewTeam4.setSuffix(" §f" + this.playerManager.getUHCPlayer(player.getUniqueId()).getKills());
            registerNewObjective3.getScore(mainColor + "Your Kills:").setScore(6);
            newScoreboard.registerNewTeam(ChatColor.LIGHT_PURPLE.toString()).addEntry("§5");
            registerNewObjective3.getScore("§5").setScore(4);
            newScoreboard.registerNewTeam(ChatColor.RED.toString()).addEntry("§f");
            registerNewObjective3.getScore("§f").setScore(2);
            final Team registerNewTeam5 = newScoreboard.registerNewTeam(ChatColor.GREEN.toString());
            registerNewTeam5.addEntry(mainColor + "Border:");
            registerNewTeam5.setSuffix(" §f" + this.gameManager.getCurrentBorder());
            registerNewObjective3.getScore(mainColor + "Border:").setScore(3);
            if (TeamManager.getInstance().isTeamsEnabled()) {
                final me.kernelfreeze.uhc.teams.Team team = TeamManager.getInstance().getTeam((OfflinePlayer)player);
                final Team registerNewTeam6 = newScoreboard.registerNewTeam(ChatColor.DARK_GREEN.toString());
                registerNewTeam6.addEntry(mainColor + "Team Kills:");
                registerNewTeam6.setSuffix(" §f" + team.getKills());
                registerNewObjective3.getScore(mainColor + "Team Kills:").setScore(5);
                final Team registerNewTeam7 = newScoreboard.registerNewTeam(ChatColor.YELLOW.toString());
                registerNewTeam7.addEntry(mainColor + "Teams Ali");
                registerNewTeam7.setSuffix(mainColor + "ve: §f" + TeamManager.getInstance().getTeamsAlive());
                registerNewObjective3.getScore(mainColor + "Teams Ali").setScore(9);
            }
            final String scoreboardIP = this.gameManager.getScoreboardIP();
            final int n = scoreboardIP.length() / 2;
            final String[] array = { scoreboardIP.substring(0, n), scoreboardIP.substring(n) };
            final Team registerNewTeam8 = newScoreboard.registerNewTeam(ChatColor.DARK_RED.toString());
            registerNewTeam8.addEntry(array[0]);
            registerNewTeam8.setSuffix(array[1]);
            registerNewObjective3.getScore(array[0]).setScore(1);
            this.playersScoreboard.put(player, newScoreboard);
            player.setScoreboard((Scoreboard)this.playersScoreboard.get(player));
        }
    }
    
    void updateScoreboard(final Player player, final int n) {
        if (player != null) {
            if (!this.playersScoreboard.containsKey(player)) {
                this.newScoreboard(player);
            }
            else {
                final ChatColor mainColor = this.gameManager.getMainColor();
                this.playersScoreboard.get(player).getTeam(ChatColor.GOLD.toString()).setSuffix(" §f" + this.convert(n));
                this.playersScoreboard.get(player).getTeam(ChatColor.AQUA.toString()).setSuffix(mainColor + "ve: §f" + this.playerManager.alivePlayers());
                this.playersScoreboard.get(player).getTeam(ChatColor.BLACK.toString()).setSuffix(mainColor + ": §f" + this.playerManager.spectators());
                if (TeamManager.getInstance().isTeamsEnabled()) {
                    this.playersScoreboard.get(player).getTeam(ChatColor.YELLOW.toString()).setSuffix(mainColor + "ve: §f" + TeamManager.getInstance().getTeamsAlive());
                }
            }
        }
    }
    
    public void updateBorder() {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player != null) {
                if (!this.playersScoreboard.containsKey(player)) {
                    this.newScoreboard(player);
                }
                else {
                    this.playersScoreboard.get(player).getTeam(ChatColor.GREEN.toString()).setSuffix(" §f" + this.gameManager.getCurrentBorder());
                }
            }
        }
    }
    
    public void updateKills(final Player player) {
        if (player != null) {
            if (!this.playersScoreboard.containsKey(player)) {
                this.newScoreboard(player);
            }
            else {
                this.playersScoreboard.get(player).getTeam(ChatColor.BLUE.toString()).setSuffix(" §f" + this.playerManager.getUHCPlayer(player.getUniqueId()).getKills());
            }
        }
    }
    
    public void updateTeamKills(final me.kernelfreeze.uhc.teams.Team team) {
        final Iterator<UUID> iterator = team.getPlayers().iterator();
        while (iterator.hasNext()) {
            final Player player = Bukkit.getServer().getPlayer((UUID)iterator.next());
            if (player != null) {
                if (!this.playersScoreboard.containsKey(player)) {
                    this.newScoreboard(player);
                }
                else {
                    this.playersScoreboard.get(player).getTeam(ChatColor.DARK_GREEN.toString()).setSuffix(" §f" + team.getKills());
                }
            }
        }
    }
    
    private String convert(final int n) {
        final int n2 = n / 3600;
        final int n3 = n - n2 * 3600;
        final int n4 = n3 / 60;
        final int n5 = n3 - n4 * 60;
        String string = "";
        if (n2 < 10) {
            string += "0";
        }
        String s = string + n2 + ":";
        if (n4 < 10) {
            s += "0";
        }
        String s2 = s + n4 + ":";
        if (n5 < 10) {
            s2 += "0";
        }
        return s2 + n5;
    }
}
