package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

@SuppressWarnings("deprecation")
public class WhitelistCMD implements CommandExecutor
{
    private GameManager gameManager;

    public WhitelistCMD() {
        this.gameManager = GameManager.getGameManager();
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("whitelist")) {
            if (!commandSender.hasPermission("uhc.whitelist") && !this.gameManager.getModerators().contains(commandSender)) {
                commandSender.sendMessage("§cNo Permission!");
                return true;
            }
            final ChatColor mainColor = this.gameManager.getMainColor();
            final ChatColor secondaryColor = this.gameManager.getSecondaryColor();
            if (array.length == 0) {
                commandSender.sendMessage(secondaryColor + "---------------");
                commandSender.sendMessage(mainColor + "§nWhiteList commands: ");
                commandSender.sendMessage(" ");
                commandSender.sendMessage("§c/whitelist [add/remove] <player> - Remove/Add players to whitelist.");
                commandSender.sendMessage("§c/whitelist [on/off] - Turns the whitelist on/off.");
                commandSender.sendMessage("§c/whitelist [clear] - Clears the whitelist.");
                commandSender.sendMessage("§c/whitelist [all] - Whitelists all the players.");
                commandSender.sendMessage(secondaryColor + "---------------");
                return true;
            }
            if (array.length == 1) {
                if (array[0].equalsIgnoreCase("on")) {
                    Bukkit.getServer().setWhitelist(true);
                    commandSender.sendMessage(mainColor + "Whitelist: §aOn.");
                    return true;
                }
                if (array[0].equalsIgnoreCase("off")) {
                    Bukkit.getServer().setWhitelist(false);
                    commandSender.sendMessage(mainColor + "Whitelist: §cOff.");
                    return true;
                }
                if (array[0].equalsIgnoreCase("clear")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.setWhitelisted(false);
                    }
                    commandSender.sendMessage(mainColor + "Whitelist: " + secondaryColor + " cleared.");
                    return true;
                }
                if (array[0].equalsIgnoreCase("all")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.setWhitelisted(true);
                    }
                    commandSender.sendMessage(mainColor + "Whitelist: " + secondaryColor + " added all.");
                    return true;
                }
                commandSender.sendMessage(secondaryColor + "---------------");
                commandSender.sendMessage(mainColor + "§nWhiteList commands: ");
                commandSender.sendMessage(" ");
                commandSender.sendMessage("§c/whitelist [add/remove] <player> - Remove/Add players to whitelist.");
                commandSender.sendMessage("§c/whitelist [on/off] - Turns the whitelist on/off.");
                commandSender.sendMessage("§c/whitelist [clear] - Clears the whitelist.");
                commandSender.sendMessage("§c/whitelist [all] - Whitelists all the players.");
                commandSender.sendMessage(secondaryColor + "---------------");
                return true;
            }
            else if (array.length > 1) {
                final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(array[1]);
                if (array[0].equalsIgnoreCase("add")) {
                    if (offlinePlayer == null) {
                        commandSender.sendMessage("§cCould not find player!"); // No deberia pasar nunca pero bueno...
                        return true;
                    }
                    offlinePlayer.setWhitelisted(true);
                    commandSender.sendMessage(mainColor + "Whitelist: " + secondaryColor + " added " + offlinePlayer.getName());
                    return true;
                }
                else if (array[0].equalsIgnoreCase("remove")) {
                    if (offlinePlayer == null) {
                        commandSender.sendMessage("§cCould not find player!");
                        return true;
                    }
                    offlinePlayer.setWhitelisted(false);
                    commandSender.sendMessage(mainColor + "Whitelist: " + secondaryColor + " removed " + offlinePlayer.getName());
                    return true;
                }
            }
        }
        return false;
    }
}
