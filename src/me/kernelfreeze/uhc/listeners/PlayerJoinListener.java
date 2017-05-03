package me.kernelfreeze.uhc.listeners;

import me.kernelfreeze.uhc.UHC;
import me.kernelfreeze.uhc.configs.ConfigIntegers;
import me.kernelfreeze.uhc.game.GameManager;
import me.kernelfreeze.uhc.game.LobbyScoreboard;
import me.kernelfreeze.uhc.game.ScoreboardM;
import me.kernelfreeze.uhc.player.PlayerManager;
import me.kernelfreeze.uhc.player.UHCPlayer;
import me.kernelfreeze.uhc.scenarios.ScenarioManager;
import me.kernelfreeze.uhc.teams.TeamManager;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;

public class PlayerJoinListener implements Listener
{
    private final GameManager gameManager;
    
    public PlayerJoinListener() {
        this.gameManager = GameManager.getGameManager();
    }
    
    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent playerJoinEvent) {
        final Player player = playerJoinEvent.getPlayer();
        UHCPlayer uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        this.sendJoinMessae(player);
        if (PlayerManager.getPlayerManager().getUHCPlayers().get(player.getUniqueId()) == null) {
            PlayerManager.getPlayerManager().createUHCPlayer(player.getUniqueId());
            uhcPlayer = PlayerManager.getPlayerManager().getUHCPlayer(player.getUniqueId());
        }
        if (this.gameManager.lobbyScoreboard() && !this.gameManager.isGameRunning() && !this.gameManager.isScattering()) {
            new LobbyScoreboard().newScoreboard(player);
        }
        if (GameManager.getGameManager().getSpawnWorld() == null) {
            Bukkit.getServer().broadcastMessage(ChatColor.RED + "Spawn world is not defined in config.yml, You wont be able to start the UHC!");
        }
        for (final UHCPlayer uhcPlayer2 : PlayerManager.getPlayerManager().getUHCPlayers().values()) {
            if (uhcPlayer2.isSpectating()) {
                final Player player2 = Bukkit.getServer().getPlayer(uhcPlayer2.getUuid());
                if (player2 == null) {
                    continue;
                }
                player.hidePlayer(player2);
            }
        }
        if (!uhcPlayer.isPlayerAlive()) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.getInventory().setArmorContents((ItemStack[])null);
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(this.gameManager.getSpawnLocation());
            for(PotionEffect e : player.getActivePotionEffects()){
                player.removePotionEffect(e.getType());
            }
        }
        if (this.gameManager.isGameRunning()) {
            if (TeamManager.getInstance().isTeamsEnabled() && TeamManager.getInstance().getTeam((OfflinePlayer)player) == null) {
                TeamManager.getInstance().createTeam(player);
            }
            new ScoreboardM().newScoreboard(player);
            if (!this.gameManager.isPvpEnabled() && !uhcPlayer.isPlayerAlive() && player.hasPermission("uhc.joinlate") && !uhcPlayer.didPlayerDie()) {
                this.gameManager.scatterPlayer(player);
            }
            else if (this.gameManager.isPvpEnabled() && player.hasPermission("uhc.spectate") && (!uhcPlayer.isPlayerAlive() || uhcPlayer.didPlayerDie())) {
                PlayerManager.getPlayerManager().setSpectating(true, player);
            }
            else if (player.hasPermission("uhc.spectate") && uhcPlayer.didPlayerDie() && !uhcPlayer.isPlayerAlive()) {
                PlayerManager.getPlayerManager().setSpectating(true, player);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLoginEvent(final PlayerLoginEvent playerLoginEvent) {
        final Player player = playerLoginEvent.getPlayer();
        if (this.gameManager.isScattering()) {
            playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§fThe scatter is currently running. Please wait until the scatter finishes!");
            return;
        }
        if (this.gameManager.isGameRunning() && !player.hasPermission("uhc.spectate") && !player.isWhitelisted() && !player.hasPermission("uhc.joinlate")) {
            playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§fThe game has already started!");
            return;
        }
        if (Bukkit.getServer().hasWhitelist() && !player.isWhitelisted() && !player.hasPermission("uhc.whitelist.bypass")) {
            playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "§fThe server is currently whitelisted!");
            return;
        }
        if (ConfigIntegers.MAXPLAYERS.get() <= PlayerManager.getPlayerManager().online() && !playerLoginEvent.getPlayer().hasPermission("uhc.maxplayers.bypass") && !playerLoginEvent.getPlayer().isWhitelisted()) {
            playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_FULL, "§fThe server is full!");
            return;
        }
        if (this.gameManager.isMapGenerating() && !player.hasPermission("uhc.join.on.map.generation")) {
            playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§fThe map is currently generating!");
            return;
        }
        if (playerLoginEvent.getResult().equals((Object)PlayerLoginEvent.Result.ALLOWED)) {
            playerLoginEvent.allow();
        }
    }
    
    @EventHandler
    public void onAsyncPlayerPreLoginEvent(final AsyncPlayerPreLoginEvent asyncPlayerPreLoginEvent) {
        if (asyncPlayerPreLoginEvent.getLoginResult().equals((Object)AsyncPlayerPreLoginEvent.Result.ALLOWED) && !PlayerManager.getPlayerManager().doesUHCPlayerExsists(asyncPlayerPreLoginEvent.getUniqueId())) {
            PlayerManager.getPlayerManager().createUHCPlayer(asyncPlayerPreLoginEvent.getUniqueId());
        }
    }
    
    private void sendJoinMessae(final Player player) {
        final ChatColor mainColor = this.gameManager.getMainColor();
        player.sendMessage(this.gameManager.getSecondaryColor() + "---------------");
        player.sendMessage(mainColor + "Welcome to " + this.gameManager.getName() + " version " + UHC.getInstance().getConfig().getDouble("settings.version"));
        player.sendMessage("  ");
        player.sendMessage(this.gameManager.getHostPrefix() + this.gameManager.getHostName());
        if (TeamManager.getInstance().isTeamsEnabled()) {
            player.sendMessage(mainColor + "Game: §fTeams Of " + TeamManager.getInstance().getMaxSize());
        }
        else {
            player.sendMessage(mainColor + "Game: §fFFA");
        }
        player.sendMessage(mainColor + "Scenarios: §f" + ScenarioManager.getInstance().getActiveScenarios().toString());
        player.sendMessage("  ");
        player.sendMessage(this.gameManager.getSecondaryColor() + "---------------");
    }
}
