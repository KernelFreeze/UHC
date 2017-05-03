package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.configs.ConfigBooleans;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.*;

public class InventoryListeners implements Listener
{
    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent inventoryClickEvent) {
        final Inventory inventory = inventoryClickEvent.getInventory();
        final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventory != null && currentItem != null) {
            if (inventory.getTitle().equalsIgnoreCase("§aCurrent Scenarios:") || inventory.getTitle().equalsIgnoreCase("§aStats:") || inventory.getTitle().equalsIgnoreCase("§6Scenarios: (Click to Toggle!)") || inventory.getTitle().equalsIgnoreCase("§aPlayers Alive:") || inventory.getTitle().equalsIgnoreCase("§aEquipped")) {
                inventoryClickEvent.setCancelled(true);
                if (inventory.getTitle().equalsIgnoreCase("§aPlayers Alive:") && currentItem.getType().equals((Object)Material.SKULL_ITEM)) {
                    final Player player2 = Bukkit.getServer().getPlayer(currentItem.getItemMeta().getDisplayName().replace("§a", ""));
                    if (player2 != null) {
                        player.teleport((Entity)player2);
                    }
                }
                if (inventory.getTitle().equalsIgnoreCase("§6Scenarios: (Click to Toggle!)") && currentItem.getType() != Material.AIR) {
                    final String replace = currentItem.getItemMeta().getDisplayName().replace(GameManager.getGameManager().getMainColor().toString(), "");
                    final ArrayList<String> list = new ArrayList<String>();
                    if (currentItem.getItemMeta().getLore().contains("§aEnabled")) {
                        ScenarioManager.getInstance().getScenarioExact(replace).setEnabled(false);
                        list.add("§cDisabled");
                        final ItemMeta itemMeta = currentItem.getItemMeta();
                        itemMeta.setLore((List)list);
                        currentItem.setItemMeta(itemMeta);
                    }
                    else {
                        ScenarioManager.getInstance().getScenarioExact(replace).setEnabled(true);
                        list.add("§aEnabled");
                        final ItemMeta itemMeta2 = currentItem.getItemMeta();
                        itemMeta2.setLore((List)list);
                        currentItem.setItemMeta(itemMeta2);
                    }
                }
            }
            if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
                inventoryClickEvent.setCancelled(true);
            }
            if (!ConfigBooleans.HORSEARMOUR.isEnabled() && currentItem.getType() != Material.AIR && (currentItem.getType().equals((Object)Material.IRON_BARDING) || currentItem.getType().equals((Object)Material.GOLD_BARDING) || currentItem.getType().equals((Object)Material.DIAMOND_BARDING))) {
                inventoryClickEvent.setCancelled(true);
                player.sendMessage("§cHorse armour is currently disabled!");
                currentItem.setType(Material.AIR);
            }
        }
    }
}
