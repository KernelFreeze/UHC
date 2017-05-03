package me.kernelfreeze.uhc.player;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.game.GameManager;
import org.bukkit.inventory.meta.*;
import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import me.kernelfreeze.uhc.*;
import java.sql.*;
import org.bukkit.plugin.*;
import me.kernelfreeze.uhc.game.*;
import java.util.*;

public class UHCPlayer
{
    private UUID uuid;
    private int kills;
    private int diamondsMinedGame;
    private int spawnersMined;
    private boolean playerAlive;
    private boolean playerDied;
    private boolean spectator;
    private boolean teamChatToggled;
    private ItemStack[] armour;
    private ItemStack[] items;
    private Location respawnLocation;
    private boolean usedCommand;
    private Inventory statsInventory;
    private int totalKills;
    private int totalDeath;
    private int highestKillStreak;
    private int wins;
    private int arrowsShot;
    private int arrowsHit;
    private int goldenApplesEaten;
    private int goldenHeadsEaten;
    private int heartsHealed;
    private int zombiesKilled;
    private int creepersKilled;
    private int skeletonsKilled;
    private int caveSpidersKilled;
    private int spidersKilled;
    private int blazesKilled;
    private int ghastsKilled;
    private int cowsKilled;
    private int pigsKilled;
    private int chickensKilled;
    private int horsesKilled;
    private int witchesKilled;
    private int netherEntrances;
    private int horsesTamed;
    private int xpLevelsEarned;
    private int totalDiamondsMined;
    private int totalGoldMined;
    private int totalRedstoneMined;
    private int totalLapisMined;
    private int totalIronMined;
    private int totalCoalMined;
    private int totalQuartzMined;
    private int totalSpawnersMined;
    
