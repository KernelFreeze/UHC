package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;

public class ModeratorCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("moderator")) {
            if (!commandSender.hasPermission("uhc.mod")) {
                commandSender.sendMessage("§cNo Permission!");
                return true;
            }
            if (array.length < 2) {
                commandSender.sendMessage("§c/mod add/remove <player>");
                return true;
            }
            final Player player = Bukkit.getServer().getPlayer(array[1]);
            if (player == null) {
                commandSender.sendMessage("§cCould not find player!");
                return true;
            }
            if (array[0].equalsIgnoreCase("add")) {
                if (GameManager.getGameManager().getModerators().contains(player)) {
                    commandSender.sendMessage("§cThis player is already a moderator!");
                    return true;
                }
                GameManager.getGameManager().setModerator(player, true);
                commandSender.sendMessage("§aSuccessfully added " + player.getName() + " to the moderators list.");
                return true;
            }
            else if (array[0].equalsIgnoreCase("remove")) {
                if (!GameManager.getGameManager().getModerators().contains(player)) {
                    commandSender.sendMessage("§cThis player is not a moderator!");
                    return true;
                }
                GameManager.getGameManager().setModerator(player, false);
                commandSender.sendMessage("§cSuccessfully removed " + player.getName() + " from the moderators list.");
            }
        }
        return true;
    }
}
