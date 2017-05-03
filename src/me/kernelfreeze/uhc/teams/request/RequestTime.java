package me.kernelfreeze.uhc.teams.request;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.teams.Team;
import org.bukkit.entity.*;
import me.kernelfreeze.uhc.teams.*;
import org.bukkit.scheduler.*;
import me.kernelfreeze.uhc.*;
import org.bukkit.plugin.*;

class RequestTime
{
    static void requestTimer(final Player player, final Team team) {
        new BukkitRunnable() {
            public void run() {
                if (RequestManager.getInstance().getRequest(player) != null) {
                    RequestManager.getInstance().timedOut(player);
                    player.sendMessage("§cYour request from " + team.getOwner().getName() + " timed out!");
                    team.getOwner().sendMessage("§cTeam invite to " + player.getName() + " timed out!");
                }
            }
        }.runTaskLater((Plugin) UHC.getInstance(), 350L);
    }
}
