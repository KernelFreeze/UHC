package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.player.*;

public class StatsCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("stats")) {
            if (!GameManager.getGameManager().isStatsEnabled()) {
                commandSender.sendMessage("§cStats are currently disabled!");
                return true;
            }
            final Player player = (Player)commandSender;
            if (array.length < 1) {
                player.openInventory(PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).getStatsInventory());
                return true;
            }
            if (array.length > 0) {
                final OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(array[0]);
                UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(offlinePlayer.getUniqueId());
                if (uhcPlayer == null) {
                    PlayerManager.getPlayerManager().createUHCPlayer(offlinePlayer.getUniqueId());
                    uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(offlinePlayer.getUniqueId());
                }
                if (!uhcPlayer.hasData() && !offlinePlayer.isOnline()) {
                    commandSender.sendMessage("§cThis player never played on this server!");
                    return true;
                }
                player.openInventory(uhcPlayer.getStatsInventory());
            }
        }
        return false;
    }
}
