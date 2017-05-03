package me.kernelfreeze.uhc.configs;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.command.*;
import org.bukkit.*;

public class ConfigCMD implements CommandExecutor
{
    private final GameManager gameManager;
    
    public ConfigCMD() {
        this.gameManager = GameManager.getGameManager();
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("config")) {
            final ChatColor mainColor = this.gameManager.getMainColor();
            final ChatColor secondaryColor = this.gameManager.getSecondaryColor();
            if (array.length <= 0) {
                commandSender.sendMessage(secondaryColor + "=--------------------------=");
                commandSender.sendMessage("§a§nGame Config:");
                commandSender.sendMessage(" ");
                commandSender.sendMessage(this.gameManager.getHostPrefix() + this.gameManager.getHostName());
                commandSender.sendMessage(mainColor + "Map Size: " + secondaryColor + this.gameManager.getDefaultMapSize());
                commandSender.sendMessage(mainColor + "Teams: " + secondaryColor + TeamManager.getInstance().isTeamsEnabled());
                if (TeamManager.getInstance().isTeamsEnabled()) {
                    commandSender.sendMessage(mainColor + "Team Size: " + secondaryColor + TeamManager.getInstance().getMaxSize());
                    commandSender.sendMessage(mainColor + "Can Damage Team Members: " + secondaryColor + TeamManager.getInstance().canDamageTeamMembers());
                }
                commandSender.sendMessage(mainColor + "Apples Rate: " + secondaryColor + this.gameManager.getAppleRates());
                for (final ConfigIntegers configIntegers : ConfigIntegers.values()) {
                    if (configIntegers != ConfigIntegers.BORDER_SHRINK_BY && configIntegers != ConfigIntegers.BORDER_SHRINK_EVERY && configIntegers != ConfigIntegers.BORDER_SHRINK_UNTIL) {
                        commandSender.sendMessage(mainColor + ConfigIntegers.configToString(configIntegers) + ": " + secondaryColor + configIntegers.get());
                    }
                }
                commandSender.sendMessage(mainColor + "PvP: " + secondaryColor + this.gameManager.isPvpEnabled());
                for (final ConfigBooleans configBooleans : ConfigBooleans.values()) {
                    commandSender.sendMessage(mainColor + ConfigBooleans.configToString(configBooleans) + ": " + secondaryColor + configBooleans.isEnabled());
                }
                commandSender.sendMessage(mainColor + "Stats: " + secondaryColor + this.gameManager.isStatsEnabled());
                commandSender.sendMessage(mainColor + "Current Border: " + secondaryColor + this.gameManager.getCurrentBorder());
                commandSender.sendMessage(secondaryColor + "=--------------------------=");
                return true;
            }
            if (commandSender.hasPermission("uhc.config") || this.gameManager.getHostName().equalsIgnoreCase(commandSender.getName())) {
                if (array.length == 1 && !array[0].equalsIgnoreCase("types")) {
                    commandSender.sendMessage("§c/config <configType> <True/False/Number>");
                    return true;
                }
                if (ConfigIntegers.fromString(array[0]) == null && ConfigBooleans.fromString(array[0]) == null && !array[0].equalsIgnoreCase("applerates") && !array[0].equalsIgnoreCase("applesrate")) {
                    commandSender.sendMessage("§cConfig type could not be found!");
                    return true;
                }
                final ConfigIntegers fromString = ConfigIntegers.fromString(array[0]);
                if (fromString != null) {
                    if (!this.isNumeric(array[1])) {
                        commandSender.sendMessage("§cThis config type must be a number!");
                        return true;
                    }
                    if (fromString.equals(ConfigIntegers.BORDERSHRINKTIME) && Integer.parseInt(array[1]) < 10) {
                        commandSender.sendMessage("§cBorder shrink time cannot be smaller than 10 minutes!");
                        return true;
                    }
                    if (fromString.equals(ConfigIntegers.BORDERSHRINKTIME) && GameManager.getGameManager().isGameRunning()) {
                        commandSender.sendMessage("§You can't change the border shrink time after the game has already started!");
                        return true;
                    }
                    fromString.set(Integer.parseInt(array[1]), commandSender);
                    return true;
                }
                else if (ConfigBooleans.fromString(array[0]) != null) {
                    if (!array[1].equalsIgnoreCase("true") && !array[1].equalsIgnoreCase("false")) {
                        commandSender.sendMessage("§cThis config type must be a boolean! (True/False)");
                        return true;
                    }
                    if (array[1].equalsIgnoreCase("true")) {
                        ConfigBooleans.fromString(array[0]).enable(commandSender);
                        return true;
                    }
                    if (array[1].equalsIgnoreCase("false")) {
                        ConfigBooleans.fromString(array[0]).disable(commandSender);
                        return true;
                    }
                    return true;
                }
                else if (array[0].equalsIgnoreCase("applerates") || array[0].equalsIgnoreCase("applesrate")) {
                    if (!this.isDouble(array[1])) {
                        commandSender.sendMessage("§cApples Rate must be a number!");
                        return true;
                    }
                    if (Double.parseDouble(array[1]) > 100.0) {
                        commandSender.sendMessage("§cApples rate cannot be bigger than 100%!");
                        return true;
                    }
                    GameManager.getGameManager().setAppleRates(Double.parseDouble(array[1]));
                    Bukkit.getServer().broadcastMessage(GameManager.getGameManager().getConfigPrefix() + GameManager.getGameManager().getMainColor() + commandSender.getName() + " has set the " + GameManager.getGameManager().getSecondaryColor() + "Apples Rate" + GameManager.getGameManager().getMainColor() + " to §a" + array[1]);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isNumeric(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
    
    private boolean isDouble(final String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
