package me.cosmic.teams;

import com.google.common.collect.Lists;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;

public class TeamHandler implements Listener {

    private static List<UUID> inTeamChat = Lists.newArrayList();


    public static void setTeamChat(Player player, boolean bool) {
        if (Team.getTeam(player) == null) return;

        if (bool) { inTeamChat.add(player.getUniqueId()); }
        else {
            inTeamChat.remove(player.getUniqueId());
        }
    }

    public static boolean isInTeamChat(Player player) {
        return inTeamChat.contains(player.getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (isInTeamChat(player)) {
            Team team = Team.getTeam(player);
            if (team == null) { setTeamChat(player, false); return; }

            e.setCancelled(true);

            if (e.getMessage().startsWith("!")) {
                // talk in normal chat
                e.setMessage(e.getMessage().replaceFirst("!", ""));
                e.setCancelled(false);
                return;
            }

            team.sendMessage("&8[&bTeam&8] &a" + player.getName() + "&7:&e " + e.getMessage());
        }
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player damager = ((Player)arrow.getShooter());
                Player damaged = (Player) e.getEntity();

                Team team = Team.getTeam(damaged);
                if (team != null && team.containsPlayer(damager)) {
                    e.setCancelled(true);
                }
            }
        }
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            // both damagers are players
            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();

            Team team = Team.getTeam(damaged);
            if (team != null && team.containsPlayer(damager)) {
                e.setCancelled(true);
            }
        }
    }



}
