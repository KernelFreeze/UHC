package me.kernelfreeze.uhc.configs;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.util.WUtil;
import org.bukkit.command.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import me.kernelfreeze.uhc.util.*;

public enum ConfigIntegers
{
    MAXPLAYERS(135),
    BORDERSHRINKTIME(35),
    HEALTIME(5),
    PVPTIME(10),
    STARTERFOOD(5),
    BORDER_SHRINK_BY(500), 
    BORDER_SHRINK_EVERY(5), 
    BORDER_SHRINK_UNTIL(25);
    //APPLESRATE(3);
    
    private int i;
    
    private ConfigIntegers(final int i) {
        this.i = i;
    }
    
    public void set(final int i, final CommandSender commandSender) {
        this.i = i;
        Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getConfigPrefix() + GameManager.getGameManager().getMainColor() + commandSender.getName() + " has set the " + GameManager.getGameManager().getSecondaryColor() + configToString(this) + GameManager.getGameManager().getMainColor() + " to §a" + i);
    }
    
    public int get() {
        return this.i;
    }
    
    public static ConfigIntegers fromString(final String s) {
        for (final ConfigIntegers configIntegers : values()) {
            if (configIntegers.toString().equalsIgnoreCase(s.replace(" ", "_"))) {
                return configIntegers;
            }
            if (s.equalsIgnoreCase(configIntegers.toString().replace("_", ""))) {
                return configIntegers;
            }
        }
        return null;
    }
    
    public static String configToString(final ConfigIntegers configIntegers) {
        return WUtil.capitalizeFully(configIntegers.toString().replace("_", " ").toLowerCase());
    }
}
