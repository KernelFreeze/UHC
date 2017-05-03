package me.kernelfreeze.uhc.scenarios;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.player.*;

public class ExtraInventoryCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("extrainventory")) {
            if (!ScenarioManager.getInstance().getScenarioExact("ExtraInventory").isEnabled()) {
                commandSender.sendMessage("§cExtraInventory scenario is not currently enabled!");
                return true;
            }
            if (!GameManager.getGameManager().isGameRunning()) {
                commandSender.sendMessage("§cA UHC is not currently running!");
                return true;
            }
            final Player player = (Player)commandSender;
            if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
                player.sendMessage("§cYou cannot use this command while spectating!");
                return true;
            }
            player.openInventory(player.getEnderChest());
            player.sendMessage("§aOpened extra inventory!");
        }
        return false;
    }
}
