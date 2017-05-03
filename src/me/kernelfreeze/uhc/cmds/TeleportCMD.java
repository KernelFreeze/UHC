package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.player.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class TeleportCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("teleport")) {
            if (!PlayerManager.getPlayerManager().getUHCPlayer(((Player)commandSender).getUniqueId()).isSpectating() && !GameManager.getGameManager().getModerators().contains(((Player)commandSender).getPlayer())) {
                commandSender.sendMessage("§cNo Permission!");
                return true;
            }
            if (array.length < 1) {
                commandSender.sendMessage("§c/tele [player/location]");
                if (GameManager.getGameManager().getModerators().contains(((Player)commandSender).getPlayer())) {
                    commandSender.sendMessage("§c/tele [player/location] [player/location]");
                    return true;
                }
                return true;
            }
            else {
                final Player player = (Player)commandSender;
                final ChatColor mainColor = GameManager.getGameManager().getMainColor();
                final ChatColor secondaryColor = GameManager.getGameManager().getSecondaryColor();
                if (array.length == 1) {
                    final Player player2 = Bukkit.getServer().getPlayer(array[0]);
                    if (player2 == null) {
                        commandSender.sendMessage("§cCould not find player!");
                        return true;
                    }
                    player.teleport((Entity)player2);
                    player.sendMessage(mainColor + "Teleported you to " + secondaryColor + player2.getName());
                    return true;
                }
                else if (array.length == 2) {
                    final Player player3 = Bukkit.getServer().getPlayer(array[1]);
                    final Player player4 = Bukkit.getServer().getPlayer(array[0]);
                    if (!array[0].equalsIgnoreCase(player.getName()) && !GameManager.getGameManager().getModerators().contains(commandSender)) {
                        commandSender.sendMessage("§cNo Permission!");
                        return true;
                    }
                    if (player4 == null) {
                        commandSender.sendMessage(ChatColor.RED + "Could not find player: " + array[0]);
                        return true;
                    }
                    if (player3 == null) {
                        commandSender.sendMessage("§cCould not find player: " + array[1]);
                        return true;
                    }
                    player4.teleport((Entity)player3);
                    commandSender.sendMessage(mainColor + "Teleported " + secondaryColor + player4.getName() + mainColor + " to: " + secondaryColor + player3.getName());
                    player4.sendMessage(secondaryColor + commandSender.getName() + mainColor + "teleported you to: " + secondaryColor + player3.getName());
                    return true;
                }
                else if (array.length == 3) {
                    if (!this.isNum(array[0]) && !this.isNum(array[1]) && !this.isNum(array[2])) {
                        commandSender.sendMessage("§cLocation must be a number");
                        return true;
                    }
                    final double double1 = Double.parseDouble(array[0]);
                    final double double2 = Double.parseDouble(array[1]);
                    final double double3 = Double.parseDouble(array[2]);
                    player.teleport(new Location(player.getWorld(), double1, double2, double3));
                    commandSender.sendMessage(mainColor + "Teleported to location: " + secondaryColor + double1 + ", " + double2 + ", " + double3);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isNum(final String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
