package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.listeners.GameStopEvent;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.TeamManager;
import me.kernelfreeze.uhc.world.WorldCreator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.DisplaySlot;

public class StopGameCMD implements CommandExecutor {
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!commandSender.hasPermission("uhc.stop") && !GameManager.getGameManager().getHostName().equalsIgnoreCase(commandSender.getName())) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (!GameManager.getGameManager().isScattering() && !GameManager.getGameManager().isGameRunning()) {
            commandSender.sendMessage("§cA UHC is not currently running!");
            return true;
        }
        commandSender.sendMessage("§cStopping UHC...");
        GameManager.getGameManager().setGameRunning(false);
        new GameManager();
        new ScenarioManager();
        PlayerManager.getPlayerManager().getUHCPlayers().clear();
        new PlayerManager();
        TeamManager.getInstance().clearTeams();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
        }
        Bukkit.getServer().getPluginManager().callEvent(new GameStopEvent("Force stopped with command."));
        GameManager.getGameManager().setCanBorderShrink(false);
        new WorldCreator(true, true);
        commandSender.sendMessage("§cStopped UHC!");
        if (GameManager.getGameManager().restartOnEnd()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), GameManager.getGameManager().getRestartCommand().replace("/", ""));
        }
        return false;
    }
}
