package me.kernelfreeze.uhc.cmds;

import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.util.ValueComparator;
import org.bukkit.*;
import me.kernelfreeze.uhc.game.*;
import org.bukkit.command.*;
import me.kernelfreeze.uhc.util.*;
import me.kernelfreeze.uhc.player.*;
import java.util.*;

public class BestKillersCMD implements CommandExecutor
{
    private final ChatColor color1;
    private final ChatColor color2;
    
    public BestKillersCMD() {
        this.color1 = GameManager.getGameManager().getMainColor();
        this.color2 = GameManager.getGameManager().getSecondaryColor();
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("bestkillers")) {
            if (!GameManager.getGameManager().isGameRunning()) {
                commandSender.sendMessage("§cA UHC is not currently running!");
                return true;
            }
            commandSender.sendMessage(this.color2 + "----------------");
            commandSender.sendMessage(this.color1 + "§nTop 10 Killers: ");
            commandSender.sendMessage(" ");
            this.getBestKillers(10, commandSender);
            commandSender.sendMessage(this.color2 + "----------------");
        }
        return false;
    }
    
    private void getBestKillers(final int n, final CommandSender commandSender) {
        final HashMap<String, Integer> hashMap = new HashMap<>();
        final TreeMap<String, Integer> treeMap = new TreeMap<>(new ValueComparator(hashMap));
        for (final UHCPlayer uhcPlayer : PlayerManager.getPlayerManager().getUHCPlayers().values()) {
            if (uhcPlayer.getKills() > 0) {
                if (uhcPlayer.isPlayerAlive()) {
                    hashMap.put(ChatColor.GREEN + uhcPlayer.getName(), uhcPlayer.getKills());
                }
                else {



                    hashMap.put(ChatColor.RED + uhcPlayer.getName(), uhcPlayer.getKills());
                }
            }
        }
        treeMap.putAll(hashMap);
        int n2 = 1;
        for (final String s : treeMap.keySet()) {
            if (n2 > n) {
                break;
            }
            commandSender.sendMessage(this.color2 + "" + n2 + ") " + s + ": " + this.color2 + hashMap.get(s));
            ++n2;
        }
    }
}
