package me.kernelfreeze.uhc.teams.commands;

import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.command.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.player.*;
import me.kernelfreeze.uhc.teams.*;

public class TeamListCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("teamlist")) {
            if (!TeamManager.getInstance().isTeamsEnabled()) {
                commandSender.sendMessage("§cTeams are currently disabled!");
                return true;
            }
            if (array.length <= 0) {
                final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)commandSender);
                if (team == null) {
                    commandSender.sendMessage("§cYou are not part of any team!");
                    return true;
                }
                commandSender.sendMessage(team.toString());
                return true;
            }
            else if (array.length >= 1) {
                final OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(array[0]);
                if (offlinePlayer == null) {
                    commandSender.sendMessage("§cCould not find player!");
                    return true;
                }
                if (PlayerManager.getPlayerManager().getUHCPlayer(offlinePlayer.getUniqueId()) == null) {
                    commandSender.sendMessage("§cThis player hasn't played this UHC!");
                    return true;
                }
                final Team team2 = TeamManager.getInstance().getTeam(offlinePlayer);
                if (team2 == null) {
                    commandSender.sendMessage("§cThis player is not part of any team!");
                    return true;
                }
                commandSender.sendMessage(team2.toString());
                return true;
            }
        }
        return false;
    }
}