    private ItemStack buildItem(final Material material, final String displayName, final List<String> lore, final int n) {
        final ItemStack itemStack = new ItemStack(material, 1, (short)n);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public void toggleTeamChat(final CommandSender commandSender) {
        this.teamChatToggled = !this.teamChatToggled;
        commandSender.sendMessage("§aSuccessfully toggled team chat (" + this.teamChatToggled + ")");
    }
    
    public boolean hasTeamChatToggled() {
        return this.teamChatToggled;
    }
    
    public int getSpawnersMined() {
        return this.spawnersMined;
    }
    
    public void addSpawnersMined() {
        ++this.spawnersMined;
    }
    
    public boolean didUseCommand() {
        return this.usedCommand;
    }
    
    public void setUsedCommand(final boolean usedCommand) {
        this.usedCommand = usedCommand;
    }
    
    public void addArrowHit() {
        ++this.arrowsHit;
    }
    
    public void addArrowShot() {
        ++this.arrowsShot;
    }
    
    public void addHorsesTamed() {
        ++this.horsesTamed;
    }
    
    public void addTotalQuartzMined() {
        ++this.totalQuartzMined;
    }
    
    public void addTotalSpawnersMined() {
        ++this.totalSpawnersMined;
    }
    
    public void addHeartsHealed(final int n) {
        this.heartsHealed += n;
    }
    
    public void addTotalDiamondsMined() {
        ++this.totalDiamondsMined;
    }
    
    public void addTotalRedstoneMined() {
        ++this.totalRedstoneMined;
    }
    
    public void addTotalLapisMined() {
        ++this.totalLapisMined;
    }
    
    public void addTotalIronMined() {
        ++this.totalIronMined;
    }
    
    public void addTotalCoalMined() {
        ++this.totalCoalMined;
    }
    
    public void addTotalGoldMined() {
        ++this.totalGoldMined;
    }
    
    public void addNetherE() {
        ++this.netherEntrances;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public void addWin() {
        ++this.wins;
    }
    
    public void addGoldenApplesEaten() {
        ++this.goldenApplesEaten;
    }
    
    public void addGoldenHeadsEaten() {
        ++this.goldenHeadsEaten;
    }
    
    public void addHorsesKilled() {
        ++this.horsesKilled;
    }
    
    public void addWitchesKilled() {
        ++this.witchesKilled;
    }
    
    public void addCowsKilled() {
        ++this.cowsKilled;
    }
    
    public void addPigsKilled() {
        ++this.pigsKilled;
    }
    
    public void addChickensKilled() {
        ++this.chickensKilled;
    }
    
    public void addBlazesKilled() {
        ++this.blazesKilled;
    }
    
    public void addCaveSpiderKilled() {
        ++this.caveSpidersKilled;
    }
    
    public void addSpidersKilled() {
        ++this.spidersKilled;
    }
    
    public void addZombiesKilled() {
        ++this.zombiesKilled;
    }
    
    public void addCreepersKilled() {
        ++this.creepersKilled;
    }
    
    public void addSkeletonsKilled() {
        ++this.skeletonsKilled;
    }
    
    public void addGhastsKilled() {
        ++this.ghastsKilled;
    }
    
    public void addXPLevel() {
        ++this.xpLevelsEarned;
    }
    
    public int getHighestKillStreak() {
        return this.highestKillStreak;
    }
    
    public double getKd() {
        double n;
        if (this.totalKills > 0 && this.totalDeath == 0) {
            n = this.totalKills;
        }
        else if (this.totalKills == 0 && this.totalDeath == 0) {
            n = 0.0;
        }
        else {
            n = this.totalKills / this.totalDeath;
        }
        return n;
    }
    
    public void setHighestKillStreak(final int highestKillStreak) {
        this.highestKillStreak = highestKillStreak;
    }
    
    public void addTotalDeath() {
        ++this.totalDeath;
    }
    
    public void addTotalKill() {
        ++this.totalKills;
    }
    
    public UHCPlayer(final boolean b, final UUID uuid) {
        this.kills = 0;
        this.diamondsMinedGame = 0;
        this.spawnersMined = 0;
        this.playerAlive = false;
        this.playerDied = false;
        this.spectator = false;
        this.teamChatToggled = false;
        this.usedCommand = false;
        this.statsInventory = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.GREEN + "Stats:");
        this.totalKills = 0;
        this.totalDeath = 0;
        this.highestKillStreak = 0;
        this.wins = 0;
        this.arrowsShot = 0;
        this.arrowsHit = 0;
        this.goldenApplesEaten = 0;
        this.goldenHeadsEaten = 0;
        this.heartsHealed = 0;
        this.zombiesKilled = 0;
        this.creepersKilled = 0;
        this.skeletonsKilled = 0;
        this.caveSpidersKilled = 0;
        this.spidersKilled = 0;
        this.blazesKilled = 0;
        this.ghastsKilled = 0;
        this.cowsKilled = 0;
        this.pigsKilled = 0;
        this.chickensKilled = 0;
        this.horsesKilled = 0;
        this.witchesKilled = 0;
        this.netherEntrances = 0;
        this.horsesTamed = 0;
        this.xpLevelsEarned = 0;
        this.totalDiamondsMined = 0;
        this.totalGoldMined = 0;
        this.totalRedstoneMined = 0;
        this.totalLapisMined = 0;
        this.totalIronMined = 0;
        this.totalCoalMined = 0;
        this.totalQuartzMined = 0;
        this.totalSpawnersMined = 0;
        this.uuid = uuid;
        if (b) {
            this.loadData();
        }
    }
    
    public String getName() {
        return Bukkit.getServer().getOfflinePlayer(this.uuid).getName();
    }
    
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }
    
    public void setRespawnLocation(final Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }
    
    public ItemStack[] lastInventory() {
        return this.items;
    }
    
    public ItemStack[] lastArmour() {
        return this.armour;
    }
    
    public void setLastInventory(final ItemStack[] items) {
        this.items = items;
    }
    
    public void setLastArmour(final ItemStack[] armour) {
        this.armour = armour;
    }
    
    public int getDiamondsMined() {
        return this.diamondsMinedGame;
    }
    
    public void addDiamond() {
        ++this.diamondsMinedGame;
    }
    
    public void addKill() {
        ++this.kills;
    }
    
    public int getKills() {
        return this.kills;
    }
    
    public void setPlayerAlive(final boolean playerAlive) {
        this.playerAlive = playerAlive;
    }
    
    public boolean isPlayerAlive() {
        return this.playerAlive;
    }
    
    public boolean didPlayerDie() {
        return this.playerDied;
    }
    
    public void setDied(final boolean playerDied) {
        this.playerDied = playerDied;
    }
    
    public boolean isSpectating() {
        return this.spectator;
    }
    
    void setSpec(final boolean spectator) {
        this.spectator = spectator;
    }
    
