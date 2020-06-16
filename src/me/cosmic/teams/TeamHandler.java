package me.cosmic.teams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamHandler implements Listener {


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
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
