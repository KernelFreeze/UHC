package me.kernelfreeze.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class WlCMD implements CommandExecutor {
    static HashSet<UUID> whitelisted = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("uhc.addfriend")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (whitelisted.contains(p.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "You already used the whitelist.");
            } else {
                if (args.length > 0) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + args[0]);
                    whitelisted.add(p.getUniqueId());
                    sender.sendMessage(ChatColor.GREEN + "Done!");
                } else {
                    sender.sendMessage(ChatColor.GREEN + "Usage: /wl [user]");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You should use /whitelist...");
        }
        return true;
    }
}
