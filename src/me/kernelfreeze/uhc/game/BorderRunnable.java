package me.kernelfreeze.uhc.game;

import me.kernelfreeze.uhc.configs.ConfigIntegers;
import org.bukkit.scheduler.*;
import me.kernelfreeze.uhc.configs.*;
import org.bukkit.*;

public class BorderRunnable extends BukkitRunnable
{
    private int i;
    private final GameManager gameManager;
    
    public BorderRunnable() {
        this.i = ConfigIntegers.BORDER_SHRINK_EVERY.get() * 60;
        this.gameManager = GameManager.getGameManager();
    }
    
    public void run() {
        if (this.gameManager.getCurrentBorder() <= ConfigIntegers.BORDER_SHRINK_UNTIL.get()) {
            this.cancel();
            return;
        }
        if (!this.gameManager.canBorderShrink()) {
            this.cancel();
        }
        this.i -= 10;
        final ChatColor mainColor = GameManager.getGameManager().getMainColor();
        final ChatColor secondaryColor = GameManager.getGameManager().getSecondaryColor();
        if (this.i == 10) {
            Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getBorderPrefix() + mainColor + " Border shrinking in " + secondaryColor + "10" + mainColor + " seconds!");
            this.gameManager.startSeconds();
            this.cancel();
        }
        else if (this.i > 10 && (this.i == 600 || this.i == 540 || this.i == 480 || this.i == 420 || this.i == 360 || this.i == 300 || this.i == 240 || this.i == 180 || this.i == 120 || this.i == 60)) {
            Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getBorderPrefix() + mainColor + " Border shrinking in " + secondaryColor + this.i / 60 + mainColor + " minutes!");
        }
    }
}
