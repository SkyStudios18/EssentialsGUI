package me.cosmic;

import com.google.common.collect.Lists;
import me.cosmic.playermanagement.ManagementGUI;
import me.cosmic.playermanagement.PlayersGUI;
import me.cosmic.teams.Team;
import me.cosmic.teams.TeamCommands;
import me.cosmic.teams.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin implements Listener{

    private static Main instance;
    public static Main get() { return instance; }

    private List<Team> teams = Lists.newArrayList();
    public List<Team> getTeams() { return teams; }
    public void addTeam(Team team) { teams.add(team); }
    public void removeTeam(Team team) { teams.remove(team); }

    @Override
    public void onEnable() {
        instance=this;
        register(new GamemodeEvents());
        register(new MedicEvents());
        register("gamemodes", new me.cosmic.Commands());
        register("medicgui", new me.cosmic.Commands());
        register("gmc", new me.cosmic.Commands());
        register("gms", new me.cosmic.Commands());
        register("gmsp", new me.cosmic.Commands());
        register("gma", new me.cosmic.Commands());
        register("heal25", new me.cosmic.Commands());
        register("heal50", new me.cosmic.Commands());
        register("heal75", new me.cosmic.Commands());
        register("heal100", new me.cosmic.Commands());
        register( this);

        register("team", new TeamCommands());
        register("unteam", new TeamCommands());
        register(new TeamHandler());

        register(new PlayersGUI());
        register(new ManagementGUI());

        this.getServer().getLogger().info("EssentialsGUI Enabled!");
    }
    public void register(Listener l) {
        Bukkit.getPluginManager().registerEvents(l,this);
    }
    public void register(String n, CommandExecutor ce) {
        getCommand(n).setExecutor(ce);
    }

    @Override
    public void onDisable() {
        this.getServer().getLogger().info("EssentialsGUI Disabled!");
    }
}
