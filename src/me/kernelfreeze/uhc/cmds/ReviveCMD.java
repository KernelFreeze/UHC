package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class ReviveCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getName().equalsIgnoreCase("revive")) {
            return false;
        }
        if (!commandSender.hasPermission("uhc.revive") && !GameManager.getGameManager().getModerators().contains(commandSender)) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (array.length < 1) {
            commandSender.sendMessage("§c/revive <player>");
            return true;
        }
        final Player player = Bukkit.getServer().getPlayer(array[0]);
        if (player == null) {
            commandSender.sendMessage("§cCould not find player!");
            return true;
        }
        if (!GameManager.getGameManager().isGameRunning()) {
            commandSender.sendMessage("§cA UHC is not currently running!");
            return true;
        }
        GameManager.getGameManager().scatterPlayer(player);
        commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Successfully revived: " + GameManager.getGameManager().getSecondaryColor() + player.getName());
        return true;
    }
}
