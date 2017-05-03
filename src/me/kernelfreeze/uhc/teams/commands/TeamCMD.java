package me.kernelfreeze.uhc.teams.commands;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import me.kernelfreeze.uhc.teams.request.Request;
import me.kernelfreeze.uhc.teams.request.RequestManager;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.teams.*;
import me.kernelfreeze.uhc.teams.request.*;

public class TeamCMD implements CommandExecutor
{
    private final GameManager gameManager;
    
    public TeamCMD() {
        this.gameManager = GameManager.getGameManager();
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("team")) {
            if (array.length < 1) {
                commandSender.sendMessage(this.gameManager.getSecondaryColor() + "-------------------------------");
                commandSender.sendMessage(this.gameManager.getMainColor() + "" + ChatColor.UNDERLINE + "Team commands: ");
                commandSender.sendMessage(" ");
                commandSender.sendMessage(ChatColor.GRAY + "1)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team create" + ChatColor.RED + " to create a team.");
                commandSender.sendMessage(ChatColor.GRAY + "2)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team solo" + ChatColor.RED + " to be alone in your team.");
                commandSender.sendMessage(ChatColor.GRAY + "3)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team invite <player>" + ChatColor.RED + " to invite players to your team.");
                commandSender.sendMessage(ChatColor.GRAY + "4)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team accept" + ChatColor.RED + " to accept a team invitation.");
                commandSender.sendMessage(ChatColor.GRAY + "5)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team deny" + ChatColor.RED + " to deny a team invitation.");
                commandSender.sendMessage(ChatColor.GRAY + "6)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team leave" + ChatColor.RED + " to leave your current team.");
                commandSender.sendMessage(ChatColor.GRAY + "7)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team kick <player>" + ChatColor.RED + " to kick a player from your team.");
                commandSender.sendMessage(ChatColor.GRAY + "8)" + ChatColor.RED + " Use - " + ChatColor.RED + "/team list <player>" + ChatColor.RED + " to list someone's team.");
                commandSender.sendMessage(this.gameManager.getSecondaryColor() + "-------------------------------");
                return true;
            }
            if (array[0].equalsIgnoreCase("list")) {
                Player player = (Player)commandSender;
                if (array.length == 2) {
                    player = Bukkit.getPlayer(array[1]);
                }
                if (player == null) {
                    commandSender.sendMessage("§cCouldn't find player.");
                    return true;
                }
                final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)player);
                if (team != null) {
                    commandSender.sendMessage(team.toString());
                    return true;
                }
                if (player == commandSender) {
                    commandSender.sendMessage(ChatColor.RED + "You are not in a team!");
                    commandSender.sendMessage(this.gameManager.getMainColor() + "Use - " + this.gameManager.getSecondaryColor() + "/team create" + this.gameManager.getMainColor() + " to create a team");
                    return true;
                }
                commandSender.sendMessage(ChatColor.RED + player.getName() + " is not in a team!");
                return true;
            }
            else {
                if (this.gameManager.isScattering() || this.gameManager.isGameRunning()) {
                    commandSender.sendMessage("§cYou cannot edit teams now!");
                    return true;
                }
                final Player player2 = (Player)commandSender;
                if (commandSender.hasPermission("uhc.teams.edit") || this.gameManager.getHostName().equalsIgnoreCase(player2.getName())) {
                    if (array[0].equalsIgnoreCase("reset")) {
                        TeamManager.getInstance().clearTeams();
                        commandSender.sendMessage("§aSuccessfully restarted all teams!");
                        return true;
                    }
                    if (array[0].equalsIgnoreCase("friendlyfire") || array[0].equalsIgnoreCase("damageteam") || array[0].equalsIgnoreCase("damageteammembers")) {
                        if (array.length == 1) {
                            commandSender.sendMessage(ChatColor.RED + "/team damageteam [true/false]");
                            return true;
                        }
                        if (array[1].equalsIgnoreCase("true") || array[1].equalsIgnoreCase("enable")) {
                            TeamManager.getInstance().setCanDamageTeamMembers(true);
                            Bukkit.getServer().broadcastMessage(TeamManager.getInstance().getTeamsPrefix() + this.gameManager.getMainColor() + commandSender.getName() + " has set " + this.gameManager.getSecondaryColor() + "Damage Team Members" + this.gameManager.getMainColor() + " to " + ChatColor.GREEN + "True");
                            return true;
                        }
                        if (array[1].equalsIgnoreCase("false") || array[1].equalsIgnoreCase("disable")) {
                            TeamManager.getInstance().setCanDamageTeamMembers(false);
                            Bukkit.getServer().broadcastMessage(TeamManager.getInstance().getTeamsPrefix() + this.gameManager.getMainColor() + commandSender.getName() + " has set " + this.gameManager.getSecondaryColor() + "Damage Team Members" + this.gameManager.getMainColor() + " to " + ChatColor.RED + "False");
                            return true;
                        }
                    }
                    if (array[0].equalsIgnoreCase("size")) {
                        if (array.length == 1) {
                            commandSender.sendMessage(ChatColor.RED + "/team size <size>");
                            return true;
                        }
                        try {
                            final int int1 = Integer.parseInt(array[1]);
                            TeamManager.getInstance().setMaxSize(int1);
                            Bukkit.getServer().broadcastMessage(TeamManager.getInstance().getTeamsPrefix() + this.gameManager.getMainColor() + commandSender.getName() + " has set the " + this.gameManager.getSecondaryColor() + "Team Size" + this.gameManager.getMainColor() + " to " + ChatColor.GREEN + int1);
                        }
                        catch (NumberFormatException ex) {
                            commandSender.sendMessage(ChatColor.RED + "The team size must be a number!");
                        }
                        return true;
                    }
                    else {
                        if (array[0].equalsIgnoreCase("true") || array[0].equalsIgnoreCase("enable")) {
                            TeamManager.getInstance().setTeamsEnabled(true);
                            Bukkit.getServer().broadcastMessage(TeamManager.getInstance().getTeamsPrefix() + this.gameManager.getMainColor() + commandSender.getName() + " has set " + this.gameManager.getSecondaryColor() + "Teams" + this.gameManager.getMainColor() + " to " + ChatColor.GREEN + "True");
                            return true;
                        }
                        if (array[0].equalsIgnoreCase("false") || array[0].equalsIgnoreCase("disable")) {
                            TeamManager.getInstance().setTeamsEnabled(false);
                            Bukkit.getServer().broadcastMessage(TeamManager.getInstance().getTeamsPrefix() + this.gameManager.getMainColor() + commandSender.getName() + " has set " + this.gameManager.getSecondaryColor() + "Teams" + this.gameManager.getMainColor() + " to " + ChatColor.RED + "False");
                            return true;
                        }
                    }
                }
                if (!TeamManager.getInstance().isTeamsEnabled()) {
                    commandSender.sendMessage(ChatColor.RED + "Teams are currently disabled!");
                    return true;
                }
                if (array[0].equalsIgnoreCase("create") || array[0].equalsIgnoreCase("solo")) {
                    if (TeamManager.getInstance().getTeam((OfflinePlayer)player2) != null) {
                        commandSender.sendMessage("§cYou are already in a team, use /team leave to leave your current team.");
                        return true;
                    }
                    TeamManager.getInstance().createTeam(player2);
                    return true;
                }
                else if (array[0].equalsIgnoreCase("accept")) {
                    final Request request = RequestManager.getInstance().getRequest(player2);
                    if (request == null) {
                        commandSender.sendMessage(ChatColor.RED + "You don't have any pending team invites!");
                        return true;
                    }
                    TeamManager.getInstance().registerTeam(player2, request.getTeam());
                    RequestManager.getInstance().requestMap.remove(player2);
                    return true;
                }
                else if (array[0].equalsIgnoreCase("deny")) {
                    if (RequestManager.getInstance().getRequest(player2) == null) {
                        player2.sendMessage(ChatColor.RED + "You don't have any pending invites.");
                        return true;
                    }
                    RequestManager.getInstance().declined(player2);
                    return true;
                }
                else if (array[0].equalsIgnoreCase("invite")) {
                    if (array.length == 1) {
                        player2.sendMessage(ChatColor.RED + "/team invite <player>");
                        return true;
                    }
                    final Player player3 = Bukkit.getServer().getPlayer(array[1]);
                    if (player3 == null) {
                        player2.sendMessage(ChatColor.RED + "Could not find player!");
                        return true;
                    }
                    if (player3 == player2) {
                        player2.sendMessage(ChatColor.RED + "You cannot invite yourself to the team!");
                        return true;
                    }
                    final Team team2 = TeamManager.getInstance().getTeam((OfflinePlayer)player2);
                    if (team2 == null) {
                        TeamManager.getInstance().createTeam(player2);
                        RequestManager.getInstance().sendRequest(player2, player3, TeamManager.getInstance().getTeam((OfflinePlayer)player2));
                        return true;
                    }
                    if (team2.getOwner() != player2) {
                        commandSender.sendMessage(ChatColor.RED + "You must be the team leader to invite players to the team!");
                        return true;
                    }
                    RequestManager.getInstance().sendRequest(player2, player3, team2);
                    return true;
                }
                else if (array[0].equalsIgnoreCase("kick")) {
                    if (array.length == 1) {
                        player2.sendMessage(ChatColor.RED + "/team kick <player>");
                        return true;
                    }
                    final Team team3 = TeamManager.getInstance().getTeam((OfflinePlayer)player2);
                    if (team3 == null) {
                        player2.sendMessage(ChatColor.RED + "You are not in a team!");
                        return true;
                    }
                    if (team3.getOwner() != player2) {
                        commandSender.sendMessage(ChatColor.RED + "You must be the team leader to kick players out of the team!");
                        return true;
                    }
                    final OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(array[1]);
                    if (offlinePlayer == null) {
                        commandSender.sendMessage(ChatColor.RED + "Could not find player!");
                        return true;
                    }
                    if (offlinePlayer == player2) {
                        commandSender.sendMessage(ChatColor.RED + "You cannot kick yourself out of the team, use /team leave to leave the team!");
                        return true;
                    }
                    if (!team3.getPlayers().contains(offlinePlayer.getUniqueId())) {
                        commandSender.sendMessage(ChatColor.RED + "This player is not part of your team!");
                        return true;
                    }
                    TeamManager.getInstance().unregisterTeam(offlinePlayer.getUniqueId());
                    if (offlinePlayer.isOnline()) {
                        offlinePlayer.getPlayer().sendMessage(ChatColor.RED + "You were kicked from the team by " + commandSender.getName());
                    }
                    team3.sendMessage(ChatColor.RED + "" + offlinePlayer.getName() + " has been kicked from the team.");
                    return true;
                }
                else if (array[0].equalsIgnoreCase("leave")) {
                    if (TeamManager.getInstance().getTeam((OfflinePlayer)player2) == null) {
                        commandSender.sendMessage(ChatColor.RED + "You are not part of any team!");
                        return true;
                    }
                    TeamManager.getInstance().unregisterTeam(player2.getUniqueId());
                    return true;
                }
            }
        }
        return false;
    }
}
