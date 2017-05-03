package me.kernelfreeze.uhc.teams.commands;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import me.kernelfreeze.uhc.teams.*;

public class TeamChatCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("teamchat")) {
            if (!TeamManager.getInstance().isTeamsEnabled()) {
                commandSender.sendMessage("§cTeams are currently disabled!");
                return true;
            }
            if (array.length == 1 && array[0].equalsIgnoreCase("toggle")) {
                PlayerManager.getPlayerManager().getUHCPlayer(((Player)commandSender).getUniqueId()).toggleTeamChat(commandSender);
                return true;
            }
            if (array.length > 0) {
                if (commandSender instanceof Player) {
                    final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)commandSender);
                    if (team != null) {
                        final StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < array.length; ++i) {
                            final String s2 = array[i];
                            if (i != 0) {
                                sb.append(" ");
                            }
                            sb.append(s2);
                        }
                        team.sendMessage(TeamManager.getInstance().getTeamsPrefix() + GameManager.getGameManager().getSecondaryColor() + commandSender.getName() + ": " + GameManager.getGameManager().getMainColor() + sb.toString());
                    }
                }
            }
            else {
                commandSender.sendMessage("§c/teamchat <message>");
            }
        }
        return false;
    }
}
