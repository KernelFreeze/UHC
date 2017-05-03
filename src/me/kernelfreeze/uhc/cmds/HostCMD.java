package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;

public class HostCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("host")) {
            if (!commandSender.hasPermission("uhc.host")) {
                commandSender.sendMessage("§cNo Permission!");
                return true;
            }
            if (array.length < 2) {
                commandSender.sendMessage("§c/host add/remove <player>");
                return true;
            }
            final Player player = Bukkit.getServer().getPlayer(array[1]);
            if (player == null) {
                commandSender.sendMessage("§cCould not find player!");
                return true;
            }
            if (array[0].equalsIgnoreCase("add")) {
                if (GameManager.getGameManager().getHostName().equalsIgnoreCase(player.getName())) {
                    commandSender.sendMessage("§cThis player is already the host!");
                    return true;
                }
                if (GameManager.getGameManager().getHostName().equalsIgnoreCase(player.getName())) {
                    commandSender.sendMessage("§cThe host is already set, you must remove the current host first!");
                    return true;
                }
                GameManager.getGameManager().setHost(player);
                GameManager.getGameManager().setModerator(player, true);
                commandSender.sendMessage("§aSuccessfully set " + player.getName() + " as host.");
                return true;
            }
            else if (array[0].equalsIgnoreCase("remove")) {
                if (!GameManager.getGameManager().getHostName().equalsIgnoreCase(player.getName())) {
                    commandSender.sendMessage("§cThis player is not the host!");
                    return true;
                }
                GameManager.getGameManager().setHost(null);
                GameManager.getGameManager().setModerator(player, false);
                commandSender.sendMessage("§cSuccessfully removed " + player.getName() + " from host.");
            }
        }
        return true;
    }
}
