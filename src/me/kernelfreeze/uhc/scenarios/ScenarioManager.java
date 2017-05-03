package me.kernelfreeze.uhc.scenarios;

import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;

public class ScenarioManager
{
    private static ScenarioManager instance;
    private Map<String, Scenario> scenarios;
    private final Inventory scenariosInv;
    private final Set<String> activeScenarios;
    
    public ScenarioManager() {
        this.scenarios = new HashMap<String, Scenario>();
        this.scenariosInv = Bukkit.createInventory((InventoryHolder)null, 18, "§aCurrent Scenarios:");
        this.activeScenarios = new HashSet<String>();
    }
    
    public static ScenarioManager getInstance() {
        if (ScenarioManager.instance == null) {
            ScenarioManager.instance = new ScenarioManager();
        }
        return ScenarioManager.instance;
    }
    
    public Inventory getScenariosInventory() {
        this.scenariosInv.clear();
        for (final Scenario scenario : this.getScenarios().values()) {
            if (scenario.isEnabled()) {
                this.scenariosInv.addItem(new ItemStack[] { scenario.getScenarioItemStack() });
            }
        }
        return this.scenariosInv;
    }
    
    public Map<String, Scenario> getScenarios() {
        return this.scenarios;
    }
    
    public Scenario getScenarioIgnoreCase(final String s) {
        for (final Scenario scenario : this.scenarios.values()) {
            if (scenario.getName().equalsIgnoreCase(s)) {
                return scenario;
            }
        }
        return this.scenarios.get(s);
    }
    
    public Scenario getScenarioExact(final String s) {
        return this.scenarios.get(s);
    }
    
    public Set<String> getActiveScenarios() {
        return this.activeScenarios;
    }
    
    boolean doesScenarioExists(final String s) {
        final Iterator<Scenario> iterator = this.scenarios.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return this.scenarios.containsKey(s);
    }
    
    public void newScenario(final String s, final Material material, final String... array) {
        this.scenarios.put(s, new Scenario(s, material, array));
    }
    
    public void createAllScenarios() {
        this.newScenario("CutClean", Material.IRON_INGOT, "Ores drop smelted.", "Food drop cooked.", "Flint, Leather and Feathers", "drop rates are 100%");
        this.newScenario("RiskyRetrieval", Material.ENDER_CHEST, "All the gold/diamonds you mine,", "will go to the enderchest which is placed in 0,0.");
        this.newScenario("TripleOres", Material.EMERALD, "Food and ores are tripled when mined / harvested.", "All TripleOres games are CutClean.");
        this.newScenario("BareBones", Material.BONE, "Enchantment tables/Anvils can't be crafted or used,", "Golden apples can't be crafted either.", "The Nether is disabled.", "Players drop 1 Diamond, 2 Golden apples", "32 Arrows and 2 String on death.");
        this.newScenario("TimeBomb", Material.TNT, "When player dies,", "their loot will drop into a chest.", "After 30s, the chest will explode.");
        this.newScenario("ExtraInventory", Material.CHEST, "Use - /extrainv ", "to open your extra inventory.");
        this.newScenario("NoFallDamage", Material.DIAMOND_BOOTS, "You cannot take fall damage.");
        this.newScenario("Fireless", Material.FIRE, "You cannot take fire damage.");
        this.newScenario("Soup", Material.MUSHROOM_SOUP, "Mushroom Stew heals 2 hearts.");
        this.newScenario("BackPacks", Material.CHEST, "Use - /backpack ", "to open the team inventory.");
        this.newScenario("Diamondless", Material.DIAMOND_ORE, "You cannot mine diamonds.", "Players drop 1 diamond on death.");
        this.newScenario("Goldless", Material.GOLD_ORE, "You cannot mine gold.", "Players drop 8 gold on death.");
        this.newScenario("Bowless", Material.BOW, "Bows cannot be crafted/used.");
        this.newScenario("Rodless", Material.FISHING_ROD, "Fishing rods cannot be crafted/used.");
        this.newScenario("Vanilla+", Material.FLINT, "Flint and Apple rates are up.");
        this.newScenario("Timber", Material.DIAMOND_AXE, "");
        this.newScenario("GoldenRetriever", Material.GOLDEN_APPLE, "Players drop 1 golden head on death.");
        this.newScenario("BloodDiamonds", Material.DIAMOND, "Every time you mine diamonds,", "you take 0.5 heart of damage.");
        this.newScenario("Switcheroo", Material.ENDER_PEARL, "Every time you shoot someone with a bow,", "you both switch your current locations.");
        this.newScenario("GoneFishing", Material.FISHING_ROD, "You start with 64 anvils, infinite levels ", "and a fishing rod with luck of the sea 250.");
        this.newScenario("LuckyLeaves", Material.GOLDEN_APPLE, "There is a small chance of a golden apple", "to drop from trees.");
    }
}
