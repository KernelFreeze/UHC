package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import java.text.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class HealthCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("health")) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            if (array.length < 1) {
                commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Your Health: §a" + decimalFormat.format(((Player)commandSender).getHealth() / 2.0) + "§4 \u2764");
                return true;
            }
            if (array.length > 0) {
                final Player player = Bukkit.getPlayer(array[0]);
                if (player == null) {
                    commandSender.sendMessage(ChatColor.RED + "Could not find player!");
                    return true;
                }
                commandSender.sendMessage(GameManager.getGameManager().getMainColor() + player.getName() + "'s Health: §a" + decimalFormat.format(player.getHealth() / 2.0) + "§4 \u2764");
                return true;
            }
        }
        return false;
    }
}
