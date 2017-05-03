package me.kernelfreeze.uhc.scenarios;

import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.inventory.*;
import me.kernelfreeze.uhc.game.*;
import java.util.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;

public class Scenario
{
    private String name;
    private String[] description;
    private boolean enabled;
    private Material scenarioItem;
    private Material scenarioItem2;
    
    Scenario(final String name, final Material material, final String... description) {
        this.description = new String[10];
        this.enabled = false;
        this.name = name;
        this.description = description;
        this.scenarioItem = material;
        this.scenarioItem2 = material;
    }
    
    ItemStack getScenarioItemStack() {
        final ItemStack itemStack = new ItemStack(this.scenarioItem);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(GameManager.getGameManager().getMainColor() + this.name);
        final ArrayList<String> lore = new ArrayList<String>();
        final ChatColor secondaryColor = GameManager.getGameManager().getSecondaryColor();
        final String[] description = this.description;
        for (int length = description.length, i = 0; i < length; ++i) {
            lore.add(secondaryColor + description[i]);
        }
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    ItemStack getScenarioIST() {
        final ItemStack itemStack = new ItemStack(this.scenarioItem2);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(GameManager.getGameManager().getMainColor() + this.name);
        final ArrayList<String> lore = new ArrayList<String>();
        if (this.enabled) {
            lore.add("§aEnabled");
        }
        else {
            lore.add("§cDisabled");
        }
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        lore.clear();
        return itemStack;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            ScenarioManager.getInstance().getActiveScenarios().add(this.getName());
            Bukkit.broadcastMessage(GameManager.getGameManager().getPrefix() + GameManager.getGameManager().getSecondaryColor() + this.getName() + GameManager.getGameManager().getMainColor() + " scenario has been §aenabled!");
            if (this.name.equalsIgnoreCase("CutClean") || this.name.equalsIgnoreCase("TripleOres") || this.name.equalsIgnoreCase("Vanilla+")) {
                GameManager.getGameManager().setAppleRates(1.0);
            }
        }
        else {
            ScenarioManager.getInstance().getActiveScenarios().remove(this.getName());
            Bukkit.broadcastMessage(GameManager.getGameManager().getPrefix() + GameManager.getGameManager().getSecondaryColor() + this.getName() + GameManager.getGameManager().getMainColor() + " scenario has been §cdisabled!");
        }
        this.enabled = enabled;
        this.getScenarioIST();
    }
    
    public String getName() {
        return this.name;
    }
}