    private void loadData() {
        if (this.hasData()) {
            new BukkitRunnable() {
                public void run() {
                    try {
                        final PreparedStatement prepareStatement = UHC.getInstance().getSQL().getConnection().prepareStatement("SELECT * FROM `" + UHC.getInstance().getSQL().table + "` WHERE `uuid` = ?;");
                        prepareStatement.setString(1, UHCPlayer.this.uuid.toString());
                        prepareStatement.executeQuery();
                        final ResultSet resultSet = prepareStatement.getResultSet();
                        if (resultSet.isBeforeFirst()) {
                            while (resultSet.next()) {
                                UHCPlayer.this.wins = resultSet.getInt("wins");
                                UHCPlayer.this.totalKills = resultSet.getInt("kills");
                                UHCPlayer.this.totalDeath = resultSet.getInt("deaths");
                                UHCPlayer.this.highestKillStreak = resultSet.getInt("higheststreak");
                                UHCPlayer.this.arrowsShot = resultSet.getInt("arrowsshot");
                                UHCPlayer.this.arrowsHit = resultSet.getInt("arrowshit");
                                UHCPlayer.this.goldenApplesEaten = resultSet.getInt("goldenappleseaten");
                                UHCPlayer.this.goldenHeadsEaten = resultSet.getInt("goldenheadseaten");
                                UHCPlayer.this.heartsHealed = resultSet.getInt("heartshealed");
                                UHCPlayer.this.zombiesKilled = resultSet.getInt("zombieskilled");
                                UHCPlayer.this.creepersKilled = resultSet.getInt("creeperskilled");
                                UHCPlayer.this.skeletonsKilled = resultSet.getInt("skeletonskilled");
                                UHCPlayer.this.caveSpidersKilled = resultSet.getInt("cavespiderskilled");
                                UHCPlayer.this.spidersKilled = resultSet.getInt("spiderskilled");
                                UHCPlayer.this.blazesKilled = resultSet.getInt("blazeskilled");
                                UHCPlayer.this.ghastsKilled = resultSet.getInt("ghastskilled");
                                UHCPlayer.this.cowsKilled = resultSet.getInt("cowskilled");
                                UHCPlayer.this.pigsKilled = resultSet.getInt("pigskilled");
                                UHCPlayer.this.chickensKilled = resultSet.getInt("chickenskilled");
                                UHCPlayer.this.horsesKilled = resultSet.getInt("horseskilled");
                                UHCPlayer.this.witchesKilled = resultSet.getInt("witcheskilled");
                                UHCPlayer.this.netherEntrances = resultSet.getInt("netherentrances");
                                UHCPlayer.this.horsesTamed = resultSet.getInt("horsestamed");
                                UHCPlayer.this.xpLevelsEarned = resultSet.getInt("xplevelsearned");
                                UHCPlayer.this.totalDiamondsMined = resultSet.getInt("diamondsmined");
                                UHCPlayer.this.totalGoldMined = resultSet.getInt("goldmined");
                                UHCPlayer.this.totalRedstoneMined = resultSet.getInt("redstonemined");
                                UHCPlayer.this.totalLapisMined = resultSet.getInt("lapismined");
                                UHCPlayer.this.totalIronMined = resultSet.getInt("ironmined");
                                UHCPlayer.this.totalCoalMined = resultSet.getInt("coalmined");
                                UHCPlayer.this.totalQuartzMined = resultSet.getInt("quartzmined");
                                UHCPlayer.this.totalSpawnersMined = resultSet.getInt("spawnersmined");
                            }
                        }
                        resultSet.close();
                        prepareStatement.close();
                    }
                    catch (SQLException ex) {
                        System.out.print(ex.getMessage());
                    }
                }
            }.runTaskAsynchronously((Plugin) UHC.getInstance());
        }
        else {
            System.out.println("Could not find data for UHCPlayer: " + this.getUuid());
        }
    }
    
