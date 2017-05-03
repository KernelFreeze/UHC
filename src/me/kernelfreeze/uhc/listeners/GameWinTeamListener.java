package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.teams.Team;
import org.bukkit.event.*;
import me.kernelfreeze.uhc.teams.*;
import java.util.*;

public class GameWinTeamListener extends Event
{
    private static HandlerList handlers;
    private Team team;
    
    public HandlerList getHandlers() {
        return GameWinTeamListener.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return GameWinTeamListener.handlers;
    }
    
    public GameWinTeamListener(final Team team) {
        this.team = team;
    }
    
    public Team getWinTeam() {
        return this.team;
    }
    
    public Set<UUID> getUUIDs() {
        return this.team.getPlayers();
    }
    
    static {
        GameWinTeamListener.handlers = new HandlerList();
    }
}
