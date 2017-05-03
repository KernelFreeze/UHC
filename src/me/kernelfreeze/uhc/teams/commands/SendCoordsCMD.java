package me.kernelfreeze.uhc.teams.commands;

import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.teams.*;

public class SendCoordsCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("sendcoords")) {
            if (!TeamManager.getInstance().isTeamsEnabled()) {
                commandSender.sendMessage("§cTeams are currently disabled!");
                return true;
            }
            if (commandSender instanceof Player) {
                final OfflinePlayer offlinePlayer = (OfflinePlayer)commandSender;
                final Team team = TeamManager.getInstance().getTeam(offlinePlayer);
                if (team != null) {
                    team.sendMessage(TeamManager.getInstance().getTeamsPrefix() + ChatColor.GREEN + offlinePlayer.getName() + ":" + ("§b x: §e" + ((Player)commandSender).getLocation().getBlockX() + "§b y: §e" + ((Player)commandSender).getLocation().getBlockY() + "§b z: §e" + ((Player)commandSender).getLocation().getBlockZ()));
                }
                else {
                    commandSender.sendMessage("§cYou are not in a team!");
                }
            }
        }
        return false;
    }
}
