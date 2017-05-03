package me.kernelfreeze.uhc.game;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.configs.ConfigIntegers;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;
import org.bukkit.potion.*;
import org.bukkit.enchantments.*;
import org.bukkit.*;

import java.util.*;

public class GameManager
{
    private static GameManager instance;
    private final FileConfiguration config;
    private final String name;
    private final String scoreboardTitle;
    private final String scoreboardIP;
    private final String prefix;
    private final String borderPrefix;
    private final String configPrefix;
    private final String helpopPrefix;
    private final String hostPrefix;
    private final String moderatorPrefix;
    private final String spectatorPrefix;
    private final String kickDeathMessage;
    private final String uhcWorld;
    private final String restartCommand;
    private final ChatColor mainColor;
    private final ChatColor secondaryColor;
    private World spawnWorld;
    private int defaultMapSize;
    private int currentBorder;
    private double appleRates;
    private final int killOnQuitTime;
    private final int helpopCoolDown;
    private final int randomShrinkAtAndBelow;
    private final int deathKickTime;
    private final int scatterTicks;
    private boolean borderShrinks;
    private boolean canOpenInv;
    private boolean statsEnabled;
    private boolean canUseRescatter;
    private boolean gameRunning;
    private boolean scattering;
    private boolean pvpEnabled;
    private boolean mapGenerating;
    private boolean restarted;
    private boolean worldUsed;
    private final boolean lobbySb;
    private final boolean kickPlayerOnDeath;
    private final boolean restartOnEnd;
    private final Set<Player> moderators;
    private final Set<Chunk> chunks;
    private final Material starterFood;
    private final ItemStack playersAlive;
    private final ItemStack randomPlayer;
    private Player host;
    
    public GameManager() {
        this.config = UHC.getInstance().getConfig();
        this.name = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.name"));
        this.scoreboardTitle = ChatColor.translateAlternateColorCodes('&', this.config.getString("scoreboard.scoreboard-title"));
        this.scoreboardIP = ChatColor.translateAlternateColorCodes('&', this.config.getString("scoreboard.scoreboard-ip"));
        this.prefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.uhc-prefix"));
        this.borderPrefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.border-prefix"));
        this.configPrefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.config-prefix"));
        this.helpopPrefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.helpop-prefix"));
        this.hostPrefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.host-prefix"));
        this.moderatorPrefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.moderator-prefix"));
        this.spectatorPrefix = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.spectator-prefix"));
        this.kickDeathMessage = ChatColor.translateAlternateColorCodes('&', this.config.getString("settings.kick-player-on-death-message"));
        this.uhcWorld = this.config.getString("settings.uhc-world-name");
        this.restartCommand = this.config.getString("settings.restart-command");
        this.mainColor = ChatColor.valueOf(this.config.getString("settings.main-chat-color").toUpperCase());
        this.secondaryColor = ChatColor.valueOf(this.config.getString("settings.secondary-chat-color").toUpperCase());
        this.spawnWorld = Bukkit.getWorld(this.config.getString("settings.spawn-world-name"));
        this.defaultMapSize = this.config.getInt("settings.default-map-size");
        this.currentBorder = this.defaultMapSize;
        this.appleRates = 0.5;
        this.killOnQuitTime = this.config.getInt("settings.kill-player-on-quit-after-minutes");
        this.helpopCoolDown = this.config.getInt("settings.helpop-cooldown-seconds");
        this.randomShrinkAtAndBelow = this.config.getInt("settings.random-border-shrink-at-and-below");
        this.deathKickTime = this.config.getInt("settings.kick-on-death-after-seconds");
        this.scatterTicks = this.config.getInt("settings.scatter-ticks");
        this.borderShrinks = true;
        this.canOpenInv = this.config.getBoolean("settings.spectators-can-open-inventory-on-right-click");
        this.statsEnabled = this.config.getBoolean("stats.enabled");
        this.canUseRescatter = true;
        this.gameRunning = false;
        this.scattering = false;
        this.pvpEnabled = false;
        this.mapGenerating = false;
        this.restarted = this.config.getBoolean("DO-NOT-TOUCH.wrs");
        this.worldUsed = this.config.getBoolean("DO-NOT-TOUCH.wmu");
        this.lobbySb = this.config.getBoolean("scoreboard.lobby-scoreboard-enabled");
        this.kickPlayerOnDeath = this.config.getBoolean("settings.kick-player-on-death");
        this.restartOnEnd = this.config.getBoolean("settings.restart-on-game-finish");
        this.moderators = new HashSet<Player>();
        this.chunks = new HashSet<Chunk>();
        this.starterFood = Material.valueOf(UHC.getInstance().getConfig().getString("settings.starter-food-item"));
        this.playersAlive = this.newItem(Material.DIAMOND, this.mainColor + "Players Alive", 0);
        this.randomPlayer = this.newItem(Material.SKULL_ITEM, this.mainColor + "Random Player", 3);
    }
    
