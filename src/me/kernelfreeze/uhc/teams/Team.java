package me.kernelfreeze.uhc.teams;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.inventory.*;
import me.kernelfreeze.uhc.player.*;
import java.util.*;
import java.text.*;
import org.bukkit.*;

public class Team
{
    private final Set<UUID> players;
    private Player owner;
    private int id;
    private int kills;
    private Inventory backPack;
    private Location scatterLocation;
    
    protected Team(final Player owner, final int id) {
        this.players = new HashSet<UUID>();
        this.kills = 0;
        this.scatterLocation = GameManager.getGameManager().getScatterLocation();
        this.owner = owner;
        this.id = id;
        this.backPack = Bukkit.createInventory((InventoryHolder)null, 27, "§aTeam" + id + " BackPack");
    }
    
    public Inventory getBackPack() {
        return this.backPack;
    }
    
    public Location getScatterLocation() {
        return this.scatterLocation;
    }
    
    public void setScatterLocation(final Location scatterLocation) {
        this.scatterLocation = scatterLocation;
    }
    
    public Set<UUID> getPlayers() {
        return this.players;
    }
    
    public Player getOwner() {
        return this.owner;
    }
    
    boolean isAlive() {
        for (final UHCPlayer uhcPlayer : PlayerManager.getPlayerManager().uhcPlayersSet(this.players)) {
            if (uhcPlayer != null && uhcPlayer.isPlayerAlive()) {
                return true;
            }
        }
        return false;
    }
    
    public int getSize() {
        return this.getPlayers().size();
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getKills() {
        return this.kills;
    }
    
    public void addKill() {
        ++this.kills;
    }
    
    @Override
    public String toString() {
        String s = TeamManager.getInstance().getTeamsPrefix().replace("s", String.valueOf(this.id)) + ":";
        for (final UUID uuid : this.players) {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer.isOnline() && offlinePlayer.getPlayer().getGameMode().equals((Object)GameMode.SURVIVAL) && offlinePlayer.getPlayer().getWorld().equals(GameManager.getGameManager().getUHCWorld())) {
                s = s + "§a " + Bukkit.getPlayer(uuid).getName() + "§e(§a" + new DecimalFormat("#.#").format(Bukkit.getPlayer(uuid).getHealth() / 2.0) + "§4 \u2665§e)§6,";
            }
            else {
                s = s + "§c " + Bukkit.getOfflinePlayer(uuid).getName() + "§6,§a";
            }
        }
        return s;
    }
    
    void removePlayer(final OfflinePlayer offlinePlayer) {
        this.players.remove(offlinePlayer.getUniqueId());
        if (offlinePlayer.isOnline()) {
            offlinePlayer.getPlayer().sendMessage("§cYou are no longer part of your team!");
        }
        this.sendMessage("§c" + offlinePlayer.getName() + " has left your team.");
    }
    
    void addPlayer(final Player player) {
        this.sendMessage("§a" + player.getName() + " has joined your team.");
        this.players.add(player.getUniqueId());
        player.sendMessage(GameManager.getGameManager().getMainColor() + "Joined team: §f" + this.id);
    }
    
    public void sendMessage(final String s) {
        final Iterator<UUID> iterator = this.getPlayers().iterator();
        while (iterator.hasNext()) {
            final Player player = Bukkit.getPlayer((UUID)iterator.next());
            if (player != null) {
                player.sendMessage(s);
            }
        }
    }
}
