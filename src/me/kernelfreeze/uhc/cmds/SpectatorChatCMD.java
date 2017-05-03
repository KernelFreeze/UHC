package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.player.*;

public class SpectatorChatCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("spectatorchat")) {
            if (!GameManager.getGameManager().getModerators().contains(((Player)commandSender).getPlayer())) {
                commandSender.sendMessage("§cOnly moderators can use this command!");
                return true;
            }
            if (array.length < 1) {
                commandSender.sendMessage("§c/spectatorchat <message>");
                return true;
            }
            PlayerManager.getPlayerManager().getUHCPlayer(((Player)commandSender).getUniqueId()).setUsedCommand(true);
            final StringBuilder sb = new StringBuilder();
            for (int length = array.length, i = 0; i < length; ++i) {
                sb.append(array[i]).append(" ");
            }
            ((Player)commandSender).chat(sb.toString());
        }
        return false;
    }
}
