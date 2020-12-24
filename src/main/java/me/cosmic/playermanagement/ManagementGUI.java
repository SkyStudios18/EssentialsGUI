package me.cosmic.playermanagement;

import com.google.common.collect.Lists;
import me.cosmic.Main;
import org.bukkit.BanList;
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

import java.util.HashMap;

public class ManagementGUI implements Listener {

    private static HashMap<Player, Player> managing = new HashMap<>();
    private static HashMap<Player, ManageType> manageType = new HashMap<>();
    // here we'll put in which player is managing which player.

    public void openMenu(Player player, Player targetPlayer) {
        if (targetPlayer != null) {
            Inventory gui = Bukkit.createInventory(null, 36, "Managing: " + targetPlayer.getName());

            ItemStack kick = new ItemStack(Material.TNT);
            ItemMeta kMeta = kick.getItemMeta();
            kMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Kick player");
            kMeta.setLore(Lists.newArrayList(ChatColor.GRAY + "Click here to " + ChatColor.YELLOW + "kick" + ChatColor.GRAY + " the player"));
            kick.setItemMeta(kMeta);
            gui.setItem(12, kick);

            ItemStack ban = new ItemStack(Material.REDSTONE_TORCH);
            ItemMeta bMeta = ban.getItemMeta();
            bMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Ban player");
            bMeta.setLore(Lists.newArrayList(ChatColor.GRAY + "Click here to " + ChatColor.YELLOW + "ban" + ChatColor.GRAY + " the player"));
            ban.setItemMeta(bMeta);
            gui.setItem(14, ban);

            player.openInventory(gui);
            managing.put(player, targetPlayer);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (managing!=null && managing.containsKey(player)) {
            Player target = managing.get(player);

            if (e.getClickedInventory() != null) {
                if (e.getClickedInventory().equals(e.getView().getTopInventory())) {
                    // player clicked the top inventory
                    if (e.getSlot() == 12 ) {
                        manageType.put(player, ManageType.KICK);
                        player.sendMessage(ChatColor.GRAY + "You are about to kick " + target.getName());
                        player.sendMessage(ChatColor.GRAY + "If you want to cancel please type " + ChatColor.RED + "cancel" + ChatColor.GRAY + " in the chat.");
                        player.sendMessage(ChatColor.GRAY + "If you want to proceed, please type the reason for kicking.");
                        player.closeInventory();
                    }
                    else if (e.getSlot() == 14) {
                        manageType.put(player, ManageType.BAN);
                        player.sendMessage(ChatColor.GRAY + "You are about to ban " + target.getName());
                        player.sendMessage(ChatColor.GRAY + "If you want to cancel please type " + ChatColor.RED + "cancel" + ChatColor.GRAY + " in the chat.");
                        player.sendMessage(ChatColor.GRAY + "If you want to proceed, please type the reason for banning.");
                        player.closeInventory();
                    }
                }
            }

        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();

        if (managing!= null && !manageType.containsKey(player)) {
            managing.remove(player);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (managing!= null && managing.containsKey(e.getPlayer()) && manageType!=null && manageType.containsKey(e.getPlayer())) {
            // player is still managing another player.
            e.setCancelled(true);
            Player target = managing.get(e.getPlayer());
            ManageType manage = manageType.get(e.getPlayer());

            String message = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            if (message.equalsIgnoreCase("cancel")) {
                e.getPlayer().sendMessage(ChatColor.RED + "You cancelled the " + manage.toString().toLowerCase() + " of " + target.getName());
                managing.remove(e.getPlayer());
                manageType.remove(e.getPlayer());
                return;
            }

            if (manage == ManageType.BAN) {
                Bukkit.getScheduler().runTask(Main.get(), () -> {
                    target.kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "You have been banned\n" + ChatColor.GRAY + "Reason:\n" + message);
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), message, null, null);
                    e.getPlayer().sendMessage(ChatColor.RED + "You banned " + target.getName() + " for: '" + ChatColor.GRAY + message + ChatColor.RED + "'");
                    managing.remove(e.getPlayer());
                    manageType.remove(e.getPlayer());
                });
            }
            else if (manage == ManageType.KICK) {
                Bukkit.getScheduler().runTask(Main.get(), () -> {
                    target.kickPlayer(message);
                    e.getPlayer().sendMessage(ChatColor.RED + "You kicked " + target.getName() + " for: '" + ChatColor.GRAY + message + ChatColor.RED + "'");
                    managing.remove(e.getPlayer());
                    manageType.remove(e.getPlayer());
                });

            }
        }
    }

}
