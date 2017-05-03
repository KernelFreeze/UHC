package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.player.*;

public class SpectatorCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("spectator")) {
            if (!GameManager.getGameManager().getModerators().contains(commandSender)) {
                commandSender.sendMessage("§cYou must be a moderator to use this command!");
                return true;
            }
            if (array.length < 2) {
                commandSender.sendMessage("§c/spectator [add/remove] [player]");
                return true;
            }
            if (array[0].equalsIgnoreCase("add")) {
                final Player player = Bukkit.getServer().getPlayer(array[1]);
                if (player == null) {
                    commandSender.sendMessage("§cCould not find player!");
                    return true;
                }
                PlayerManager.getPlayerManager().setSpectating(true, player);
                commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Successfully added " + player.getName() + " to the spectators!");
                return true;
            }
            else if (array[0].equalsIgnoreCase("remove")) {
                final Player player2 = Bukkit.getServer().getPlayer(array[1]);
                if (player2 == null) {
                    commandSender.sendMessage("§cCould not find player!");
                    return true;
                }
                PlayerManager.getPlayerManager().setSpectating(false, player2);
                commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Successfully removed " + player2.getName() + " from the spectators!");
                return true;
            }
        }
        return false;
    }
}
