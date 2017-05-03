package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.plugin.*;

public class RespawnCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getName().equalsIgnoreCase("respawn")) {
            return false;
        }
        if (!commandSender.hasPermission("uhc.respawn.command") && !GameManager.getGameManager().getModerators().contains(commandSender)) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (array.length < 1) {
            commandSender.sendMessage("§c/respawn <player>");
            return true;
        }
        if (!GameManager.getGameManager().isGameRunning()) {
            commandSender.sendMessage("§cA UHC is not currently running!");
            return true;
        }
        final Player player = Bukkit.getPlayer(array[0]);
        if (player == null) {
            commandSender.sendMessage("§cCould not find player!");
            return true;
        }
        final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        if (uhcPlayer == null) {
            commandSender.sendMessage("§cThis player hasn't played this UHC!");
            return true;
        }
        if (uhcPlayer.isPlayerAlive()) {
            commandSender.sendMessage("§cThis player is still alive!");
            return true;
        }
        if (uhcPlayer.getRespawnLocation() == null) {
            commandSender.sendMessage("§cCould not find a respawn Location!");
            return true;
        }
        if (uhcPlayer.isSpectating()) {
            PlayerManager.getPlayerManager().setSpectating(false, player);
        }
        Bukkit.getScheduler().runTaskLater((Plugin) UHC.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.teleport(uhcPlayer.getRespawnLocation());
                player.getInventory().setArmorContents(uhcPlayer.lastArmour());
                player.getInventory().setContents(uhcPlayer.lastInventory());
                uhcPlayer.setPlayerAlive(true);
                uhcPlayer.setDied(false);
                commandSender.sendMessage("§aSuccessfully respawned: " + player.getName());
            }
        }, 65L);
        return true;
    }
}