    public void createData() {
        new BukkitRunnable() {
            public void run() {
                try {
                    final PreparedStatement prepareStatement = UHC.getInstance().getSQL().getConnection().prepareStatement("INSERT INTO `" + UHC.getInstance().getSQL().table + "` (uuid, wins, kills, deaths, kd, higheststreak, arrowsshot, arrowshit, goldenappleseaten, goldenheadseaten, heartshealed, zombieskilled, creeperskilled, skeletonskilled, cavespiderskilled, spiderskilled, blazeskilled, ghastskilled, cowskilled, pigskilled, chickenskilled, horseskilled, witcheskilled, netherentrances, horsestamed, xplevelsearned, diamondsmined, goldmined, redstonemined, lapismined, ironmined, coalmined, quartzmined, spawnersmined) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                    prepareStatement.setString(1, UHCPlayer.this.uuid.toString());
                    prepareStatement.setInt(2, UHCPlayer.this.wins);
                    prepareStatement.setInt(3, UHCPlayer.this.totalKills);
                    prepareStatement.setInt(4, UHCPlayer.this.totalDeath);
                    prepareStatement.setDouble(5, UHCPlayer.this.getKd());
                    prepareStatement.setInt(6, UHCPlayer.this.highestKillStreak);
                    prepareStatement.setInt(7, UHCPlayer.this.arrowsShot);
                    prepareStatement.setInt(8, UHCPlayer.this.arrowsHit);
                    prepareStatement.setInt(9, UHCPlayer.this.goldenApplesEaten);
                    prepareStatement.setInt(10, UHCPlayer.this.goldenHeadsEaten);
                    prepareStatement.setInt(11, UHCPlayer.this.heartsHealed);
                    prepareStatement.setInt(12, UHCPlayer.this.zombiesKilled);
                    prepareStatement.setInt(13, UHCPlayer.this.creepersKilled);
                    prepareStatement.setInt(14, UHCPlayer.this.skeletonsKilled);
                    prepareStatement.setInt(15, UHCPlayer.this.caveSpidersKilled);
                    prepareStatement.setInt(16, UHCPlayer.this.spidersKilled);
                    prepareStatement.setInt(17, UHCPlayer.this.blazesKilled);
                    prepareStatement.setInt(18, UHCPlayer.this.ghastsKilled);
                    prepareStatement.setInt(19, UHCPlayer.this.cowsKilled);
                    prepareStatement.setInt(20, UHCPlayer.this.pigsKilled);
                    prepareStatement.setInt(21, UHCPlayer.this.chickensKilled);
                    prepareStatement.setInt(22, UHCPlayer.this.horsesKilled);
                    prepareStatement.setInt(23, UHCPlayer.this.witchesKilled);
                    prepareStatement.setInt(24, UHCPlayer.this.netherEntrances);
                    prepareStatement.setInt(25, UHCPlayer.this.horsesTamed);
                    prepareStatement.setInt(26, UHCPlayer.this.xpLevelsEarned);
                    prepareStatement.setInt(27, UHCPlayer.this.totalDiamondsMined);
                    prepareStatement.setInt(28, UHCPlayer.this.totalGoldMined);
                    prepareStatement.setInt(29, UHCPlayer.this.totalRedstoneMined);
                    prepareStatement.setInt(30, UHCPlayer.this.totalLapisMined);
                    prepareStatement.setInt(31, UHCPlayer.this.totalIronMined);
                    prepareStatement.setInt(32, UHCPlayer.this.totalCoalMined);
                    prepareStatement.setInt(33, UHCPlayer.this.totalQuartzMined);
                    prepareStatement.setInt(34, UHCPlayer.this.totalSpawnersMined);
                    prepareStatement.executeUpdate();
                }
                catch (SQLException ex) {}
            }
        }.runTaskAsynchronously((Plugin) UHC.getInstance());
    }
    
    public void saveData() {
        if (this.hasData()) {
            new BukkitRunnable() {
                public void run() {
                    PreparedStatement prepareStatement = null;
                    try {
                        prepareStatement = UHC.getInstance().getSQL().getConnection().prepareStatement("UPDATE `" + UHC.getInstance().getSQL().table + "` SET `wins` = ?, `kills` = ?, `deaths` = ?, `kd` = ?, `higheststreak` = ?, `arrowsshot` = ?, `arrowshit` = ?,`goldenappleseaten` = ?, `goldenheadseaten` = ?, `heartshealed` = ?, `zombieskilled` = ?, `creeperskilled` = ?, `skeletonskilled` = ?, `cavespiderskilled` = ?, `spiderskilled` = ?, `blazeskilled` = ?, `ghastskilled` = ?, `cowskilled` = ?, `pigskilled` = ?, `chickenskilled` = ?, `horseskilled` = ?, `witcheskilled` = ?, `netherentrances` = ?, `horsestamed` = ?, `xplevelsearned` = ?, `diamondsmined` = ?, `goldmined` = ?, `redstonemined` = ?, `lapismined` = ?, `ironmined` = ?, `coalmined` = ?, `quartzmined` = ?, `spawnersmined` = ? WHERE `uuid` = ?;");
                        prepareStatement.setInt(1, UHCPlayer.this.wins);
                        prepareStatement.setInt(2, UHCPlayer.this.totalKills);
                        prepareStatement.setInt(3, UHCPlayer.this.totalDeath);
                        prepareStatement.setDouble(4, UHCPlayer.this.getKd());
                        prepareStatement.setInt(5, UHCPlayer.this.highestKillStreak);
                        prepareStatement.setInt(6, UHCPlayer.this.arrowsShot);
                        prepareStatement.setInt(7, UHCPlayer.this.arrowsHit);
                        prepareStatement.setInt(8, UHCPlayer.this.goldenApplesEaten);
                        prepareStatement.setInt(9, UHCPlayer.this.goldenHeadsEaten);
                        prepareStatement.setInt(10, UHCPlayer.this.heartsHealed);
                        prepareStatement.setInt(11, UHCPlayer.this.zombiesKilled);
                        prepareStatement.setInt(12, UHCPlayer.this.creepersKilled);
                        prepareStatement.setInt(13, UHCPlayer.this.skeletonsKilled);
                        prepareStatement.setInt(14, UHCPlayer.this.caveSpidersKilled);
                        prepareStatement.setInt(15, UHCPlayer.this.spidersKilled);
                        prepareStatement.setInt(16, UHCPlayer.this.blazesKilled);
                        prepareStatement.setInt(17, UHCPlayer.this.ghastsKilled);
                        prepareStatement.setInt(18, UHCPlayer.this.cowsKilled);
                        prepareStatement.setInt(19, UHCPlayer.this.pigsKilled);
                        prepareStatement.setInt(20, UHCPlayer.this.chickensKilled);
                        prepareStatement.setInt(21, UHCPlayer.this.horsesKilled);
                        prepareStatement.setInt(22, UHCPlayer.this.witchesKilled);
                        prepareStatement.setInt(23, UHCPlayer.this.netherEntrances);
                        prepareStatement.setInt(24, UHCPlayer.this.horsesTamed);
                        prepareStatement.setInt(25, UHCPlayer.this.xpLevelsEarned);
                        prepareStatement.setInt(26, UHCPlayer.this.totalDiamondsMined);
                        prepareStatement.setInt(27, UHCPlayer.this.totalGoldMined);
                        prepareStatement.setInt(28, UHCPlayer.this.totalRedstoneMined);
                        prepareStatement.setInt(29, UHCPlayer.this.totalLapisMined);
                        prepareStatement.setInt(30, UHCPlayer.this.totalIronMined);
                        prepareStatement.setInt(31, UHCPlayer.this.totalCoalMined);
                        prepareStatement.setInt(32, UHCPlayer.this.totalQuartzMined);
                        prepareStatement.setInt(33, UHCPlayer.this.totalSpawnersMined);
                        prepareStatement.setString(34, UHCPlayer.this.uuid.toString());
                        prepareStatement.executeUpdate();
                    }
                    catch (SQLException ex) {
                        ex.printStackTrace();
                        try {
                            assert prepareStatement != null;
                            prepareStatement.close();
                        }
                        catch (SQLException ex2) {
                            ex2.printStackTrace();
                        }
                    }
                    finally {
                        try {
                            assert prepareStatement != null;
                            prepareStatement.close();
                        }
                        catch (SQLException ex3) {
                            ex3.printStackTrace();
                        }
                    }
                }
            }.runTaskAsynchronously((Plugin) UHC.getInstance());
        }
        else {
            this.createData();
        }
    }
    
    public boolean hasData() {
        PreparedStatement prepareStatement = null;
        try {
            prepareStatement = UHC.getInstance().getSQL().getConnection().prepareStatement("SELECT `kills` FROM `" + UHC.getInstance().getSQL().table + "` WHERE `uuid` = ?;");
            prepareStatement.setString(1, this.uuid.toString());
            prepareStatement.executeQuery();
            if (prepareStatement.getResultSet().next()) {
                return true;
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            try {
                assert prepareStatement != null;
                prepareStatement.close();
            }
            catch (SQLException ex2) {
                System.out.print(ex2.getMessage());
            }
        }
        finally {
            try {
                assert prepareStatement != null;
                prepareStatement.close();
            }
            catch (SQLException ex3) {
                System.out.print(ex3.getMessage());
            }
        }
        return false;
    }
    
    public Inventory getStatsInventory() {
        final ChatColor mainColor = GameManager.getGameManager().getMainColor();
        final ChatColor secondaryColor = GameManager.getGameManager().getSecondaryColor();
        this.statsInventory.clear();
        this.statsInventory.setItem(0, this.buildItem(Material.DIAMOND, mainColor + "Wins: " + secondaryColor + this.wins, null, 0));
        this.statsInventory.setItem(1, this.buildItem(Material.DIAMOND_SWORD, mainColor + "Kills: " + secondaryColor + this.totalKills, null, 0));
        this.statsInventory.setItem(2, this.buildItem(Material.SKULL_ITEM, mainColor + "Deaths: " + secondaryColor + this.totalDeath, null, 0));
        this.statsInventory.setItem(3, this.buildItem(Material.BLAZE_ROD, mainColor + "KD: " + secondaryColor + this.getKd(), null, 0));
        this.statsInventory.setItem(4, this.buildItem(Material.DIAMOND_AXE, mainColor + "Highest Kill Streak: " + secondaryColor + this.highestKillStreak, null, 0));
        final ArrayList<String> list = new ArrayList<String>();
        list.add(mainColor + "Arrows Shot: " + secondaryColor + this.arrowsShot);
        list.add(mainColor + "Arrows Hit: " + secondaryColor + this.arrowsHit);
        this.statsInventory.setItem(5, this.buildItem(Material.BOW, "§aBow Statistics", list, 0));
        list.clear();
        list.add(mainColor + "Golden Apples Eaten: " + secondaryColor + this.goldenApplesEaten);
        list.add(mainColor + "Golden Heads Eaten: " + secondaryColor + this.goldenHeadsEaten);
        this.statsInventory.setItem(6, this.buildItem(Material.GOLDEN_APPLE, "§aItems Consumed", list, 0));
        this.statsInventory.setItem(7, this.buildItem(Material.POTION, mainColor + "Hearts Healed: " + secondaryColor + this.heartsHealed, null, 3738261));
        this.statsInventory.setItem(8, this.buildItem(Material.EXP_BOTTLE, mainColor + "XP Levels Earned: " + secondaryColor + this.xpLevelsEarned, null, 0));
        this.statsInventory.setItem(9, this.buildItem(Material.SADDLE, mainColor + "Horses Tamed: " + secondaryColor + this.horsesTamed, null, 0));
        list.clear();
        list.add(mainColor + "Zombies Killed: " + secondaryColor + this.zombiesKilled);
        list.add(mainColor + "Creepers Killed: " + secondaryColor + this.creepersKilled);
        list.add(mainColor + "Skeletons Killed: " + secondaryColor + this.skeletonsKilled);
        list.add(mainColor + "Cave Spiders Killed: " + secondaryColor + this.caveSpidersKilled);
        list.add(mainColor + "Spiders Killed: " + mainColor + this.spidersKilled);
        list.add(mainColor + "Blazes Killed: " + secondaryColor + this.blazesKilled);
        list.add(mainColor + "Ghasts Killed: " + secondaryColor + this.ghastsKilled);
        list.add(mainColor + "Witches Killed: " + secondaryColor + this.witchesKilled);
        list.add(mainColor + "Cows Killed: " + secondaryColor + this.cowsKilled);
        list.add(mainColor + "Pigs Killed: " + secondaryColor + this.pigsKilled);
        list.add(mainColor + "Chickens Killed: " + secondaryColor + this.chickensKilled);
        list.add(mainColor + "Horses Killed: " + secondaryColor + this.horsesKilled);
        this.statsInventory.setItem(10, this.buildItem(Material.MONSTER_EGG, "§aMobs Killed", list, 54));
        list.clear();
        list.add(mainColor + "Diamonds Mined: " + secondaryColor + this.totalDiamondsMined);
        list.add(mainColor + "Gold Mined: " + secondaryColor + this.totalGoldMined);
        list.add(mainColor + "Iron Mined: " + secondaryColor + this.totalIronMined);
        list.add(mainColor + "Coal Mined: " + secondaryColor + this.totalCoalMined);
        list.add(mainColor + "Redstone Mined: " + secondaryColor + this.totalRedstoneMined);
        list.add(mainColor + "Lapis Mined: " + secondaryColor + this.totalLapisMined);
        list.add(mainColor + "Quartz Mined: " + secondaryColor + this.totalQuartzMined);
        list.add(mainColor + "Spawners Mined: " + secondaryColor + this.totalSpawnersMined);
        this.statsInventory.setItem(11, this.buildItem(Material.DIAMOND_ORE, "§aOres Mined", list, 0));
        this.statsInventory.setItem(12, this.buildItem(Material.NETHER_BRICK, "§aNether Entrances: " + secondaryColor + this.netherEntrances, null, 0));
        return this.statsInventory;
    }
}
