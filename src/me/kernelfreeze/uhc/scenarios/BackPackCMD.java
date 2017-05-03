package me.kernelfreeze.uhc.scenarios;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.teams.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.*;

public class BackPackCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("backpack")) {
            if (!TeamManager.getInstance().isTeamsEnabled()) {
                commandSender.sendMessage("§cTeams are not currently enabled!");
                return true;
            }
            if (!ScenarioManager.getInstance().getScenarioExact("BackPacks").isEnabled()) {
                commandSender.sendMessage("§cBackPacks scenario is not currently enabled!");
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
            if (TeamManager.getInstance().getTeam((OfflinePlayer)player) != null) {
                player.openInventory(TeamManager.getInstance().getTeam((OfflinePlayer)player.getPlayer()).getBackPack());
            }
        }
        return false;
    }
}
