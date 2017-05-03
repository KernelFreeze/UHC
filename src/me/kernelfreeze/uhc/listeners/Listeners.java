package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.configs.ConfigBooleans;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;

import java.text.*;
import org.bukkit.entity.*;
import org.spigotmc.event.entity.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.block.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import java.util.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;
import org.bukkit.event.entity.*;

public class Listeners implements Listener
{
    private final GameManager gameManager;
    private final Inventory alive;
    
    public Listeners() {
        this.gameManager = GameManager.getGameManager();
        this.alive = Bukkit.createInventory((InventoryHolder)null, 54, "§aPlayers Alive:");
    }
    
    @EventHandler
    public void onPlayerPortalEvent(final PlayerPortalEvent playerPortalEvent) {
        if (!ConfigBooleans.NETHER.isEnabled()) {
            playerPortalEvent.setCancelled(true);
            playerPortalEvent.getPlayer().sendMessage("§cThe nether is currently disabled!");
        }
        if (this.gameManager.getCurrentBorder() <= 500) {
            playerPortalEvent.setCancelled(true);
            playerPortalEvent.getPlayer().sendMessage("§cYou can only go to the nether before the 500 border shrink!");
        }
        final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(playerPortalEvent.getPlayer().getUniqueId());
        if (this.gameManager.isStatsEnabled() && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
            uhcPlayer.addNetherE();
        }
        if (!playerPortalEvent.isCancelled() && playerPortalEvent.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (playerPortalEvent.getFrom().getWorld().getName().equalsIgnoreCase(this.gameManager.getUhcWorldName())) {
                final Player player = playerPortalEvent.getPlayer();
                playerPortalEvent.setTo(playerPortalEvent.getPortalTravelAgent().findOrCreate(new Location(Bukkit.getServer().getWorld(this.gameManager.getUhcWorldName() + "_nether"), player.getLocation().getX() / 8.0, player.getLocation().getY(), player.getLocation().getZ() / 8.0)));
            }
            else if (playerPortalEvent.getFrom().getWorld().getName().equalsIgnoreCase(this.gameManager.getUhcWorldName() + "_nether")) {
                final Player player2 = playerPortalEvent.getPlayer();
                playerPortalEvent.setTo(playerPortalEvent.getPortalTravelAgent().findOrCreate(new Location(Bukkit.getServer().getWorld(this.gameManager.getUhcWorldName()), player2.getLocation().getX() * 8.0, player2.getLocation().getY(), player2.getLocation().getZ() * 8.0)));
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageEvent(final EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getEntity().getWorld().equals(this.gameManager.getSpawnLocation().getWorld()) || this.gameManager.isScattering()) {
            entityDamageEvent.setCancelled(true);
        }
        if (entityDamageEvent.getEntity() instanceof Player) {
            if (ScenarioManager.getInstance().getScenarioExact("Fireless").isEnabled() && (entityDamageEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.LAVA) || entityDamageEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.FIRE) || entityDamageEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.FIRE_TICK))) {
                entityDamageEvent.setCancelled(true);
            }
            if (PlayerManager.getPlayerManager().getUHCPlayer(entityDamageEvent.getEntity().getUniqueId()).isSpectating()) {
                entityDamageEvent.setCancelled(true);
            }
            if (entityDamageEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.FALL) && ScenarioManager.getInstance().getScenarioExact("NoFallDamage").isEnabled()) {
                entityDamageEvent.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent playerInteractEntityEvent) {
        if (PlayerManager.getPlayerManager().getUHCPlayer(playerInteractEntityEvent.getPlayer().getUniqueId()).isSpectating()) {
            playerInteractEntityEvent.setCancelled(true);
            if (playerInteractEntityEvent.getRightClicked() instanceof Player && this.gameManager.openInvOnRightClick()) {
                playerInteractEntityEvent.getPlayer().openInventory((Inventory)((Player)playerInteractEntityEvent.getRightClicked()).getInventory());
            }
        }
    }
    
    @EventHandler
    public void onPlayerLevelChangeEvent(final PlayerLevelChangeEvent playerLevelChangeEvent) {
        final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(playerLevelChangeEvent.getPlayer().getUniqueId());
        if (playerLevelChangeEvent.getNewLevel() > playerLevelChangeEvent.getOldLevel() && this.gameManager.isStatsEnabled() && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
            uhcPlayer.addXPLevel();
        }
    }
    
