package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.player.UHCPlayer;
import org.bukkit.event.*;
import me.kernelfreeze.uhc.player.*;

public class GameWinListener extends Event
{
    private static HandlerList handlers;
    private String winner;
    private UHCPlayer uhcPlayer;
    
    public HandlerList getHandlers() {
        return GameWinListener.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return GameWinListener.handlers;
    }
    
    public GameWinListener(final String winner, final UHCPlayer uhcPlayer) {
        this.winner = winner;
        this.uhcPlayer = uhcPlayer;
    }
    
    public String getWinner() {
        return this.winner;
    }
    
    public UHCPlayer getUHCPlayer() {
        return this.uhcPlayer;
    }
    
    static {
        GameWinListener.handlers = new HandlerList();
    }
}
