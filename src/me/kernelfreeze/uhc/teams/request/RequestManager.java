package me.kernelfreeze.uhc.teams.request;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import me.kernelfreeze.uhc.game.*;
import me.kernelfreeze.uhc.teams.*;
import org.bukkit.*;

public class RequestManager implements Listener
{
    private static RequestManager instance;
    public Map<Player, Request> requestMap;
    
    public RequestManager() {
        this.requestMap = new HashMap<Player, Request>();
        RequestManager.instance = this;
    }
    
    public void register(final JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
    }
    
    public static RequestManager getInstance() {
        return RequestManager.instance;
    }
    
    public void sendRequest(final Player player, final Player player2, final Team team) {
        final ChatColor mainColor = GameManager.getGameManager().getMainColor();
        final ChatColor secondaryColor = GameManager.getGameManager().getSecondaryColor();
        if (!this.requestMap.containsKey(player2)) {
            if (TeamManager.getInstance().getTeam((OfflinePlayer)player2) == null) {
                this.requestMap.put(player2, new Request(team, player2));
                player2.sendMessage(mainColor + "You have received a team request from: §r" + team.toString());
                player2.sendMessage(mainColor + "Use §a/team accept " + mainColor + "or §c/team deny" + mainColor + " to respond.");
                team.sendMessage("§a" + team.getOwner().getName() + mainColor + " invited " + secondaryColor + player2.getName() + mainColor + " to the team!");
                RequestTime.requestTimer(player2, team);
            }
            else {
                player.sendMessage("§cPlayer is already in a team!");
            }
        }
        else {
            player.sendMessage("§cPlayer already has a team request!");
        }
    }
    
    public Request getRequest(final Player player) {
        return this.requestMap.get(player);
    }
    
    public void declined(final Player player) {
        this.requestMap.remove(player).decline();
    }
    
    public void timedOut(final Player player) {
        this.requestMap.remove(player);
    }
}
