package me.cosmic;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
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
