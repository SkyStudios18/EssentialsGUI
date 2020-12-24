package me.cosmic.teams;

import com.google.common.collect.Lists;
import me.cosmic.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Team {

    private final int max_players = 8;

    private UUID owner; // the owner of the team
    private List<UUID> members = Lists.newArrayList(); // the members of the team
    private List<UUID> invitedPlayers = Lists.newArrayList(); // the players that are invited to join the team


    public Team(UUID owner, List<UUID> members) {
        this.owner = owner;
        this.members = members;
    }

    public static Team getTeam(Player player) {
        for (Team team : Main.get().getTeams()) {
            if (team.getOwner().equals(player.getUniqueId()) || team.getMembers().contains(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }


    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public List<UUID> getInvitedPlayers() {
        return invitedPlayers;
    }

    public void invitePlayer(Player player) {
        Player own = Bukkit.getPlayer(getOwner());
        if (own != null) {
            if (isInvited(player)) {
                own.sendMessage(ChatColor.RED + "You already invited that player!");
                return;
            }
            own.sendMessage(ChatColor.GREEN + "You invited " + player.getName() + " to join your team!");
        }
        else {
            return;
        }
        invitedPlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.GRAY + "You have been invited to join " + own.getName() +"'s team.");
        player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/team accept " + own.getName() + ChatColor.GRAY + " to accept.");
    }

    public TeamInviteResponse acceptPlayer(Player player) {
        if (invitedPlayers.contains(player.getUniqueId())) {
            Team cur = Team.getTeam(player);
            if (cur != null) {
                if (!(cur.getOwner().equals(player.getUniqueId()) && cur.getMembers().isEmpty())) {
                    return TeamInviteResponse.ALREADY_IN_PARTY;
                }
                else {
                    cur.disbandTeam();
                }
            }

            if (members.size()+1 >= 8) {
                return TeamInviteResponse.MAX_PLAYERS;
            }

            player.sendMessage(ChatColor.GREEN + "You accepted " + Bukkit.getPlayer(getOwner()).getName() + "'s team invite!");
            sendMessage("&8[&a+&8] &e" + player.getName() + " has joined your team!");

            invitedPlayers.remove(player.getUniqueId());
            members.add(player.getUniqueId());

            return TeamInviteResponse.ACCEPTED;
        }
        return TeamInviteResponse.NOT_INVITED;
    }

    public void disbandTeam() {
        Player owner = Bukkit.getPlayer(this.owner);
        if (owner != null && owner.isOnline()) {
            sendMessage(ChatColor.RED + "Your party is now disbanded.");
        }
        this.owner = null;
        this.members = null;
        Main.get().removeTeam(this);
    }

    public void removePlayer(Player player) {
        if (owner.equals(player.getUniqueId())) {
            if (members.size() <= 1) {
                this.owner = null;
                sendMessage("&8[&c-&8] &e" + player.getName() + " left the team!");
                disbandTeam();
            }
            else {
                UUID newOwner = getMembers().get(new Random().nextInt(members.size()));
                getMembers().remove(newOwner);
                this.owner = newOwner;
                sendMessage("&b" + Bukkit.getPlayer(newOwner).getName() + " &eis the new owner of the party!");
            }
        }
        else {
            members.remove(player.getUniqueId());
            sendMessage("&8[&c-&8] &e" + player.getName() + " left the team!");
            if (members.isEmpty()) {
                disbandTeam();
            }
        }
    }
    public void removeInvitedPlayer(Player player) {
        invitedPlayers.remove(player.getUniqueId());
    }
    public boolean isInvited(Player player) {
        return invitedPlayers.contains(player.getUniqueId());
    }

    public boolean containsPlayer(Player player) {
        return owner.equals(player.getUniqueId()) || members.contains(player.getUniqueId());
    }

    public void sendMessage(String message) {
        List<UUID> uids = Lists.newArrayList(getMembers());
        uids.add(getOwner());
        for (UUID uid : uids) {
            Player p = Bukkit.getPlayer(uid);
            if (p != null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

}
