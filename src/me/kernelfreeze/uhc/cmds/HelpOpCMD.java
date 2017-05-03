package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.*;
import org.bukkit.plugin.*;
import java.util.*;

public class HelpOpCMD implements CommandExecutor
{
    private final Set<CommandSender> cooldown;
    private final GameManager gameManager;
    
    public HelpOpCMD() {
        this.cooldown = new HashSet<CommandSender>();
        this.gameManager = GameManager.getGameManager();
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("helpop")) {
            if (this.cooldown.contains(commandSender)) {
                commandSender.sendMessage("§cDo not spam /helpop.");
                return true;
            }
            if (array.length < 1) {
                commandSender.sendMessage("§c/helpop <message>");
                return true;
            }
            final StringBuilder sb = new StringBuilder();
            for (int length = array.length, i = 0; i < length; ++i) {
                sb.append(array[i]).append(" ");
            }
            for (final Player player : this.gameManager.getModerators()) {
                if (player != null) {
                    player.sendMessage(this.gameManager.getHelpopPrefix() + this.gameManager.getSecondaryColor() + commandSender.getName() + ": " + this.gameManager.getMainColor() + (Object)sb);
                }
            }
            commandSender.sendMessage(this.gameManager.getMainColor() + "HelpOp message sent!");
            this.cooldown.add(commandSender);
            Bukkit.getServer().getScheduler().runTaskLater((Plugin) UHC.getInstance(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    HelpOpCMD.this.cooldown.remove(commandSender);
                }
            }, (long)(20 * this.gameManager.getHelpopCoolDown()));
        }
        return false;
    }
}
