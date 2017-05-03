package me.kernelfreeze.uhc.world;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.LobbyScoreboard;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;

public class WorldCreator {
    private final GameManager gameManager;
    private final LobbyScoreboard sb;

    public WorldCreator(final boolean b, final boolean b2) {
        this.gameManager = GameManager.getGameManager();
        this.sb = new LobbyScoreboard();
        this.gameManager.setMapGenerating(true);
        if (b2 && !b) {
            this.deleteWorld(false);
            return;
        }
        if (b2) {
            this.deleteWorld(true);
            return;
        }
        if (!b) {
            this.deleteWorld(false);
            return;
        }
        if (b) {
            if (!this.gameManager.wasWorldUsed()) {
                this.createWorld();
            } else {
                this.deleteWorld(true);
            }
        }
    }

    private void createWorld() {
        final int defaultMapSize = this.gameManager.getDefaultMapSize();
        final World world = Bukkit.getServer().createWorld(new org.bukkit.WorldCreator(this.gameManager.getUhcWorldName()).environment(World.Environment.NORMAL).type(WorldType.NORMAL));
        world.setTime(0L);
        world.setPVP(false);
        this.gameManager.setPvP(false);
        world.setDifficulty(Difficulty.NORMAL);
        world.setGameRuleValue("doDaylightCycle", "false");
        this.gameManager.buildWalls(this.gameManager.getDefaultMapSize(), Material.BEDROCK, 4, world);
        world.setSpawnLocation(0, 100, 0);
        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "multiverse-core:mvimport " + this.gameManager.getUhcWorldName() + " normal");
        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "multiverse-core:mvimport " + this.gameManager.getUhcWorldName() + "_nether nether");
        this.gameManager.setMapGenerating(false);
        this.gameManager.setWorldWasUsed(false);
        if (UHC.getInstance().getConfig().getBoolean("settings.using-world-border-plugin") && !this.gameManager.wasRestarted()) {
            this.gameManager.setMapGenerating(true);
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb whoosh off");
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb denypearl on");
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb " + world.getName() + " setcorners " + defaultMapSize + " -" + defaultMapSize + " -" + defaultMapSize + " " + defaultMapSize);
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb " + WorldCreator.this.gameManager.getUhcWorldName() + "_nether setcorners " + defaultMapSize / 8 + " -" + defaultMapSize / 8 + " -" + defaultMapSize / 8 + " " + defaultMapSize / 8);
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb shape square");
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb setmsg \"\"");
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb portal on");
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb knockback 2");
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb " + world.getName() + " fill " + UHC.getInstance().getConfig().getInt("settings.load-chunks-per-second"));
                    Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "worldborder:wb " + world.getName() + " fill confirm");
                }
            }.runTaskLater((Plugin) UHC.getInstance(), 40L);
        }
        Bukkit.getServer().createWorld(new org.bukkit.WorldCreator(this.gameManager.getUhcWorldName() + "_nether").environment(World.Environment.NETHER).type(WorldType.NORMAL)).setPVP(false);
        if (this.gameManager.lobbyScoreboard()) {
            new BukkitRunnable() {
                public void run() {
                    if (!GameManager.getGameManager().isScattering()) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            WorldCreator.this.sb.updateScoreboard(p);
                        }
                    } else {
                        this.cancel();
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
                        }
                    }
                }
            }.runTaskTimerAsynchronously((Plugin) UHC.getInstance(), 20L, 20L);
        }
    }

    private void deleteWorld(final boolean b) {
        this.gameManager.setRestarted(false);
        final World world = Bukkit.getServer().getWorld(this.gameManager.getUhcWorldName());
        final World world2 = Bukkit.getServer().getWorld(this.gameManager.getUhcWorldName() + "_nether");
        if (world != null) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.teleport(this.gameManager.getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(world, false);
            this.deleteFile(world.getWorldFolder());
            this.deleteFile(world.getWorldFolder());
        }
        if (world2 != null) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.teleport(this.gameManager.getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(world2, false);
            this.deleteFile(world2.getWorldFolder());
            this.deleteFile(world2.getWorldFolder());
        }
        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "multiverse-core:mvdelete " + this.gameManager.getUhcWorldName());
        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "multiverse-core:mv confirm");
        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "multiverse-core:mvdelete " + this.gameManager.getUhcWorldName() + "_nether");
        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), "multiverse-core:mvconfirm");
        if (b) {
            new BukkitRunnable() {
                public void run() {
                    WorldCreator.this.createWorld();
                }
            }.runTaskLater((Plugin) UHC.getInstance(), 120L);
        }
    }

    private boolean deleteFile(final File file) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                if (!this.deleteFile(listFiles[i])) {
                    return false;
                }
            }
        }
        return file.delete();
    }
}
