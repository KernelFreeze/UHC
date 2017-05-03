package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.*;

public class ListCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("list")) {
            final ChatColor mainColor = GameManager.getGameManager().getMainColor();
            final ChatColor secondaryColor = GameManager.getGameManager().getSecondaryColor();
            commandSender.sendMessage(secondaryColor + "----------------");
            commandSender.sendMessage(mainColor + "Host: §f" + GameManager.getGameManager().getHostName());
            commandSender.sendMessage(mainColor + "Total Online: §f" + PlayerManager.getPlayerManager().online());
            commandSender.sendMessage(mainColor + "Players Alive: §f" + PlayerManager.getPlayerManager().alivePlayers());
            commandSender.sendMessage(mainColor + "Spectators: §f" + PlayerManager.getPlayerManager().spectators());
            commandSender.sendMessage(mainColor + "Moderators: §f" + GameManager.getGameManager().getModeratorsNames().toString());
            commandSender.sendMessage(secondaryColor + "----------------");
        }
        return false;
    }
}
