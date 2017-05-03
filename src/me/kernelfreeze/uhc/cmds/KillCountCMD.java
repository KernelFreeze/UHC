package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.teams.*;
import me.kernelfreeze.uhc.player.*;

public class KillCountCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("killcount")) {
            if (!GameManager.getGameManager().isGameRunning()) {
                commandSender.sendMessage("§cA UHC is not currently running!");
                return true;
            }
            if (array.length == 0) {
                commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "You have " + GameManager.getGameManager().getSecondaryColor() + PlayerManager.getPlayerManager().getUHCPlayer(((Player)commandSender).getUniqueId()).getKills() + GameManager.getGameManager().getMainColor() + " kills.");
                final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)commandSender);
                if (TeamManager.getInstance().isTeamsEnabled() && team != null) {
                    commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "You have " + GameManager.getGameManager().getSecondaryColor() + team.getKills() + GameManager.getGameManager().getMainColor() + " team kills.");
                }
                return true;
            }
            if (array.length > 0) {
                final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(array[0]);
                if (offlinePlayer == null) {
                    commandSender.sendMessage("§cCould not find player!");
                    return true;
                }
                final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(offlinePlayer.getUniqueId());
                if (uhcPlayer == null) {
                    commandSender.sendMessage("§cThis player hasn't played this UHC!");
                    return true;
                }
                commandSender.sendMessage(GameManager.getGameManager().getMainColor() + offlinePlayer.getName() + " has " + GameManager.getGameManager().getSecondaryColor() + uhcPlayer.getKills() + GameManager.getGameManager().getMainColor() + " kills.");
                final Team team2 = TeamManager.getInstance().getTeam(offlinePlayer);
                if (TeamManager.getInstance().isTeamsEnabled() && team2 != null) {
                    commandSender.sendMessage(GameManager.getGameManager().getMainColor() + offlinePlayer.getName() + " has " + GameManager.getGameManager().getSecondaryColor() + team2.getKills() + GameManager.getGameManager().getMainColor() + " team kills.");
                }
                return true;
            }
        }
        return false;
    }
}
