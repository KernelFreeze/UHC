package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GRunnable;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.ScoreboardM;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class StartGameCMD implements CommandExecutor {
    private final GameManager gameManager;
    private int i;
    private int an;

    public StartGameCMD() {
        this.gameManager = GameManager.getGameManager();
        this.an = 300;
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!commandSender.hasPermission("uhc.start") && !this.gameManager.getHostName().equalsIgnoreCase(commandSender.getName())) {
            commandSender.sendMessage("§cNo Permission!");
            return true;
        }
        if (this.gameManager.isScattering() || this.gameManager.isGameRunning()) {
            commandSender.sendMessage("§cA UHC is already running!");
            return true;
        }
        commandSender.sendMessage("§aStarting UHC...");
        if (ScenarioManager.getInstance().getScenarioExact("RiskyRetrieval").isEnabled()) {
            this.gameManager.getUHCWorld().getHighestBlockAt(0, 0).getLocation().add(0.0, 1.0, 0.0).getBlock().setType(Material.ENDER_CHEST);
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.setWhitelisted(true);
        }
        this.start();
        commandSender.sendMessage("§aScatter started...");
        return false;
    }

    private void start() {
        this.gameManager.setCanBorderShrink(true);
        this.gameManager.setScattering(true);
        if (TeamManager.getInstance().isTeamsEnabled()) {
            TeamManager.getInstance().autoPlace();
        }
        final ArrayList<Player> list = new ArrayList<Player>();
        for (final Player player : this.gameManager.getSpawnLocation().getWorld().getPlayers()) {
            if (player != null && !this.gameManager.getModerators().contains(player)) {
                list.add(player);
            }
        }
        this.i = list.size() - 1;
        Bukkit.broadcastMessage(this.gameManager.getPrefix() + this.gameManager.getMainColor() + "UHC starts in " + this.startsIn(this.i + 1) + " seconds!");
        new BukkitRunnable() {
            public void run() {
                if (StartGameCMD.this.i < 0) {
                    this.cancel();
                    StartGameCMD.this.gameManager.setWorldWasUsed(true);
                    StartGameCMD.this.gameManager.setScattering(false);
                    for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if (player != null) {
                            player.setFoodLevel(20);
                            player.setLevel(0);
                            player.setHealth(20.0);
                            if (ScenarioManager.getInstance().getScenarioExact("GoneFishing").isEnabled() && !StartGameCMD.this.gameManager.getModerators().contains(player)) {
                                final ItemStack itemStack = new ItemStack(Material.FISHING_ROD);
                                itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 150);
                                itemStack.addUnsafeEnchantment(Enchantment.LUCK, 250);
                                player.getInventory().addItem(itemStack);
                                player.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                player.setLevel(999999);
                            }
                            for (PotionEffect pot : player.getActivePotionEffects()) {
                                player.removePotionEffect(pot.getType());
                            }
                            new ScoreboardM().newScoreboard(player);
                        }
                    }
                    StartGameCMD.this.gameManager.getUHCWorld().setPVP(false);
                    StartGameCMD.this.gameManager.setGameRunning(true);
                    new GRunnable().runTaskTimerAsynchronously((Plugin) UHC.getInstance(), 20L, 20L);
                    Bukkit.getServer().broadcastMessage(StartGameCMD.this.gameManager.getPrefix() + StartGameCMD.this.gameManager.getMainColor() + " The game has started!");
                } else {
                    StartGameCMD.this.an -= StartGameCMD.this.gameManager.getScatterTicks();
                    if (StartGameCMD.this.an <= 0) {
                        StartGameCMD.this.an = 300;
                        Bukkit.broadcastMessage(StartGameCMD.this.gameManager.getPrefix() + StartGameCMD.this.gameManager.getMainColor() + "UHC starts in " + StartGameCMD.this.startsIn(StartGameCMD.this.i + 1) + " seconds!");
                    }
                    try {
                        list.get(StartGameCMD.this.i);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        try {
                            list.remove(StartGameCMD.this.i);
                        } catch (ArrayIndexOutOfBoundsException ex2) {
                            System.out.println("Null remove player scatter.");
                        }
                    }
                    try {
                        final Player player2 = list.get(StartGameCMD.this.i);
                        if (player2 != null) {
                            if (player2.getWorld().equals(StartGameCMD.this.gameManager.getSpawnLocation().getWorld())) {
                                StartGameCMD.this.gameManager.scatterPlayer(player2);
                                player2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 999));
                                player2.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 999));
                            } else {
                                list.remove(StartGameCMD.this.i);
                            }
                            StartGameCMD.this.i--;
                        }
                    } catch (ArrayIndexOutOfBoundsException ex3) {
                        System.out.println("Null player scatter.");
                    }
                }
            }
        }.runTaskTimer((Plugin) UHC.getInstance(), (long) this.gameManager.getScatterTicks(), (long) this.gameManager.getScatterTicks());
    }

    private int startsIn(final int n) {
        return n * this.gameManager.getScatterTicks() / 20;
    }
}
