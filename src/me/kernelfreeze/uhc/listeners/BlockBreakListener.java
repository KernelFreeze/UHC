package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.event.block.*;
import me.kernelfreeze.uhc.scenarios.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import me.kernelfreeze.uhc.player.*;
import org.bukkit.event.*;

public class BlockBreakListener implements Listener
{
    private final GameManager gameManager;
    
    public BlockBreakListener() {
        this.gameManager = GameManager.getGameManager();
    }
    
    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent blockBreakEvent) {
        final Block block = blockBreakEvent.getBlock();
        final Player player = blockBreakEvent.getPlayer();
        if (player.getWorld().equals(this.gameManager.getSpawnLocation().getWorld()) && !player.hasPermission("uhc.spawnprotection.bypass")) {
            blockBreakEvent.setCancelled(true);
            player.sendMessage("§cYou cannot break blocks here!");
        }
        final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        if (uhcPlayer.isSpectating()) {
            blockBreakEvent.setCancelled(true);
        }
        if (!blockBreakEvent.isCancelled()) {
            final boolean statsEnabled = this.gameManager.isStatsEnabled();
            switch (block.getType()) {
                case DIAMOND_ORE: {
                    if (ScenarioManager.getInstance().getScenarioExact("BloodDiamonds").isEnabled()) {
                        player.damage(1.0);
                    }
                    if (ScenarioManager.getInstance().getScenarioExact("Diamondless").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(4);
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("RiskyRetrieval").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(4);
                        player.getEnderChest().addItem(new ItemStack[] { new ItemStack(Material.DIAMOND) });
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(2);
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND, 3));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(8);
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(4);
                    }
                    uhcPlayer.addDiamond();
                    for (final Player player2 : this.gameManager.getModerators()) {
                        if (player2 != null) {
                            if (uhcPlayer.getDiamondsMined() % 10 == 0) {
                                player2.sendMessage("§a" + player.getName() + this.gameManager.getMainColor() + " is mining diamonds! (total: " + uhcPlayer.getDiamondsMined() + ")");
                            }
                            else {
                                if (uhcPlayer.getDiamondsMined() != 1) {
                                    continue;
                                }
                                player2.sendMessage("§a" + player.getName() + this.gameManager.getMainColor() + " has mined his first diamond!");
                            }
                        }
                    }
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalDiamondsMined();
                        break;
                    }
                    break;
                }
                case GOLD_ORE: {
                    if (ScenarioManager.getInstance().getScenarioExact("Goldless").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(3);
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("RiskyRetrieval").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(4);
                        player.getEnderChest().addItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT) });
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(2);
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT, 3));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(6);
                    }
                    else if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(3);
                    }
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalGoldMined();
                        break;
                    }
                    break;
                }
                case IRON_ORE: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalIronMined();
                    }
                    if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(2);
                        break;
                    }
                    if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT, 3));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(5);
                        break;
                    }
                    if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
                        ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class)).setExperience(2);
                        break;
                    }
                    break;
                }
                case REDSTONE_ORE: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalRedstoneMined();
                        break;
                    }
                    break;
                }
                case GLOWING_REDSTONE_ORE: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalRedstoneMined();
                        break;
                    }
                    break;
                }
                case QUARTZ_ORE: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalQuartzMined();
                        break;
                    }
                    break;
                }
                case LAPIS_ORE: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalLapisMined();
                        break;
                    }
                    break;
                }
                case COAL_ORE: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalCoalMined();
                        break;
                    }
                    break;
                }
                case MOB_SPAWNER: {
                    if (statsEnabled && uhcPlayer.isPlayerAlive()) {
                        uhcPlayer.addTotalSpawnersMined();
                    }
                    uhcPlayer.addSpawnersMined();
                    for (final Player player3 : this.gameManager.getModerators()) {
                        if (player3 != null) {
                            player3.sendMessage("§a" + player.getName() + this.gameManager.getMainColor() + " is mining a mob spawner! (total: " + uhcPlayer.getSpawnersMined() + ")");
                        }
                    }
                    break;
                }
                case GRAVEL: {
                    if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT, 3));
                        break;
                    }
                    if (uhcPlayer.isPlayerAlive() && ScenarioManager.getInstance().getScenarioExact("Vanilla+").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT));
                        break;
                    }
                    if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                        blockBreakEvent.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT));
                        break;
                    }
                    break;
                }
                case LOG: {
                    if (uhcPlayer.isPlayerAlive() && ScenarioManager.getInstance().getScenarioExact("Timber").isEnabled()) {
                        Block block2 = block.getRelative(BlockFace.UP);
                        Block block3 = block.getRelative(BlockFace.DOWN);
                        while (block2.getType() == Material.LOG || block2.getType() == Material.LOG_2) {
                            block2.breakNaturally();
                            block2 = block2.getRelative(BlockFace.UP);
                        }
                        while (block3.getType() == Material.LOG || block3.getType() == Material.LOG_2) {
                            block3.breakNaturally();
                            block3 = block3.getRelative(BlockFace.DOWN);
                        }
                        break;
                    }
                    break;
                }
                case LOG_2: {
                    if (uhcPlayer.isPlayerAlive() && ScenarioManager.getInstance().getScenarioExact("Timber").isEnabled()) {
                        Block block4 = block.getRelative(BlockFace.UP);
                        Block block5 = block.getRelative(BlockFace.DOWN);
                        while (block4.getType() == Material.LOG || block4.getType() == Material.LOG_2) {
                            block4.breakNaturally();
                            block4 = block4.getRelative(BlockFace.UP);
                        }
                        while (block5.getType() == Material.LOG || block5.getType() == Material.LOG_2) {
                            block5.breakNaturally();
                            block5 = block5.getRelative(BlockFace.DOWN);
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
}
