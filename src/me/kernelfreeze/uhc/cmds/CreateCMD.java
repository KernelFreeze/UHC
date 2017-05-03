package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.listeners.GameStopEvent;
import me.kernelfreeze.uhc.world.WorldCreator;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.*;
import me.kernelfreeze.uhc.listeners.*;
import org.bukkit.event.*;
import me.kernelfreeze.uhc.*;
import me.kernelfreeze.uhc.world.*;

public class CreateCMD implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getName().equalsIgnoreCase("createuhc")) {
            return false;
        }
        if (!commandSender.hasPermission("uhc.createuhc") && !GameManager.getGameManager().getHostName().equalsIgnoreCase(commandSender.getName())) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (GameManager.getGameManager().isGameRunning()) {
            Bukkit.getServer().getPluginManager().callEvent((Event)new GameStopEvent("World re-creation (command)"));
        }
        if (UHC.getInstance().getConfig().getBoolean("settings.using-world-border-plugin")) {
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), "worldborder:wb fill cancel");
        }
        new WorldCreator(true, true);
        commandSender.sendMessage(GameManager.getGameManager().getMainColor() + "Successfully created a new game!");
        return true;
    }
}