    public boolean canBorderShrink() {
        return this.borderShrinks;
    }
    
    public void setCanBorderShrink(final boolean borderShrinks) {
        this.borderShrinks = borderShrinks;
    }
    
    public double getAppleRates() {
        return this.appleRates;
    }
    
    public void setAppleRates(final double appleRates) {
        this.appleRates = appleRates;
    }
    
    public String getRestartCommand() {
        return this.restartCommand;
    }
    
    public String gameType() {
        if (TeamManager.getInstance().isTeamsEnabled()) {
            return "To" + TeamManager.getInstance().getMaxSize();
        }
        return "FFA";
    }
    
    public int getScatterTicks() {
        return this.scatterTicks;
    }
    
    public World getSpawnWorld() {
        return this.spawnWorld;
    }
    
    public static GameManager getGameManager() {
        return GameManager.instance;
    }
    
    public Set<Chunk> getChunks() {
        return this.chunks;
    }
    
    public ItemStack newItem(final Material material, final String displayName, final int n) {
        final ItemStack itemStack = new ItemStack(material, 1, (short)n);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public boolean lobbyScoreboard() {
        return this.lobbySb;
    }
    
    public boolean openInvOnRightClick() {
        return this.canOpenInv;
    }
    
    public boolean canUseRescatter() {
        return this.canUseRescatter;
    }
    
    void setCanUseRescatter() {
        this.canUseRescatter = false;
    }
    
    public String getSpectatorPrefix() {
        return this.spectatorPrefix;
    }
    
    public boolean kickPlayerOnDeath() {
        return this.kickPlayerOnDeath;
    }
    
    public int getDeathKickTime() {
        return this.deathKickTime;
    }
    
    public String getKickDeathMessage() {
        return this.kickDeathMessage;
    }
    
    public int getHelpopCoolDown() {
        return this.helpopCoolDown;
    }
    
    public int getKillOnQuitTime() {
        return this.killOnQuitTime;
    }
    
    String getScoreboardIP() {
        return this.scoreboardIP;
    }
    
    String getScoreboardTitle() {
        return this.scoreboardTitle;
    }
    
    private Material getStarterFood() {
        return this.starterFood;
    }
    
    public boolean isStatsEnabled() {
        return this.statsEnabled;
    }
    
    public static void setInstance() {
        GameManager.instance = new GameManager();
    }
    
    public boolean restartOnEnd() {
        return this.restartOnEnd;
    }
    
    public String getHostPrefix() {
        return this.hostPrefix;
    }
    
    public String getModeratorPrefix() {
        return this.moderatorPrefix;
    }
    
    public void setHost(final Player host) {
        if (host != null) {
            host.sendMessage("§aYou are now the host of the game!");
        }
        this.host = host;
    }
    
    public boolean wasWorldUsed() {
        return this.worldUsed;
    }
    
    public void setWorldWasUsed(final boolean worldUsed) {
        UHC.getInstance().reloadConfig();
        UHC.getInstance().getConfig().set("DO-NOT-TOUCH.wmu", (Object)worldUsed);
        UHC.getInstance().saveConfig();
        this.worldUsed = worldUsed;
    }
    
    public boolean wasRestarted() {
        return this.restarted;
    }
    
    public void setRestarted(final boolean restarted) {
        UHC.getInstance().reloadConfig();
        UHC.getInstance().getConfig().set("DO-NOT-TOUCH.wrs", (Object)restarted);
        UHC.getInstance().saveConfig();
        this.restarted = restarted;
    }
    
    public String getHostName() {
        if (this.host == null) {
            return "";
        }
        return this.host.getName();
    }
    
    public String getHelpopPrefix() {
        return this.helpopPrefix;
    }
    
    public Set<String> getModeratorsNames() {
        final HashSet<String> set = new HashSet<String>();
        final Iterator<Player> iterator = this.moderators.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next().getName());
        }
        return set;
    }
    
    public Set<Player> getModerators() {
        return this.moderators;
    }
    
    public void setModerator(final Player player, final boolean b) {
        if (b) {
            PlayerManager.getPlayerManager().setSpectating(true, player);
            this.moderators.add(player);
            player.sendMessage("§aYou are now part of the moderators!");
        }
        else {
            PlayerManager.getPlayerManager().setSpectating(false, player);
            this.moderators.remove(player);
            player.sendMessage("§cYou are no longer a moderator!");
        }
    }
    
    public Location getScatterLocation() {
        final Random random = new Random();
        final int n = random.nextInt(this.currentBorder * 2) - this.currentBorder;
        final int n2 = random.nextInt(this.currentBorder * 2) - this.currentBorder;
        return new Location(this.getUHCWorld(), (double)n, this.getUHCWorld().getHighestBlockYAt(n, n2) + 0.5, (double)n2);
    }
    
    public Location getSpawnLocation() {
        return new Location(this.spawnWorld, (double)this.config.getInt("settings.spawnLocation.x"), (double)this.config.getInt("settings.spawnLocation.y"), (double)this.config.getInt("settings.spawnLocation.z"));
    }
    
    public boolean isPvpEnabled() {
        return this.pvpEnabled;
    }
    
    public void setPvP(final boolean pvp) {
        if (this.getUHCWorld() != null) {
            this.pvpEnabled = pvp;
            this.getUHCWorld().setPVP(pvp);
            if (Bukkit.getWorld(this.uhcWorld + "_nether") != null) {
                Bukkit.getWorld(this.uhcWorld + "_nether").setPVP(pvp);
            }
            if (pvp) {
                Bukkit.broadcastMessage(this.prefix + this.mainColor + " PvP is now §aenabled!");
            }
            else {
                Bukkit.broadcastMessage(this.prefix + this.mainColor + " PvP is now §cdisabled!");
            }
        }
    }
    
    void healAll() {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setHealth(20.0);
            player.sendMessage(this.prefix + this.mainColor + "Final heal received!");
        }
    }
    
    void enablePermaDay() {
        this.getUHCWorld().setTime(0L);
        this.getUHCWorld().setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getServer().broadcastMessage(this.prefix + this.mainColor + " Permanent day has been enabled!");
    }
    
    public void setScattering(final boolean scattering) {
        this.scattering = scattering;
    }
    
    public World getUHCWorld() {
        return Bukkit.getWorld(this.uhcWorld);
    }
    
    public String getUhcWorldName() {
        return this.uhcWorld;
    }
    
    public boolean isScattering() {
        return this.scattering;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public ChatColor getMainColor() {
        return this.mainColor;
    }
    
    public ChatColor getSecondaryColor() {
        return this.secondaryColor;
    }
    
    public String getBorderPrefix() {
        return this.borderPrefix;
    }
    
    public String getConfigPrefix() {
        return this.configPrefix;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isGameRunning() {
        return this.gameRunning;
    }
    
    public void setGameRunning(final boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
    
    public int getDefaultMapSize() {
        return this.defaultMapSize;
    }
    
    public int getCurrentBorder() {
        return this.currentBorder;
    }
    
    public boolean isMapGenerating() {
        return this.mapGenerating;
    }
    
    public void setMapGenerating(final boolean mapGenerating) {
        this.mapGenerating = mapGenerating;
    }
    
    void startBorderShrink() {
        Bukkit.broadcastMessage(this.borderPrefix + this.mainColor + " The world border will shrink every " + this.secondaryColor + ConfigIntegers.BORDER_SHRINK_EVERY.get() + this.mainColor + " minutes, by " + this.secondaryColor + ConfigIntegers.BORDER_SHRINK_BY.get() + this.mainColor + " blocks, until " + this.secondaryColor + ConfigIntegers.BORDER_SHRINK_UNTIL.get() + this.mainColor + "x" + this.secondaryColor + ConfigIntegers.BORDER_SHRINK_UNTIL.get());
        new BorderRunnable().runTaskTimerAsynchronously((Plugin) UHC.getInstance(), 200L, 200L);
    }
    
    void startSeconds() {
        new BukkitRunnable() {
            int i = 10;
            
            public void run() {
                --this.i;
                if (this.i >= 1) {
                    Bukkit.broadcastMessage(GameManager.this.borderPrefix + GameManager.this.mainColor + " Border shrinking in " + GameManager.this.secondaryColor + this.i + GameManager.this.mainColor + " seconds!");
                }
                else if (this.i == 0) {
                    if (GameManager.this.currentBorder > 500) {
                        GameManager.this.currentBorder -= ConfigIntegers.BORDER_SHRINK_BY.get();
                        GameManager.this.shrinkBorder(GameManager.this.currentBorder, this);
                        Bukkit.getServer().broadcastMessage(GameManager.this.borderPrefix + GameManager.this.mainColor + " The world border has shrunk to " + GameManager.this.secondaryColor + GameManager.this.currentBorder + GameManager.this.mainColor + "x" + GameManager.this.secondaryColor + GameManager.this.currentBorder);
                    }
                    else if (GameManager.this.currentBorder <= 500 && GameManager.this.currentBorder > 100) {
                        GameManager.this.currentBorder = 100;
                        GameManager.this.shrinkBorder(GameManager.this.currentBorder, this);
                        Bukkit.getServer().broadcastMessage(GameManager.this.borderPrefix + GameManager.this.mainColor + " The world border has shrunk to " + GameManager.this.secondaryColor + GameManager.this.currentBorder + GameManager.this.mainColor + "x" + GameManager.this.secondaryColor + GameManager.this.currentBorder);
                    }
                    else if (GameManager.this.currentBorder == 100) {
                        GameManager.this.currentBorder = 50;
                        GameManager.this.shrinkBorder(GameManager.this.currentBorder, this);
                        Bukkit.getServer().broadcastMessage(GameManager.this.borderPrefix + GameManager.this.mainColor + " The world border has shrunk to " + GameManager.this.secondaryColor + GameManager.this.currentBorder + GameManager.this.mainColor + "x" + GameManager.this.secondaryColor + GameManager.this.currentBorder);
                    }
                    else if (GameManager.this.currentBorder == 50) {
                        GameManager.this.currentBorder = 25;
                        GameManager.this.shrinkBorder(GameManager.this.currentBorder, this);
                        Bukkit.getServer().broadcastMessage(GameManager.this.borderPrefix + GameManager.this.mainColor + " The world border has shrunk to " + GameManager.this.secondaryColor + GameManager.this.currentBorder + GameManager.this.mainColor + "x" + GameManager.this.secondaryColor + GameManager.this.currentBorder);
                    }
                    else if (GameManager.this.currentBorder == 25) {
                        GameManager.this.currentBorder = 10;
                        GameManager.this.shrinkBorder(GameManager.this.currentBorder, this);
                        Bukkit.getServer().broadcastMessage(GameManager.this.borderPrefix + GameManager.this.mainColor + " The world border has shrunk to " + GameManager.this.secondaryColor + GameManager.this.currentBorder + GameManager.this.mainColor + "x" + GameManager.this.secondaryColor + GameManager.this.currentBorder);
                    }
                    new ScoreboardM().updateBorder();
                }
            }
        }.runTaskTimer((Plugin) UHC.getInstance(), 20L, 20L);
    }
    
    public void scatterPlayer(final Player player) {
        UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        if (uhcPlayer == null) {
            PlayerManager.getPlayerManager().createUHCPlayer(player.getUniqueId());
            uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        }
        final ItemStack itemInHand = new ItemStack(this.getStarterFood(), ConfigIntegers.STARTERFOOD.get());
        if (uhcPlayer.isSpectating()) {
            PlayerManager.getPlayerManager().setSpectating(false, player);
        }
        final Iterator<PotionEffect> iterator = (Iterator<PotionEffect>)player.getActivePotionEffects().iterator();
        while (iterator.hasNext()) {
            player.removePotionEffect(iterator.next().getType());
        }
        if (this.isScattering()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 999));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 999));
        }
        uhcPlayer.setPlayerAlive(true);
        uhcPlayer.setDied(false);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setHealth(20.0);
        player.getInventory().clear();
        player.getInventory().setArmorContents((ItemStack[])null);
        player.setGameMode(GameMode.SURVIVAL);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setItemInHand(itemInHand);
        uhcPlayer.setPlayerAlive(true);
        if (ScenarioManager.getInstance().getScenarioExact("RiskyRetrieval").isEnabled()) {
            player.getEnderChest().clear();
        }
        if (ScenarioManager.getInstance().getScenarioExact("ExtraInventory").isEnabled()) {
            player.getEnderChest().clear();
        }
        if (ScenarioManager.getInstance().getScenarioExact("GoneFishing").isEnabled() && !this.getModerators().contains(player)) {
            final ItemStack itemStack = new ItemStack(Material.FISHING_ROD);
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 150);
            itemStack.addUnsafeEnchantment(Enchantment.LUCK, 250);
            player.getInventory().addItem(new ItemStack[] { itemStack });
            player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ANVIL, 64) });
            player.setLevel(999999);
        }
        if (TeamManager.getInstance().isTeamsEnabled()) {
            final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)player);
            if (team == null) {
                final Team team2 = TeamManager.getInstance().getTeam((OfflinePlayer)player);
                TeamManager.getInstance().createTeam(player);
                player.teleport(team2.getScatterLocation());
            }
            else {
                player.teleport(team.getScatterLocation());
            }
        }
        else {
            player.teleport(this.getScatterLocation());
        }
    }
    
    void checkBorder(final Player player) {
        final int currentBorder = this.currentBorder;
        final World world = player.getWorld();
        if (!world.getName().equals(this.spawnWorld.getName())) {
            if (world.getEnvironment().equals((Object)World.Environment.NETHER)) {
                return;
            }
            if (player.getLocation().getBlockX() > currentBorder) {
                player.teleport(new Location(world, (double)(currentBorder - 2), (double)player.getLocation().getBlockY(), (double)player.getLocation().getBlockZ()));
                if (player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                    player.teleport(new Location(world, (double)player.getLocation().getBlockX(), (double)(world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2), (double)player.getLocation().getBlockZ()));
                    player.sendMessage(this.borderPrefix + this.mainColor + "You have reached the edge of this world!");
                }
            }
            if (player.getLocation().getBlockZ() > currentBorder) {
                player.teleport(new Location(world, (double)player.getLocation().getBlockX(), (double)player.getLocation().getBlockY(), (double)(currentBorder - 2)));
                if (player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                    player.teleport(new Location(world, (double)player.getLocation().getBlockX(), (double)(world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2), (double)player.getLocation().getBlockZ()));
                    player.sendMessage(this.borderPrefix + this.mainColor + "You have reached the edge of this world!");
                }
            }
            if (player.getLocation().getBlockX() < -currentBorder) {
                player.teleport(new Location(world, (double)(-currentBorder + 2), (double)player.getLocation().getBlockY(), (double)player.getLocation().getBlockZ()));
                if (player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                    player.teleport(new Location(world, (double)player.getLocation().getBlockX(), (double)(world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2), (double)player.getLocation().getBlockZ()));
                    player.sendMessage(this.borderPrefix + this.mainColor + "You have reached the edge of this world!");
                }
            }
            if (player.getLocation().getBlockZ() < -currentBorder) {
                player.teleport(new Location(world, (double)player.getLocation().getBlockX(), (double)player.getLocation().getBlockY(), (double)(-currentBorder + 2)));
                if (player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                    player.teleport(new Location(world, (double)player.getLocation().getBlockX(), (double)(world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2), (double)player.getLocation().getBlockZ()));
                    player.sendMessage(this.borderPrefix + this.mainColor + "You have reached the edge of this world!");
                }
            }
        }
    }
    
    public void setStatsEnabled(final boolean statsEnabled) {
        this.statsEnabled = statsEnabled;
    }
    
    public ItemStack getPlayersAlive() {
        return this.playersAlive;
    }
    
    public void shrinkBorder(final int currentBorder, final BukkitRunnable bukkitRunnable) {
        new BorderRunnable().runTaskTimerAsynchronously((Plugin) UHC.getInstance(), 200L, 200L);
        bukkitRunnable.cancel();
        this.currentBorder = currentBorder;
        final World uhcWorld = this.getUHCWorld();
        if (currentBorder > this.randomShrinkAtAndBelow) {
            for (final Player player : this.getUHCWorld().getPlayers()) {
                if (player.getLocation().getBlockX() > currentBorder) {
                    player.setNoDamageTicks(55);
                    player.setFallDistance(0.0f);
                    player.teleport(new Location(uhcWorld, (double)(currentBorder - 4), uhcWorld.getHighestBlockYAt(currentBorder - 4, player.getLocation().getBlockZ()) + 0.5, (double)player.getLocation().getBlockZ()));
                    player.getLocation().add(0.0, 2.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 3.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 4.0, 0.0).getBlock().setType(Material.AIR);
                }
                if (player.getLocation().getBlockZ() > currentBorder) {
                    player.setNoDamageTicks(55);
                    player.setFallDistance(0.0f);
                    player.teleport(new Location(uhcWorld, (double)player.getLocation().getBlockX(), uhcWorld.getHighestBlockYAt(player.getLocation().getBlockX(), currentBorder - 4) + 0.5, (double)(currentBorder - 4)));
                    player.getLocation().add(0.0, 2.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 3.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 4.0, 0.0).getBlock().setType(Material.AIR);
                }
                if (player.getLocation().getBlockX() < -currentBorder) {
                    player.setNoDamageTicks(55);
                    player.setFallDistance(0.0f);
                    player.teleport(new Location(uhcWorld, (double)(-currentBorder + 4), uhcWorld.getHighestBlockYAt(-currentBorder + 4, player.getLocation().getBlockZ()) + 0.5, (double)player.getLocation().getBlockZ()));
                    player.getLocation().add(0.0, 2.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 3.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 4.0, 0.0).getBlock().setType(Material.AIR);
                }
                if (player.getLocation().getBlockZ() < -currentBorder) {
                    player.setNoDamageTicks(55);
                    player.setFallDistance(0.0f);
                    player.teleport(new Location(uhcWorld, (double)player.getLocation().getBlockX(), uhcWorld.getHighestBlockYAt(player.getLocation().getBlockX(), -currentBorder + 4) + 0.5, (double)(-currentBorder + 4)));
                    player.getLocation().add(0.0, 2.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 3.0, 0.0).getBlock().setType(Material.AIR);
                    player.getLocation().add(0.0, 4.0, 0.0).getBlock().setType(Material.AIR);
                }
            }
        }
        else if (TeamManager.getInstance().isTeamsEnabled()) {
            for (final Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                if (!player2.getWorld().equals(this.getSpawnLocation().getWorld()) && (player2.getLocation().getBlockX() > currentBorder || player2.getLocation().getBlockZ() > currentBorder || player2.getLocation().getBlockX() < -currentBorder || player2.getLocation().getBlockZ() < -currentBorder || player2.getWorld().getName().equalsIgnoreCase(this.getUhcWorldName() + "_nether"))) {
                    final Team team = TeamManager.getInstance().getTeam((OfflinePlayer)player2);
                    final Iterator<UUID> iterator2 = team.getPlayers().iterator();
                    while (iterator2.hasNext()) {
                        final Player player3 = Bukkit.getServer().getPlayer((UUID)iterator2.next());
                        if (player3 != null) {
                            team.setScatterLocation(this.getScatterLocation());
                            player2.setNoDamageTicks(53);
                            player2.setFallDistance(0.0f);
                            player3.teleport(team.getScatterLocation());
                            player2.getLocation().add(0.0, 2.0, 0.0).getBlock().setType(Material.AIR);
                            player2.getLocation().add(0.0, 3.0, 0.0).getBlock().setType(Material.AIR);
                            player2.getLocation().add(0.0, 4.0, 0.0).getBlock().setType(Material.AIR);
                            player2.sendMessage(this.borderPrefix + this.mainColor + "You were shrunk in the border!");
                        }
                    }
                }
            }
        }
        else {
            for (final Player player4 : Bukkit.getServer().getOnlinePlayers()) {
                if (!player4.getWorld().equals(this.getSpawnLocation().getWorld()) && (player4.getLocation().getBlockX() > currentBorder || player4.getLocation().getBlockZ() > currentBorder || player4.getLocation().getBlockX() < -currentBorder || player4.getLocation().getBlockZ() < -currentBorder || player4.getWorld().getName().equalsIgnoreCase(this.getUhcWorldName() + "_nether"))) {
                    player4.setNoDamageTicks(53);
                    player4.setFallDistance(0.0f);
                    player4.teleport(this.getScatterLocation());
                    player4.getLocation().add(0.0, 2.0, 0.0).getBlock().setType(Material.AIR);
                    player4.getLocation().add(0.0, 3.0, 0.0).getBlock().setType(Material.AIR);
                    player4.getLocation().add(0.0, 4.0, 0.0).getBlock().setType(Material.AIR);
                    player4.sendMessage(this.borderPrefix + this.mainColor + "You were shrunk in the border!");
                }
            }
        }
        this.buildWalls(currentBorder, Material.BEDROCK, 4, uhcWorld);
    }
    
    public ItemStack getRandomPlayer() {
        return this.randomPlayer;
    }
    
    public void buildWalls(final int n, final Material type, final int n2, final World world) {
        final Location location = new Location(world, 0.0, 59.0, 0.0);
        for (int i = n2; i < n2 + n2; ++i) {
            for (int j = location.getBlockX() - n; j <= location.getBlockX() + n; ++j) {
                for (int k = 58; k <= 58; ++k) {
                    for (int l = location.getBlockZ() - n; l <= location.getBlockZ() + n; ++l) {
                        if (j == location.getBlockX() - n || j == location.getBlockX() + n || l == location.getBlockZ() - n || l == location.getBlockZ() + n) {
                            final Location location2 = new Location(world, (double)j, (double)k, (double)l);
                            location2.setY((double)world.getHighestBlockYAt(location2));
                            location2.getBlock().setType(type);
                        }
                    }
                }
            }
        }
    }
}
