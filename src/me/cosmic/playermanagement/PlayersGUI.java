package me.cosmic.playermanagement;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;

public class PlayersGUI implements Listener {

    private static HashMap<Player, HashMap<Integer, Player>> guiPlayers = new HashMap<>();
    // we are going to put all players per slot in here for each player that is managing other players.

    @SuppressWarnings("deprecation")
    public void openMenu(Player player) {
        List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());
        int psize = players.size();

        int msize = 18;

        if (psize <= 9) {
            msize = 18;
        }
        if (psize <= 18) {
            msize = 27;
        } else if (psize <= 27) {
            msize = 36;
        } else if (psize <= 36) {
            msize = 45;
        } else if (psize <= 45) {
            msize = 54;
        }

        Inventory gui = Bukkit.createInventory(null, msize, "Player Management");
        HashMap<Integer, Player> playersInGui = new HashMap<>();

        int i = 0;

        for (Player p : players) {
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            // don't really know which one it is lol
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(p.getName());
            meta.setDisplayName(ChatColor.AQUA + p.getName());
            meta.setLore(Lists.newArrayList(
                    " ",
                    ChatColor.GRAY + "Click here to manage this",
                    ChatColor.GRAY + "player. You will be able to",
                    ChatColor.GRAY + "kick, ban and more."
            ));
            item.setItemMeta(meta);
            gui.setItem(i, item);
            playersInGui.put(i, p);
            i++;

        }

        ItemStack close = new ItemStack(Material.BOOK);
        ItemMeta cMeta = close.getItemMeta();
        cMeta.setDisplayName(ChatColor.RED + "Close the menu");
        cMeta.setLore(Lists.newArrayList(ChatColor.GRAY + "Click to close the menu"));
        close.setItemMeta(cMeta);
        gui.setItem(msize - 5, close);

        player.openInventory(gui);
        guiPlayers.put(player, playersInGui);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().equalsIgnoreCase("openmanageemenu")) {
            openMenu(e.getPlayer());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equals("Player Management")) {
            guiPlayers.remove((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getView().getTopInventory().getTitle().equals("Player Management")) {
                e.setCancelled(true);

                if (e.getClickedInventory().equals(e.getView().getTopInventory())) {

                    if (e.getSlot() < e.getView().getTopInventory().getSize() - 9) {
                        HashMap<Integer, Player> slotPlayers = guiPlayers.get(player);
                        int slot = e.getSlot();
                        if (slotPlayers.containsKey(slot)) {
                            new ManagementGUI().openMenu(player, slotPlayers.get(slot));
                        }
                    }

                    if (e.getSlot() == e.getView().getTopInventory().getSize() - 5) {
                        e.getWhoClicked().closeInventory();
                    }

                }
            }
        }
    }

}
