package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.configs.ConfigBooleans;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.ScoreboardM;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.scheduler.*;

public class PlayerDeathListener implements Listener
{
    private final ScoreboardM sb;
    private final GameManager gameManager;
    private boolean clearDrops;
    
    public PlayerDeathListener() {
        this.sb = new ScoreboardM();
        this.gameManager = GameManager.getGameManager();
        this.clearDrops = false;
    }
    
    @EventHandler
    public void onPlayerDeathEvent(final PlayerDeathEvent playerDeathEvent) {
        if (this.gameManager.isGameRunning() && playerDeathEvent.getEntity().getWorld() != this.gameManager.getSpawnLocation().getWorld()) {
            final Player entity = playerDeathEvent.getEntity();
            final Player killer = playerDeathEvent.getEntity().getKiller();
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(entity.getUniqueId());
            uhcPlayer.setRespawnLocation(playerDeathEvent.getEntity().getLocation());
            uhcPlayer.setLastArmour(playerDeathEvent.getEntity().getInventory().getArmorContents());
            uhcPlayer.setLastInventory(playerDeathEvent.getEntity().getInventory().getContents());
            uhcPlayer.setPlayerAlive(false);
            uhcPlayer.setDied(true);
            if (this.gameManager.isStatsEnabled()) {
                uhcPlayer.addTotalDeath();
            }
            this.deathScenarios(entity, uhcPlayer, playerDeathEvent.getEntity().getLocation());
            if (this.clearDrops) {
                playerDeathEvent.getDrops().clear();
            }
            else {
                if (ScenarioManager.getInstance().getScenarioExact("GoldenRetriever").isEnabled()) {
                    playerDeathEvent.getDrops().add(this.getGoldenHead());
                }
                if (ScenarioManager.getInstance().getScenarioExact("Diamondless").isEnabled()) {
                    playerDeathEvent.getDrops().add(new ItemStack(Material.DIAMOND, 1));
                }
                if (ScenarioManager.getInstance().getScenarioExact("Goldless").isEnabled()) {
                    playerDeathEvent.getDrops().add(new ItemStack(Material.GOLD_INGOT, 8));
                    playerDeathEvent.getDrops().add(this.getGoldenHead());
                }
                if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                    playerDeathEvent.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 2));
                    playerDeathEvent.getDrops().add(new ItemStack(Material.DIAMOND));
                    playerDeathEvent.getDrops().add(new ItemStack(Material.ARROW, 32));
                    playerDeathEvent.getDrops().add(new ItemStack(Material.STRING, 2));
                }
                this.spawnHead(entity);
            }
            if (playerDeathEvent.getEntity().getKiller() != null && playerDeathEvent.getEntity().getKiller() instanceof Player) {
                final UHCPlayer uhcPlayer2 = PlayerManager.getPlayerManager().getUHCPlayer(killer.getUniqueId());
                uhcPlayer2.addKill();
                this.sb.updateKills(killer);
                if (TeamManager.getInstance().isTeamsEnabled()) {
                    final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)killer);
                    team.addKill();
                    this.sb.updateTeamKills(team);
                }
                if (this.gameManager.isStatsEnabled()) {
                    uhcPlayer2.addTotalKill();
                    if (uhcPlayer2.getKills() > uhcPlayer2.getHighestKillStreak()) {
                        uhcPlayer2.setHighestKillStreak(uhcPlayer2.getKills());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerRespawnEvent(final PlayerRespawnEvent playerRespawnEvent) {
        final Player player = playerRespawnEvent.getPlayer();
        if (this.gameManager.isGameRunning()) {
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
            if (player.hasPermission("uhc.spectate")) {
                playerRespawnEvent.setRespawnLocation(this.gameManager.getUHCWorld().getSpawnLocation());
                PlayerManager.getPlayerManager().setSpectating(true, player);
                if (!uhcPlayer.isSpectating()) {
                    PlayerManager.getPlayerManager().setSpectating(true, player);
                    player.setGameMode(GameMode.CREATIVE);
                }
            }
            else {
                playerRespawnEvent.setRespawnLocation(this.gameManager.getSpawnLocation());
                if (this.gameManager.kickPlayerOnDeath()) {
                    Bukkit.getScheduler().runTaskLater((Plugin) UHC.getInstance(), (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            player.kickPlayer(PlayerDeathListener.this.gameManager.getKickDeathMessage());
                            player.setWhitelisted(false);
                        }
                    }, (long)(20 * this.gameManager.getDeathKickTime()));
                }
            }
        }
        else {
            playerRespawnEvent.setRespawnLocation(this.gameManager.getSpawnLocation());
        }
    }
    
    @EventHandler
    public void onEntityDeathEvent(final EntityDeathEvent entityDeathEvent) {
        final EntityType type = entityDeathEvent.getEntity().getType();
        UHCPlayer uhcPlayer = null;
        final boolean b = entityDeathEvent.getEntity().getKiller() != null;
        final boolean statsEnabled = this.gameManager.isStatsEnabled();
        if (entityDeathEvent.getEntity().getKiller() != null) {
            uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(entityDeathEvent.getEntity().getKiller().getUniqueId());
        }
        switch (type) {
            case GHAST: {
                if (!ConfigBooleans.REGENERATIONPOTIONS.isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.GOLD_INGOT));
                }
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addGhastsKilled();
                    break;
                }
                break;
            }
            case ZOMBIE: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addZombiesKilled();
                    break;
                }
                break;
            }
            case CREEPER: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addCreepersKilled();
                    break;
                }
                break;
            }
            case SKELETON: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addSkeletonsKilled();
                    break;
                }
                break;
            }
            case CAVE_SPIDER: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addCaveSpiderKilled();
                    break;
                }
                break;
            }
            case SPIDER: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addSpidersKilled();
                }
                if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                    entityDeathEvent.getDrops().add(new ItemStack(Material.STRING, 3));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                    entityDeathEvent.getDrops().add(new ItemStack(Material.STRING));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                    entityDeathEvent.getDrops().add(new ItemStack(Material.STRING));
                    break;
                }
                break;
            }
            case BLAZE: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addBlazesKilled();
                    break;
                }
                break;
            }
            case COW: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addCowsKilled();
                }
                if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.COOKED_BEEF, 3));
                    entityDeathEvent.getDrops().add(new ItemStack(Material.LEATHER, 3));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.COOKED_BEEF, 2));
                    entityDeathEvent.getDrops().add(new ItemStack(Material.LEATHER));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.COOKED_BEEF, 2));
                    entityDeathEvent.getDrops().add(new ItemStack(Material.LEATHER));
                    break;
                }
                break;
            }
            case PIG: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addPigsKilled();
                }
                if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.GRILLED_PORK, 2));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.GRILLED_PORK, 2));
                    break;
                }
                break;
            }
            case CHICKEN: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addChickensKilled();
                }
                if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 3));
                    entityDeathEvent.getDrops().add(new ItemStack(Material.FEATHER, 3));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 2));
                    entityDeathEvent.getDrops().add(new ItemStack(Material.FEATHER, 2));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.COOKED_BEEF, 2));
                    entityDeathEvent.getDrops().add(new ItemStack(Material.FEATHER));
                    break;
                }
                break;
            }
            case HORSE: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addHorsesKilled();
                }
                if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.LEATHER, 3));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.LEATHER));
                    break;
                }
                if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                    entityDeathEvent.getDrops().clear();
                    entityDeathEvent.getDrops().add(new ItemStack(Material.LEATHER));
                    break;
                }
                break;
            }
            case WITCH: {
                if (statsEnabled && b && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                    uhcPlayer.addWitchesKilled();
                    break;
                }
                break;
            }
        }
    }
    
    private void spawnHead(final Player player) {
        player.getLocation().getBlock().setType(Material.NETHER_FENCE);
        player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0)).setType(Material.SKULL);
        final Skull skull = (Skull)player.getLocation().add(0.0, 1.0, 0.0).getBlock().getState();
        skull.setOwner(player.getName());
        skull.update();
        player.getLocation().add(0.0, 1.0, 0.0).getBlock().setData((byte)1);
    }
    
    private ItemStack getGoldenHead() {
        final ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§6Golden Head");
        final ArrayList<String> lore = new ArrayList<String>();
        lore.add("§5Some say consuming the head of a");
        lore.add("§5fallen foe strengthens the blood");
        lore.add(GameManager.getGameManager().getPrefix());
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    private void deathScenarios(final Player player, final UHCPlayer uhcPlayer, final Location location) {
        if (ScenarioManager.getInstance().getScenarioExact("TimeBomb").isEnabled()) {
            this.clearDrops = true;
            location.getBlock().setType(Material.CHEST);
            final Chest chest = (Chest)location.getBlock().getState();
            location.add(1.0, 0.0, 0.0).getBlock().setType(Material.CHEST);
            location.add(0.0, 1.0, 0.0).getBlock().setType(Material.AIR);
            location.add(1.0, 1.0, 0.0).getBlock().setType(Material.AIR);
            chest.getInventory().addItem(new ItemStack[] { this.getGoldenHead() });
            chest.getInventory().addItem(uhcPlayer.lastArmour());
            for (final ItemStack itemStack : uhcPlayer.lastInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType() != Material.AIR) {
                        chest.getInventory().addItem(new ItemStack[] { itemStack });
                    }
                }
            }
            if (ScenarioManager.getInstance().getScenarioExact("GoldenRetriever").isEnabled()) {
                chest.getInventory().addItem(new ItemStack[] { this.getGoldenHead() });
            }
            if (ScenarioManager.getInstance().getScenarioExact("Diamondless").isEnabled()) {
                chest.getInventory().addItem(new ItemStack[] { new ItemStack(Material.DIAMOND, 1) });
            }
            if (ScenarioManager.getInstance().getScenarioExact("Goldless").isEnabled()) {
                chest.getInventory().addItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 8) });
                chest.getInventory().addItem(new ItemStack[] { this.getGoldenHead() });
            }
            if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                chest.getInventory().addItem(new ItemStack[] { new ItemStack(Material.GOLDEN_APPLE, 2) });
                chest.getInventory().addItem(new ItemStack[] { new ItemStack(Material.DIAMOND) });
                chest.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ARROW, 32) });
                chest.getInventory().addItem(new ItemStack[] { new ItemStack(Material.STRING, 2) });
            }
            new BukkitRunnable() {
                final /* synthetic */ String val$name = player.getName();
                
                public void run() {
                    location.getBlock().setType(Material.AIR);
                    Bukkit.broadcastMessage("§7[§5TimeBomb§7] §a" + this.val$name + "'s §fCorpse has exploded!");
                    location.getWorld().createExplosion(location.getBlockX() + 0.5, (double)(location.getBlockY() + 1), location.getBlockZ() + 0.5, 10.0f, false, true);
                    location.getWorld().strikeLightning(location);
                }
            }.runTaskLater((Plugin) UHC.getInstance(), 600L);
        }
        else {
            this.clearDrops = false;
        }
        if (ScenarioManager.getInstance().getScenarioExact("ExtraInventory").isEnabled()) {
            location.add(1.0, 1.0, 0.0).getBlock().setType(Material.CHEST);
            ((Chest)location.add(1.0, 1.0, 0.0).getBlock().getState()).getInventory().setContents(player.getEnderChest().getContents());
        }
    }
}
