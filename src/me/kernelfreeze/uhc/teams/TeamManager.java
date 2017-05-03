package me.kernelfreeze.uhc.teams;

import me.kernelfreeze.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class TeamManager implements Listener {
    private static TeamManager instance;
    private boolean teamsEnabled;
    private boolean damageTeamMembers;
    public static final Map<UUID, Team> teams;
    private int maxSize;
    private int currentTeams;
    private final String prefix;

    public TeamManager() {
        this.teamsEnabled = false;
        this.damageTeamMembers = true;
        this.maxSize = 2;
        this.currentTeams = 0;
        this.prefix = ChatColor.translateAlternateColorCodes('&', UHC.getInstance().getConfig().getString("settings.teams-prefix"));
    }

    public static TeamManager getInstance() {
        return TeamManager.instance;
    }

    public boolean canDamageTeamMembers() {
        return this.damageTeamMembers;
    }

    public void setCanDamageTeamMembers(final boolean damageTeamMembers) {
        this.damageTeamMembers = damageTeamMembers;
    }

    public boolean isTeamsEnabled() {
        return this.teamsEnabled;
    }

    public void setTeamsEnabled(final boolean teamsEnabled) {
        this.teamsEnabled = teamsEnabled;
    }

    public void clearTeams() {
        TeamManager.teams.clear();
        final Iterator<UUID> iterator = TeamManager.teams.keySet().iterator();
        while (iterator.hasNext()) {
            this.unregisterTeam(iterator.next());
        }
        this.currentTeams = 0;
    }

    public void unregisterTeam(final UUID uuid) {
        TeamManager.teams.get(uuid).removePlayer(Bukkit.getOfflinePlayer(uuid));
        TeamManager.teams.remove(uuid);
    }

    public Map<UUID, Team> getTeams() {
        return TeamManager.teams;
    }

    public void autoPlace() {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (TeamManager.teams.get(player.getUniqueId()) == null) {
                this.createTeam(player);
            }
        }
    }

    public void createTeam(final Player player) {
        ++this.currentTeams;
        final Team team = TeamManager.teams.get(player.getUniqueId());
        if (team != null) {
            team.removePlayer((OfflinePlayer) player);
        }
        this.registerTeam(player, new Team(player, this.currentTeams));
    }

    public void registerTeam(final Player player, final Team team) {
        if (team.getSize() == this.getMaxSize()) {
            player.sendMessage("§cTeam size cannot be bigger than" + this.maxSize);
            return;
        }
        team.addPlayer(player);
        TeamManager.teams.put(player.getUniqueId(), team);
    }

    public void disbandTeam(final Team team) {
        final Iterator<UUID> iterator = team.getPlayers().iterator();
        while (iterator.hasNext()) {
            this.unregisterTeam(iterator.next());
        }
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }

    public Set<Team> getTeamSet() {
        final HashSet<Team> set = new HashSet<>();
        set.addAll(TeamManager.teams.values());
        return set;
    }

    public int getTeamsAlive() {
        int n = 0;
        final Iterator<Team> iterator = this.getTeamSet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isAlive()) {
                ++n;
            }
        }
        return n;
    }

    public String getTeamsPrefix() {
        return this.prefix;
    }

    public Team getLastTeam() {
        if (this.getTeamsAlive() == 1) {
            for (final Team team : this.getTeamSet()) {
                if (team.isAlive()) {
                    return team;
                }
            }
        }
        return null;
    }

    public Team getTeam(final OfflinePlayer offlinePlayer) {
        return getInstance().getTeams().get(offlinePlayer.getUniqueId());
    }

    static {
        TeamManager.instance = new TeamManager();
        teams = new HashMap<UUID, Team>();
    }
}
