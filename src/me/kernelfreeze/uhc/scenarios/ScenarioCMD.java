package me.kernelfreeze.uhc.scenarios;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.*;

public class ScenarioCMD implements CommandExecutor
{
    private final Inventory scenarioInv;
    
    public ScenarioCMD() {
        this.scenarioInv = Bukkit.createInventory((InventoryHolder)null, 27, "§6Scenarios: (Click to Toggle!)");
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("scenario")) {
            final Player player = (Player)commandSender;
            if (array.length <= 0) {
                int n = 0;
                final Iterator<Scenario> iterator = ScenarioManager.getInstance().getScenarios().values().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().isEnabled()) {
                        ++n;
                    }
                }
                if (n == 0) {
                    player.sendMessage("§cAll of the scenarios are currently disabled!");
                    return true;
                }
                player.openInventory(ScenarioManager.getInstance().getScenariosInventory());
                return true;
            }
            else if (commandSender.hasPermission("uhc.scenario") || GameManager.getGameManager().getHostName().equalsIgnoreCase(player.getName())) {
                if (array[0].equalsIgnoreCase("listall")) {
                    player.openInventory(this.getAllScenariosInv());
                    return true;
                }
                if (array.length == 1 && !array[0].equalsIgnoreCase("listall")) {
                    commandSender.sendMessage("§c/scenario [scenario] [enable/disable]");
                    return true;
                }
                if (!ScenarioManager.getInstance().doesScenarioExists(array[0])) {
                    commandSender.sendMessage(ChatColor.RED + "This scenario does not exists!");
                    return true;
                }
                if (array[1].equalsIgnoreCase("enable") || array[1].equalsIgnoreCase("true")) {
                    ScenarioManager.getInstance().getScenarioIgnoreCase(array[0]).setEnabled(true);
                    return true;
                }
                if (array[1].equalsIgnoreCase("disable") || array[1].equalsIgnoreCase("false")) {
                    ScenarioManager.getInstance().getScenarioIgnoreCase(array[0]).setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }
    
    private Inventory getAllScenariosInv() {
        this.scenarioInv.clear();
        final Iterator<Scenario> iterator = ScenarioManager.getInstance().getScenarios().values().iterator();
        while (iterator.hasNext()) {
            this.scenarioInv.addItem(new ItemStack[] { iterator.next().getScenarioIST() });
        }
        return this.scenarioInv;
    }
}
