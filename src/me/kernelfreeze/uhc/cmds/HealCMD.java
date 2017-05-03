package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class HealCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getName().equalsIgnoreCase("heal")) {
            return false;
        }
        if (!GameManager.getGameManager().getModerators().contains(commandSender)) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (array.length < 1) {
            commandSender.sendMessage("§c/heal <player/all>");
            return true;
        }
        if (array[0].equalsIgnoreCase("all")) {
            for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.setHealth(20.0);
                player.sendMessage("§aYou have been healed!");
            }
            commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Healed all!");
            return true;
        }
        final Player player2 = Bukkit.getPlayer(array[0]);
        if (player2 == null) {
            commandSender.sendMessage("§cCould not find player!");
            return true;
        }
        player2.setHealth(20.0);
        player2.sendMessage("§aYou have been healed!");
        commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Healed: " + player2.getName());
        return true;
    }
}