    @EventHandler
    public void onPlayerDropItemEvent(final PlayerDropItemEvent playerDropItemEvent) {
        final Player player = playerDropItemEvent.getPlayer();
        if (player.getWorld().equals(this.gameManager.getSpawnLocation().getWorld()) && !player.hasPermission("uhc.spawnprotection.bypass")) {
            playerDropItemEvent.setCancelled(true);
        }
        if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
            playerDropItemEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityShootBowEvent(final EntityShootBowEvent entityShootBowEvent) {
        if (entityShootBowEvent.getEntity() instanceof Player) {
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(((Player)entityShootBowEvent.getEntity()).getUniqueId());
            if (this.gameManager.isStatsEnabled() && uhcPlayer != null && !entityShootBowEvent.isCancelled() && uhcPlayer.isPlayerAlive()) {
                uhcPlayer.addArrowShot();
            }
        }
    }
    
    @EventHandler
    public void onPlayerPickupItemEvent(final PlayerPickupItemEvent playerPickupItemEvent) {
        final Player player = playerPickupItemEvent.getPlayer();
        if (player.getWorld().equals(this.gameManager.getSpawnLocation().getWorld()) && !player.hasPermission("uhc.spawnprotection.bypass")) {
            playerPickupItemEvent.setCancelled(true);
        }
        if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
            playerPickupItemEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityRegainHealthEvent(final EntityRegainHealthEvent entityRegainHealthEvent) {
        if (!ConfigBooleans.NATURALREGENERATION.isEnabled() && entityRegainHealthEvent.getEntity().getType() == EntityType.PLAYER && entityRegainHealthEvent.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            entityRegainHealthEvent.setCancelled(true);
        }
        if (!ConfigBooleans.HORSEHEALING.isEnabled() && entityRegainHealthEvent.getEntity().getType() == EntityType.HORSE) {
            entityRegainHealthEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!ConfigBooleans.ENDERPEARLDAMAGE.isEnabled() && entityDamageByEntityEvent.getDamager().getType().equals((Object)EntityType.ENDER_PEARL)) {
            entityDamageByEntityEvent.setCancelled(true);
        }
        if (entityDamageByEntityEvent.getDamager() instanceof Player && PlayerManager.getPlayerManager().getUHCPlayer(entityDamageByEntityEvent.getDamager().getUniqueId()).isSpectating()) {
            entityDamageByEntityEvent.setCancelled(true);
        }
        if (!TeamManager.getInstance().canDamageTeamMembers() && entityDamageByEntityEvent.getDamager() instanceof Player && entityDamageByEntityEvent.getEntity() instanceof Player && TeamManager.getInstance().isTeamsEnabled() && TeamManager.getInstance().getTeam((OfflinePlayer)entityDamageByEntityEvent.getDamager()).getPlayers().contains(entityDamageByEntityEvent.getEntity().getUniqueId())) {
            entityDamageByEntityEvent.setCancelled(true);
        }
        if (!entityDamageByEntityEvent.isCancelled() && entityDamageByEntityEvent.getDamager() instanceof Arrow && entityDamageByEntityEvent.getEntity() instanceof Player && ((Arrow)entityDamageByEntityEvent.getDamager()).getShooter() instanceof Player) {
            final Player player = (Player)entityDamageByEntityEvent.getEntity();
            final Player player2 = (Player)((Arrow)entityDamageByEntityEvent.getDamager()).getShooter();
            player2.sendMessage(this.gameManager.getMainColor() + player.getName() + "'s Health: §a" + new DecimalFormat("#.#").format(player.getHealth() / 2.0) + "§4 \u2764");
            if (ScenarioManager.getInstance().getScenarioExact("Switcheroo").isEnabled()) {
                final Location location = player2.getLocation();
                player2.teleport(player.getLocation());
                player.teleport(location);
                player2.sendMessage("§aSwitched locations!");
                player.sendMessage("§aSwitched locations!");
            }
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player2.getUniqueId());
            if (this.gameManager.isStatsEnabled() && uhcPlayer != null && !entityDamageByEntityEvent.isCancelled() && uhcPlayer.isPlayerAlive()) {
                uhcPlayer.addArrowHit();
            }
        }
    }
    
    @EventHandler
    public void onEntityTameEvent(final EntityTameEvent entityTameEvent) {
        if (entityTameEvent.getEntity() instanceof Horse) {
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(((Player)entityTameEvent.getOwner()).getUniqueId());
            if (this.gameManager.isStatsEnabled() && uhcPlayer != null && !entityTameEvent.isCancelled() && uhcPlayer.isPlayerAlive()) {
                uhcPlayer.addHorsesTamed();
            }
        }
    }
    
    @EventHandler
    public void onEntityMountEvent(final EntityMountEvent entityMountEvent) {
        if (!ConfigBooleans.HORSES.isEnabled() && entityMountEvent.getMount().getType() == EntityType.HORSE) {
            entityMountEvent.setCancelled(true);
        }
        if (entityMountEvent.getEntity().getPassenger() instanceof Player) {
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(entityMountEvent.getEntity().getPassenger().getUniqueId());
            if (uhcPlayer.isSpectating() || !uhcPlayer.isPlayerAlive()) {
                entityMountEvent.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onVehicleEnterEvent(final VehicleEnterEvent vehicleEnterEvent) {
        if (vehicleEnterEvent.getEntered() instanceof Player) {
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(vehicleEnterEvent.getEntered().getUniqueId());
            if (uhcPlayer.isSpectating() || !uhcPlayer.isPlayerAlive()) {
                vehicleEnterEvent.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onGameStopEvent(final GameStopEvent gameStopEvent) {
        this.gameManager.setRestarted(false);
        this.gameManager.setWorldWasUsed(true);
    }
    
    @EventHandler
    public void onCraftItemEvent(final CraftItemEvent craftItemEvent) {
        if (craftItemEvent.getWhoClicked() instanceof Player) {
            final Player player = (Player)craftItemEvent.getWhoClicked();
            if (!ConfigBooleans.GODAPPLES.isEnabled() && craftItemEvent.getCurrentItem().getType().equals((Object)Material.GOLDEN_APPLE) && craftItemEvent.getCurrentItem().getDurability() == 1) {
                craftItemEvent.setCancelled(true);
                player.sendMessage("§cGod Apples are currently disabled!");
            }
            if (!ConfigBooleans.GOLDENHEADS.isEnabled() && craftItemEvent.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
                craftItemEvent.setCancelled(true);
                player.sendMessage("§cGolden Heads are currently disabled!");
            }
            if (!ConfigBooleans.STRENGTH1.isEnabled() && craftItemEvent.getCurrentItem().getType().equals((Object)Material.BLAZE_POWDER)) {
                craftItemEvent.setCancelled(true);
                player.sendMessage("§cStrength 1 potions are currently disabled!");
            }
            if (ScenarioManager.getInstance().getScenarioExact("Bowless").isEnabled() && craftItemEvent.getCurrentItem().getType().equals((Object)Material.BOW)) {
                craftItemEvent.setCancelled(true);
                player.sendMessage("§cBows are currently disabled!");
            }
            if (ScenarioManager.getInstance().getScenarioExact("Rodless").isEnabled() && craftItemEvent.getCurrentItem().getType().equals((Object)Material.FISHING_ROD)) {
                craftItemEvent.setCancelled(true);
                player.sendMessage("§cFishing rods are currently disabled!");
            }
            if (ScenarioManager.getInstance().getScenarioExact("GoneFishing").isEnabled() && craftItemEvent.getCurrentItem().getType().equals((Object)Material.ENCHANTMENT_TABLE)) {
                craftItemEvent.setCancelled(true);
                player.sendMessage("§cEnchantment tables are currently disabled!");
            }
            if (ScenarioManager.getInstance().getScenarioExact("BareBones").isEnabled()) {
                if (craftItemEvent.getCurrentItem().getType().equals((Object)Material.ENCHANTMENT_TABLE)) {
                    craftItemEvent.setCancelled(true);
                    player.sendMessage("§cEnchantment tables are currently disabled!");
                }
                if (craftItemEvent.getCurrentItem().getType().equals((Object)Material.ANVIL)) {
                    craftItemEvent.setCancelled(true);
                    player.sendMessage("§cAnvils are currently disabled!");
                }
                if (craftItemEvent.getCurrentItem().getType().equals((Object)Material.GOLDEN_APPLE)) {
                    craftItemEvent.setCancelled(true);
                    player.sendMessage("§cGolden Apples are currently disabled!");
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent playerInteractEvent) {
        final Player player = playerInteractEvent.getPlayer();
        final ItemStack itemInHand = player.getItemInHand();
        if (PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId()).isSpectating()) {
            playerInteractEvent.setCancelled(true);
            if (itemInHand != null) {
                if (itemInHand.getType().equals((Object)Material.DIAMOND)) {
                    player.openInventory(this.getAlive());
                }
                else if (itemInHand.getType().equals((Object)Material.SKULL_ITEM)) {
                    this.randomPlayer(player);
                }
            }
        }
        if (itemInHand != null) {
            if (ScenarioManager.getInstance().getScenarioExact("Bowless").isEnabled() && itemInHand.getType() == Material.BOW) {
                playerInteractEvent.setCancelled(true);
                player.sendMessage("§cBows are currently disabled!");
            }
            if (ScenarioManager.getInstance().getScenarioExact("Soup").isEnabled() && playerInteractEvent.getItem().getType() == Material.MUSHROOM_SOUP && (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                playerInteractEvent.setCancelled(true);
                player.getItemInHand().setType(Material.BOWL);
                if (player.getHealth() > 16.0 && player.getHealth() <= 20.0) {
                    player.setHealth(20);
                }
                else {
                    player.setHealth(player.getHealth() + 4.0);
                }
            }
            if (itemInHand.getType() == Material.POTION) {
                if (!ConfigBooleans.INVISIBILITYPOTIONS.isEnabled()) {
                    switch (itemInHand.getDurability()) {
                        case 8238:
                        case 8270:
                        case 16430:
                        case 16462: {
                            playerInteractEvent.setCancelled(true);
                            player.sendMessage("§cInvisibility potions are currently disabled!");
                            itemInHand.setDurability((short)0);
                            break;
                        }
                    }
                }
                if (!ConfigBooleans.STRENGTH2.isEnabled()) {
                    switch (itemInHand.getDurability()) {
                        case 8233:
                        case 16425: {
                            playerInteractEvent.setCancelled(true);
                            player.sendMessage("§cStrength 2 potions are currently disabled!");
                            itemInHand.setDurability((short)0);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onLeavesDecayEvent(final LeavesDecayEvent leavesDecayEvent) {
        if (ScenarioManager.getInstance().getScenarioExact("TripleOres").isEnabled() || ((ScenarioManager.getInstance().getScenarioExact("Vanilla+").isEnabled() || ScenarioManager.getInstance().getScenarioExact("CutClean").isEnabled()) && Math.random() * 100.0 <= 1.0)) {
            leavesDecayEvent.getBlock().getWorld().dropItemNaturally(leavesDecayEvent.getBlock().getLocation(), new ItemStack(Material.APPLE));
        }
        if (this.gameManager.getAppleRates() > 1.0 && Math.random() * 100.0 <= this.gameManager.getAppleRates()) {
            leavesDecayEvent.getBlock().getWorld().dropItemNaturally(leavesDecayEvent.getBlock().getLocation(), new ItemStack(Material.APPLE));
        }
        if (ScenarioManager.getInstance().getScenarioExact("LuckyLeaves").isEnabled() && Math.random() * 100.0 <= 0.25) {
            leavesDecayEvent.getBlock().getWorld().dropItemNaturally(leavesDecayEvent.getBlock().getLocation(), new ItemStack(Material.GOLDEN_APPLE));
        }
    }
    
    @EventHandler
    public void onPlayerItemConsumeEvent(final PlayerItemConsumeEvent playerItemConsumeEvent) {
        if (playerItemConsumeEvent.getItem() != null && playerItemConsumeEvent.getItem().getType().equals((Object)Material.GOLDEN_APPLE)) {
            final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(playerItemConsumeEvent.getPlayer().getUniqueId());
            if (!playerItemConsumeEvent.isCancelled() && this.gameManager.isStatsEnabled() && uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                if (playerItemConsumeEvent.getItem().getItemMeta() != null && playerItemConsumeEvent.getItem().getItemMeta().hasDisplayName() && playerItemConsumeEvent.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
                    uhcPlayer.addGoldenHeadsEaten();
                    uhcPlayer.addHeartsHealed(4);
                }
                else {
                    uhcPlayer.addGoldenApplesEaten();
                    uhcPlayer.addHeartsHealed(2);
                }
            }
            if (playerItemConsumeEvent.getItem().getItemMeta() != null && playerItemConsumeEvent.getItem().getItemMeta().hasDisplayName() && playerItemConsumeEvent.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
                playerItemConsumeEvent.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                playerItemConsumeEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            }
            if (!ConfigBooleans.ABSORPTION.isEnabled()) {
                new BukkitRunnable() {
                    public void run() {
                        playerItemConsumeEvent.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
                    }
                }.runTaskLaterAsynchronously((Plugin) UHC.getInstance(), 3L);
            }
        }
    }
    
    @EventHandler
    public void onGameWinEvent(final GameWinListener gameWinListener) {
        Bukkit.getServer().broadcastMessage(this.gameManager.getPrefix() + this.gameManager.getMainColor() + "Congratulations to " + gameWinListener.getWinner() + " for winning the UHC!");
        if (GameManager.getGameManager().isStatsEnabled()) {
            gameWinListener.getUHCPlayer().addWin();
            Bukkit.broadcastMessage(this.gameManager.getMainColor() + "Saving stats to database...");
            new BukkitRunnable() {
                public void run() {
                    final Iterator<UHCPlayer> iterator = PlayerManager.getPlayerManager().getUHCPlayers().values().iterator();
                    while (iterator.hasNext()) {
                        iterator.next().saveData();
                    }
                    Bukkit.broadcastMessage("§aSuccessfully saved stats!");
                }
            }.runTaskAsynchronously((Plugin) UHC.getInstance());
        }
    }
    
    @EventHandler
    public void onGameWinTeamEvent(final GameWinTeamListener gameWinTeamListener) {
        String string = "";
        final Iterator<UUID> iterator = gameWinTeamListener.getUUIDs().iterator();
        while (iterator.hasNext()) {
            string = string + Bukkit.getOfflinePlayer((UUID)iterator.next()).getName() + ", ";
        }
        Bukkit.getServer().broadcastMessage(this.gameManager.getPrefix() + this.gameManager.getMainColor() + "Congratulations to " + string + " for winning the UHC!");
        if (this.gameManager.isStatsEnabled()) {
            final Iterator<UHCPlayer> iterator2 = PlayerManager.getPlayerManager().uhcPlayersSet(gameWinTeamListener.getUUIDs()).iterator();
            while (iterator2.hasNext()) {
                iterator2.next().addWin();
            }
            Bukkit.broadcastMessage(this.gameManager.getMainColor() + "Saving stats to database...");
            new BukkitRunnable() {
                public void run() {
                    final Iterator<UHCPlayer> iterator = PlayerManager.getPlayerManager().getUHCPlayers().values().iterator();
                    while (iterator.hasNext()) {
                        iterator.next().saveData();
                    }
                    Bukkit.broadcastMessage("§aSuccessfully saved stats!");
                }
            }.runTaskAsynchronously((Plugin) UHC.getInstance());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEventHIGH(final AsyncPlayerChatEvent asyncPlayerChatEvent) {
        final UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(asyncPlayerChatEvent.getPlayer().getUniqueId());
        if (uhcPlayer.isSpectating() && uhcPlayer.didUseCommand()) {
            asyncPlayerChatEvent.setCancelled(true);
            this.sendSpecMessage(asyncPlayerChatEvent.getFormat().replace("%1$s", this.gameManager.getSpectatorPrefix() + asyncPlayerChatEvent.getPlayer().getName()).replace("%2$s", asyncPlayerChatEvent.getMessage()));
            uhcPlayer.setUsedCommand(false);
        }
        else if (uhcPlayer.hasTeamChatToggled()) {
            final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)asyncPlayerChatEvent.getPlayer());
            asyncPlayerChatEvent.setCancelled(true);
            team.sendMessage(TeamManager.getInstance().getTeamsPrefix() + GameManager.getGameManager().getSecondaryColor() + uhcPlayer.getName() + ": " + GameManager.getGameManager().getMainColor() + asyncPlayerChatEvent.getMessage());
        }
        else if (this.gameManager.getHostName().equalsIgnoreCase(asyncPlayerChatEvent.getPlayer().getName())) {
            asyncPlayerChatEvent.setFormat(asyncPlayerChatEvent.getFormat().replace("%1$s", this.gameManager.getHostPrefix() + asyncPlayerChatEvent.getPlayer().getName()));
        }
        else if (this.gameManager.getModerators().contains(asyncPlayerChatEvent.getPlayer())) {
            asyncPlayerChatEvent.setFormat(asyncPlayerChatEvent.getFormat().replace("%1$s", this.gameManager.getModeratorPrefix() + asyncPlayerChatEvent.getPlayer().getName()));
        }
        else if (uhcPlayer.isSpectating()) {
            asyncPlayerChatEvent.setCancelled(true);
            this.sendSpecMessage(asyncPlayerChatEvent.getFormat().replace("%1$s", this.gameManager.getSpectatorPrefix() + asyncPlayerChatEvent.getPlayer().getName()).replace("%2$s", asyncPlayerChatEvent.getMessage()));
        }
    }
    
    private void sendSpecMessage(final String s) {
        for (final UHCPlayer uhcPlayer : PlayerManager.getPlayerManager().getUHCPlayers().values()) {
            if (uhcPlayer.isSpectating()) {
                final Player player = Bukkit.getServer().getPlayer(uhcPlayer.getName());
                if (player == null) {
                    continue;
                }
                player.sendMessage(s);
            }
        }
    }
    
    private Inventory getAlive() {
        this.alive.clear();
        for (final UHCPlayer uhcPlayer : PlayerManager.getPlayerManager().getUHCPlayers().values()) {
            if (uhcPlayer.isPlayerAlive()) {
                final Player player = Bukkit.getServer().getPlayer(uhcPlayer.getName());
                if (player == null) {
                    continue;
                }
                this.alive.addItem(new ItemStack[] { this.gameManager.newItem(Material.SKULL_ITEM, "§a" + player.getName(), 3) });
            }
        }
        return this.alive;
    }
    
    private void randomPlayer(final Player player) {
        if (this.gameManager.isGameRunning()) {
            final ArrayList<Object> list = new ArrayList<Object>();
            for (final UHCPlayer uhcPlayer : PlayerManager.getPlayerManager().getUHCPlayers().values()) {
                if (uhcPlayer.isPlayerAlive()) {
                    list.add(uhcPlayer.getName());
                }
            }
            if (list.size() == 0) {
                return;
            }
            final Player player2 = Bukkit.getServer().getPlayer((String)list.get(new Random().nextInt(list.size() - 1)));
            if (player2 != null) {
                player.teleport(player2.getLocation());
            }
            else {
                player.sendMessage("§cCould not find a random player to teleport to!");
            }
        }
        else {
            player.sendMessage("§cThe game hasn't started yet!");
        }
    }
    
    @EventHandler
    public void onWeatherChangeEvent(final WeatherChangeEvent weatherChangeEvent) {
        if (weatherChangeEvent.toWeatherState()) {
            weatherChangeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onChunkUnloadEvent(final ChunkUnloadEvent chunkUnloadEvent) {
        if (this.gameManager.getChunks().contains(chunkUnloadEvent.getChunk())) {
            chunkUnloadEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onFoodLevelChangeEvent(final FoodLevelChangeEvent foodLevelChangeEvent) {
        if (foodLevelChangeEvent.getEntity() instanceof Player) {
            if (foodLevelChangeEvent.getEntity().getWorld().equals(this.gameManager.getSpawnWorld())) {
                foodLevelChangeEvent.setCancelled(true);
            }
            final Player player = (Player)foodLevelChangeEvent.getEntity();
            player.setSaturation(player.getSaturation() * 10.0f);
        }
    }
}
