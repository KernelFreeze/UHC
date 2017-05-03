package me.kernelfreeze.uhc.listeners;

import org.bukkit.event.*;

public class GameStopEvent extends Event
{
    private static HandlerList handlers;
    private String reason;
    
    public HandlerList getHandlers() {
        return GameStopEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return GameStopEvent.handlers;
    }
    
    public GameStopEvent(final String reason) {
        this.reason = reason;
    }
    
    public String getStopReason() {
        return this.reason;
    }
    
    static {
        GameStopEvent.handlers = new HandlerList();
    }
}
