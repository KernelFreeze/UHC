package me.kernelfreeze.uhc.cmds;

import java.util.*;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.entity.*;

public class ReScatterCMD implements CommandExecutor
{
    private final Set<CommandSender> used;
    
    public ReScatterCMD() {
        this.used = new HashSet<CommandSender>();
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getName().equalsIgnoreCase("rescatter")) {
            return false;
        }
        if (!commandSender.hasPermission("uhc.rescatter")) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (GameManager.getGameManager().canUseRescatter()) {
            commandSender.sendMessage("§cYou can no longer use this command!");
            return true;
        }
        if (this.used.contains(commandSender)) {
            commandSender.sendMessage("§cYou can only use this command once!");
            return true;
        }
        GameManager.getGameManager().scatterPlayer((Player)commandSender);
        this.used.add(commandSender);
        commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Successfully re-scattered!");
        return true;
    }
}
