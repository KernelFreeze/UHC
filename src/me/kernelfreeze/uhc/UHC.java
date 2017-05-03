package me.kernelfreeze.uhc;

import me.kernelfreeze.uhc.cmds.*;
import me.kernelfreeze.uhc.configs.ConfigCMD;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.listeners.*;
import me.kernelfreeze.uhc.scenarios.BackPackCMD;
import me.kernelfreeze.uhc.scenarios.ExtraInventoryCMD;
import me.kernelfreeze.uhc.scenarios.ScenarioCMD;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.commands.SendCoordsCMD;
import me.kernelfreeze.uhc.teams.commands.TeamCMD;
import me.kernelfreeze.uhc.teams.commands.TeamChatCMD;
import me.kernelfreeze.uhc.teams.commands.TeamListCMD;
import me.kernelfreeze.uhc.teams.request.RequestManager;
import me.kernelfreeze.uhc.util.SQL;
import me.kernelfreeze.uhc.world.WorldCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UHC extends JavaPlugin {
    private static UHC instance;
    private final Map<Player, Scoreboard> scoreboardMap;
    private final Map<Player, Scoreboard> lobbyScoreboardMap;
    private SQL sql;

    public UHC() {
        this.scoreboardMap = new HashMap<>();
        this.lobbyScoreboardMap = new HashMap<>();
    }

    public static UHC getInstance() {
        return UHC.instance;
    }

    public Map<Player, Scoreboard> getPlayersScoreboard() {
        return this.scoreboardMap;
    }

    public Map<Player, Scoreboard> getLobbyScoreboardMap() {
        return this.lobbyScoreboardMap;
    }

    public SQL getSQL() {
        return this.sql;
    }

    public void onEnable() {
        (UHC.instance = this).loadConfiguration();
        GameManager.setInstance();
        ScenarioManager.getInstance().createAllScenarios();
        this.registerCommands();
        this.registerEvents();
        new RequestManager().register(this);
        this.registerItems();
        if (!this.exists(GameManager.getGameManager().getUhcWorldName())) {
            GameManager.getGameManager().setRestarted(false);
        }
        this.startRunnable();
        if (this.getConfig().getBoolean("stats.enabled")) {
            (this.sql = new SQL()).openConnection();
            this.sql.createTables();
        }
    }

    private boolean exists(final String s) {
        return new File(s).exists();
    }

    public void onDisable() {
        if (GameManager.getGameManager().isGameRunning()) {
            Bukkit.getServer().getPluginManager().callEvent((Event) new GameStopEvent("On disable (game running)"));
            GameManager.getGameManager().setRestarted(false);
            GameManager.getGameManager().setWorldWasUsed(true);
        }
        if (GameManager.getGameManager().isMapGenerating()) {
            GameManager.getGameManager().setRestarted(false);
        }
        if (this.getConfig().getBoolean("stats.enabled")) {
            this.sql.closeConnection();
        }
        UHC.instance = null;
    }

    private void registerCommands() {
        //this.getCommand("heal").setExecutor(new HealCMD());
        //Bukkit.getServer().getPluginCommand("teleport").setExecutor(new TeleportCMD());
        //final InvseeCMD invseeCMD = new InvseeCMD();
        //this.getCommand("invsee").setExecutor(invseeCMD);
        //this.getCommand("armour").setExecutor(invseeCMD);
        //this.getCommand("game").setExecutor(new FriendAddCMD());

        this.getCommand("rescatter").setExecutor(new ReScatterCMD());
        this.getCommand("bestkillers").setExecutor(new BestKillersCMD());
        this.getCommand("createuhc").setExecutor(new CreateCMD());
        this.getCommand("health").setExecutor(new HealthCMD());
        this.getCommand("helpop").setExecutor(new HelpOpCMD());
        this.getCommand("killcount").setExecutor(new KillCountCMD());
        this.getCommand("border").setExecutor(new BorderCMD());
        this.getCommand("respawn").setExecutor(new RespawnCMD());
        this.getCommand("startuhc").setExecutor(new StartGameCMD());
        this.getCommand("stopuhc").setExecutor(new StopGameCMD());
        this.getCommand("config").setExecutor(new ConfigCMD());
        this.getCommand("stats").setExecutor(new StatsCMD());
        this.getCommand("host").setExecutor(new HostCMD());
        this.getCommand("moderator").setExecutor(new ModeratorCMD());
        this.getCommand("revive").setExecutor(new ReviveCMD());
        this.getCommand("spectator").setExecutor(new SpectatorCMD());
        this.getCommand("spectatorchat").setExecutor(new SpectatorChatCMD());
        this.getCommand("wl").setExecutor(new WlCMD());
        Bukkit.getServer().getPluginCommand("list").setExecutor(new ListCMD());
        Bukkit.getServer().getPluginCommand("whitelist").setExecutor(new WhitelistCMD());
        this.getCommand("team").setExecutor(new TeamCMD());
        this.getCommand("teamlist").setExecutor(new TeamListCMD());
        this.getCommand("sendcoords").setExecutor(new SendCoordsCMD());
        this.getCommand("teamchat").setExecutor(new TeamChatCMD());
        this.getCommand("scenario").setExecutor(new ScenarioCMD());
        this.getCommand("backpack").setExecutor(new BackPackCMD());
        this.getCommand("extrainventory").setExecutor(new ExtraInventoryCMD());
    }

    private void registerEvents() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(), (Plugin) this);
        pluginManager.registerEvents(new BlockPlaceListener(), (Plugin) this);
        pluginManager.registerEvents(new InventoryListeners(), (Plugin) this);
        pluginManager.registerEvents(new PlayerDeathListener(), (Plugin) this);
        pluginManager.registerEvents(new PlayerJoinListener(), (Plugin) this);
        pluginManager.registerEvents(new PlayerQuitListener(), (Plugin) this);
        pluginManager.registerEvents(new Listeners(), (Plugin) this);
        if (this.getConfig().getBoolean("settings.using-world-border-plugin")) {
            pluginManager.registerEvents(new WorldBorderFillListener(), (Plugin) this);
        }
    }

    private void loadConfiguration() {
        final String s = "settings.";
        this.getConfig().addDefault("DO-NOT-TOUCH.wmu", (Object) false);
        this.getConfig().addDefault("DO-NOT-TOUCH.wrs", (Object) false);
        this.getConfig().addDefault("available-chat-colors", (Object) "DARK_BLUE, WHITE, AQUA, BLACK, GOLD, DARK_RED, BLUE, DARK_AQUA, DARK_GRAY, DARK_GREEN, DARK_PURPLE, GRAY, GREEN, LIGHT_PURPLE, RED, YELLOW");
        this.getConfig().addDefault(s + "name", (Object) "UHC");
        this.getConfig().addDefault(s + "version", (Object) 3.0);
        this.getConfig().addDefault(s + "restart-command", (Object) "/restart");
        this.getConfig().addDefault(s + "scatter-ticks", (Object) 20);
        this.getConfig().addDefault(s + "uhc-prefix", (Object) "&b[&6UHC&b]&r");
        this.getConfig().addDefault(s + "border-prefix", (Object) "&b[&6Border&b]&r");
        this.getConfig().addDefault(s + "config-prefix", (Object) "&b[&6Config&b]&r");
        this.getConfig().addDefault(s + "helpop-prefix", (Object) "&b[&6HelpOp&b]&r");
        this.getConfig().addDefault(s + "teams-prefix", (Object) "&b[&6Teams&b]&r");
        this.getConfig().addDefault(s + "host-prefix", (Object) "&4[Host]&r");
        this.getConfig().addDefault(s + "moderator-prefix", (Object) "&2[Moderator]&r");
        this.getConfig().addDefault(s + "spectator-prefix", (Object) "&b[Spectator]&r");
        this.getConfig().addDefault(s + "main-chat-color", (Object) "YELLOW");
        this.getConfig().addDefault(s + "secondary-chat-color", (Object) "RED");
        this.getConfig().addDefault(s + "uhc-world-name", (Object) "uhc_world");
        this.getConfig().addDefault(s + "spawn-world-name", (Object) "world");
        this.getConfig().addDefault(s + "spawnLocation.x", (Object) 0);
        this.getConfig().addDefault(s + "spawnLocation.y", (Object) 64);
        this.getConfig().addDefault(s + "spawnLocation.z", (Object) 0);
        this.getConfig().addDefault(s + "default-map-size", (Object) 2000);
        this.getConfig().addDefault(s + "random-border-shrink-at-and-below", (Object) 500);
        this.getConfig().addDefault(s + "starter-food-item", (Object) "COOKED_BEEF");
        this.getConfig().addDefault(s + "kill-player-on-quit-after-minutes", (Object) 10);
        this.getConfig().addDefault(s + "helpop-cooldown-seconds", (Object) 10);
        this.getConfig().addDefault(s + "kick-player-on-death", (Object) true);
        this.getConfig().addDefault(s + "kick-player-on-death-message", (Object) "&bYou died in the UHC, thanks for playing!");
        this.getConfig().addDefault(s + "kick-on-death-after-seconds", (Object) 25);
        this.getConfig().addDefault(s + "restart-on-game-finish", (Object) true);
        this.getConfig().addDefault(s + "using-world-border-plugin", (Object) false);
        this.getConfig().addDefault(s + "load-chunks-per-second", (Object) 50);
        this.getConfig().addDefault(s + "restart-on-chunk-load-finish", (Object) true);
        this.getConfig().addDefault(s + "spectators-can-open-inventory-on-right-click", (Object) false);
        this.getConfig().addDefault("scoreboard.lobby-scoreboard-enabled", (Object) true);
        this.getConfig().addDefault("scoreboard.scoreboard-title", (Object) "&bUHC");
        this.getConfig().addDefault("scoreboard.scoreboard-ip", (Object) "&bUHC.xyz.net");
        this.getConfig().addDefault("stats.enabled", (Object) false);
        this.getConfig().addDefault("stats.SQL.ip", (Object) "localhost");
        this.getConfig().addDefault("stats.SQL.database", (Object) "databasehere");
        this.getConfig().addDefault("stats.SQL.table", (Object) "tablenamehere");
        this.getConfig().addDefault("stats.SQL.user", (Object) "userforDatabase");
        this.getConfig().addDefault("stats.SQL.password", (Object) "passForUser");
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void registerItems() {
        final ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§6Golden Head");
        final ArrayList<String> lore = new ArrayList<String>();
        lore.add("§5Some say consuming the head of a");
        lore.add("§5fallen foe strengthens the blood");
        lore.add(GameManager.getGameManager().getPrefix());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        final ShapedRecipe shapedRecipe = new ShapedRecipe(itemStack);
        shapedRecipe.shape("EEE", "ERE", "EEE");
        shapedRecipe.setIngredient('E', Material.GOLD_INGOT).setIngredient('R', Material.SKULL_ITEM, 3);
        Bukkit.getServer().addRecipe(shapedRecipe);
        final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON, 1));
        shapelessRecipe.addIngredient(Material.GOLD_BLOCK);
        shapelessRecipe.addIngredient(Material.MELON);
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }

    private void startRunnable() {
        new BukkitRunnable() {
            public void run() {
                if (UHC.this.getConfig().getBoolean("DO-NOT-TOUCH.wmu")) {
                    new WorldCreator(true, true);
                } else {
                    new WorldCreator(true, false);
                }
            }
        }.runTaskLater(this, 280L);
    }
}
