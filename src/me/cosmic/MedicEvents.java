package me.cosmic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class MedicEvents implements Listener {

    private String c(String a) {
        return ChatColor.translateAlternateColorCodes('&',a);
    }
    private static HashMap<Player, String> current = new HashMap<>();

    private static me.cosmic.MedicEvents g = new me.cosmic.MedicEvents();
    public static me.cosmic.MedicEvents get() { return g; }

    public void openInventory(Player p) {

        Inventory medic = Bukkit.createInventory(null,9, ChatColor.AQUA + "Medic GUI");

        ItemStack glasspane = new ItemStack(Material.THIN_GLASS);
        ItemMeta gp = glasspane.getItemMeta();
        glasspane.setItemMeta(gp);
        medic.setItem(4, glasspane);

        ItemStack dsword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta ds = dsword.getItemMeta();
        ds.setDisplayName("Heal 25%");
        dsword.setItemMeta(ds);
        medic.setItem(0, dsword);

        ItemStack isword = new ItemStack(Material.IRON_SWORD);
        ItemMeta is = isword.getItemMeta();
        is.setDisplayName("Heal 50%");
        isword.setItemMeta(is);
        medic.setItem(1, isword);

        ItemStack gsword = new ItemStack(Material.GOLD_SWORD);
        ItemMeta gs = gsword.getItemMeta();
        gs.setDisplayName("Heal 75%");
        gsword.setItemMeta(gs);
        medic.setItem(2, gsword);

        ItemStack ssword = new ItemStack(Material.STONE_SWORD);
        ItemMeta ss = ssword.getItemMeta();
        ss.setDisplayName("Heal 100%");
        ssword.setItemMeta(ss);
        medic.setItem(3, ssword);

        p.openInventory(medic);
        current.put(p, "first");
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.isLeftClick()) {
            Player p = (Player) e.getWhoClicked();
            int slot = e.getRawSlot();


            if (current.containsKey(p) && current.get(p).equals("first")) {
                e.setCancelled(true);
                if (slot == 5) {
                    p.sendMessage(c(ChatColor.RED + "&8[&4Essentials&bGUI&8]&e Please click a Heal you want!"));
                    p.closeInventory();
                }
                if (slot == 0) {
                    p.performCommand("heal25");
                    p.closeInventory();
                }
                if (slot == 7) {
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Please click a Heal you want!"));
                    p.closeInventory();
                }
                if (slot == 1) {
                    p.performCommand("heal50");
                    p.closeInventory();
                }
                if (slot == 4) {
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Please click a Heal you want!"));
                    p.closeInventory();
                }
                if (slot == 2) {
                    p.performCommand("heal75");                    p.closeInventory();
                }
                if (slot == 6) {
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Please click a Heal you want!"));
                    p.closeInventory();
                }
                if (slot == 3) {
                    p.performCommand("heal100");                    p.closeInventory();
                }
                if (slot == 8) {
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Please click a Heal you want!"));
                    p.closeInventory();
                }
            }
        }
    }

}
