package me.kernelfreeze.uhc.teams.request;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.teams.Team;
import me.kernelfreeze.uhc.teams.*;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.game.*;

public class Request
{
    private Team team;
    private Player recipient;
    
    public Request(final Team team, final Player recipient) {
        this.team = team;
        this.recipient = recipient;
    }
    
    void decline() {
        this.recipient.sendMessage(GameManager.getGameManager().getMainColor() + "You have denied the team invite!");
        this.team.getOwner().sendMessage("§c" + this.recipient.getName() + " denied the team invite!");
    }
    
    public Team getTeam() {
        return this.team;
    }
}
