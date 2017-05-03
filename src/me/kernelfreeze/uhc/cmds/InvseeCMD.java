package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class InvseeCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("invsee")) {
            if (!GameManager.getGameManager().getModerators().contains(commandSender)) {
                commandSender.sendMessage("§cNo Permission!");
                return true;
            }
            if (array.length < 1) {
                commandSender.sendMessage("§c/invsee <player>");
                return true;
            }
            final Player player = Bukkit.getServer().getPlayer(array[0]);
            if (player == null) {
                commandSender.sendMessage("§cCould not find player!");
                return true;
            }
            ((Player)commandSender).openInventory((Inventory)player.getInventory());
            return true;
        }
        else {
            if (!command.getName().equalsIgnoreCase("armour")) {
                return false;
            }
            if (!GameManager.getGameManager().getModerators().contains(commandSender)) {
                commandSender.sendMessage("§cNo Permission!");
                return true;
            }
            if (array.length < 1) {
                commandSender.sendMessage("§c/armour <player>");
                return true;
            }
            final Player player2 = Bukkit.getServer().getPlayer(array[0]);
            if (player2 == null) {
                commandSender.sendMessage("§cCould not find player!");
                return true;
            }
            final Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)null, 9, "§aEquipped");
            inventory.setContents(player2.getInventory().getArmorContents());
            ((Player)commandSender).openInventory(inventory);
            return true;
        }
    }
}
