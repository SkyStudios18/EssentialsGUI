package me.cosmic;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private String c(String a) {
        return ChatColor.translateAlternateColorCodes('&',a);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        Player p = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("gms")) {
                if(sender.hasPermission("essentialsgui.gamemode.survival")) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Your gamemode has been updated to Survival!"));
                    return true;
                }
                sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e You do not have permission essentialsgui.gamemode.survival"));
                return true;
        }

        if(cmd.getName().equalsIgnoreCase("gmc")) {
                if(sender.hasPermission("essentialsgui.gamemode.creative")) {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Your gamemode has been updated to Creative!"));
                    return true;
                }
                sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e You do not have permission essentialsgui.gamemode.creative"));
                return true;
        }

        if(cmd.getName().equalsIgnoreCase("gmsp")) {

                if(sender.hasPermission("essentialsgui.gamemode.spectator")) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Your gamemode has been updated to Spectator!"));
                    return true;
                }
                sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e You do not have permission essentialsgui.gamemode.spectator"));
                return true;
        }

        if(cmd.getName().equalsIgnoreCase("gma")) {
                if(sender.hasPermission("essentialsgui.gamemode.adventure")) {
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(c("&8[&4Essentials&bGUI&8]&e Your gamemode has been updated to Adventure!"));
                    return true;
                }
                sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e You do not have permission essentialsgui.gamemode.adventure"));
                return true;
        }

        if (cmd.getName().equalsIgnoreCase("gamemodes")) {

                if (sender.hasPermission("essentialsgui.gamemode.open")) {
                    GamemodeEvents.get().openInventory((Player) sender);
                    sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e Opening GamemodeGUI..."));
                    return true;
                }
                sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e Your eyes can't handle this big of a Plugin!"));
                return true;
        }
        if (cmd.getName().equalsIgnoreCase("medicgui")) {

            if (sender.hasPermission("essentialsgui.medic.open")) {
                MedicEvents.get().openInventory((Player) sender);
                sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e Opening MedicGUI..."));
                return true;
            }
            sender.sendMessage(c("&8[&4Essentials&bGUI&8]&e Your eyes can't handle this big of a Plugin!"));
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("heal25")) {
            if (p.hasPermission("essentials.heal.25")) {
                p.setHealth(5);
                p.sendMessage(c("&7[&aEssentials&bGUI&7]&e You have been healed 25%"));
            }
            sender.sendMessage(c("&7[&aEssentials&bGUI&7] You don't have permission essentialsgui.heal.25"));
        }
        if(cmd.getName().equalsIgnoreCase("heal50")) {
            if (p.hasPermission("essentials.heal.50")) {
                p.setHealth(10);
                p.sendMessage(c("&7[&aEssentials&bGUI&7]&e You have been healed 50%"));
            }
            sender.sendMessage(c("&7[&aEssentials&bGUI&7] You don't have permission essentialsgui.heal.50"));
        }
        if(cmd.getName().equalsIgnoreCase("heal75")) {
            if (p.hasPermission("essentials.heal.75")) {
                p.setHealth(15);
                p.sendMessage(c("&7[&aEssentials&bGUI&7]&e You have been healed 75%"));
            }
            sender.sendMessage(c("&7[&aEssentials&bGUI&7] You don't have permission essentialsgui.heal.75"));
        }
        if(cmd.getName().equalsIgnoreCase("heal100")) {
            if (p.hasPermission("essentials.heal.100")) {
                p.setHealth(20);
                p.sendMessage(c("&7[&aEssentials&bGUI&7]&e You have been healed 100%"));
            }
            sender.sendMessage(c("&7[&aEssentials&bGUI&7] You don't have permission essentialsgui.heal.100"));
        }
        return false;
    }

}
