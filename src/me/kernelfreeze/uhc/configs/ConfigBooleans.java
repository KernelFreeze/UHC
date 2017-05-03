package me.kernelfreeze.uhc.configs;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.util.WUtil;
import org.bukkit.command.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import me.kernelfreeze.uhc.util.*;

public enum ConfigBooleans
{
    NETHER(true), 
    STRENGTH1(false),
    STRENGTH2(false),
    INVISIBILITYPOTIONS(false),
    REGENERATIONPOTIONS(false),
    ENDERPEARLDAMAGE(false),
    ABSORPTION(true),
    GODAPPLES(false),
    HORSES(true),
    HORSEHEALING(true),
    HORSEARMOUR(true),
    HEADPOST(true),
    GOLDENHEADS(true),
    NATURALREGENERATION(false);
    
    private boolean bool;
    
    private ConfigBooleans(final boolean bool) {
        this.bool = bool;
    }
    
    public void enable(final CommandSender commandSender) {
        this.bool = true;
        System.out.println();
        Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getConfigPrefix() + GameManager.getGameManager().getMainColor() + commandSender.getName() + " has set " + GameManager.getGameManager().getSecondaryColor() + configToString(this) + GameManager.getGameManager().getMainColor() + " to §aTrue");
    }
    
    public void disable(final CommandSender commandSender) {
        this.bool = false;
        Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getConfigPrefix() + GameManager.getGameManager().getMainColor() + commandSender.getName() + " has set " + GameManager.getGameManager().getSecondaryColor() + configToString(this) + GameManager.getGameManager().getMainColor() + " to §cFalse");
    }
    
    public boolean isEnabled() {
        return this.bool;
    }
    
    public static ConfigBooleans fromString(final String s) {
        for (final ConfigBooleans configBooleans : values()) {
            if (configBooleans.toString().equalsIgnoreCase(s.replace(" ", "_"))) {
                return configBooleans;
            }
            if (s.equalsIgnoreCase(configBooleans.toString().replace("_", ""))) {
                return configBooleans;
            }
        }
        return null;
    }
    
    public static String configToString(final ConfigBooleans configBooleans) {
        return WUtil.capitalizeFully(configBooleans.toString().replace("_", " ").toLowerCase());
    }
}
