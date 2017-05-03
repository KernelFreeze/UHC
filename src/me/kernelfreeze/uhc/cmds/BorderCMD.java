package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.ScoreboardM;
import org.bukkit.command.*;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import me.kernelfreeze.uhc.*;
import org.bukkit.plugin.*;

public class BorderCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getName().equalsIgnoreCase("border")) {
            return false;
        }
        if (!GameManager.getGameManager().getModerators().contains(commandSender) && !commandSender.hasPermission("uhc.mod")) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (array.length < 1) {
            commandSender.sendMessage("§c/border <size>");
            return true;
        }
        final int int1 = Integer.parseInt(array[0]);
        if (!this.checkNumber(array[0])) {
            commandSender.sendMessage("§cThe size must be a number!");
            return true;
        }
        if (int1 < 5) {
            commandSender.sendMessage("§cThe size cannot be smaller than 5!");
            return true;
        }
        commandSender.sendMessage("§aShrinking border to " + int1 + "x" + int1 + "...");
        this.startSeconds(int1);
        return true;
    }
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean checkNumber(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    private void startSeconds(final int n) {
        final GameManager gameManager = GameManager.getGameManager();
        gameManager.setCanBorderShrink(false);
        new BukkitRunnable() {
            int i = 11;
            
            public void run() {
                --this.i;
                if (this.i >= 1) {
                    Bukkit.broadcastMessage(gameManager.getBorderPrefix() + gameManager.getMainColor() + " Border shrinking in " + gameManager.getSecondaryColor() + this.i + gameManager.getMainColor() + " seconds!");
                }
                else if (this.i == 0) {
                    gameManager.shrinkBorder(n, this);
                    Bukkit.getServer().broadcastMessage(gameManager.getBorderPrefix() + gameManager.getMainColor() + " The world border has shrunk to " + gameManager.getSecondaryColor() + n + gameManager.getMainColor() + "x" + gameManager.getSecondaryColor() + n);
                    new ScoreboardM().updateBorder();
                }
            }
        }.runTaskTimer(UHC.getInstance(), 20L, 20L);
    }
}
