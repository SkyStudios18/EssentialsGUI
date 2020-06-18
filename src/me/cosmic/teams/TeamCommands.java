package me.cosmic.teams;

import com.google.common.collect.Lists;
import me.cosmic.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TeamCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        /// > /test arg1

        if (command.getName().equalsIgnoreCase("team")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(Messaging.TeamMessaging.CORRECT_USAGE_0);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("chat")) {
                        Team team = Team.getTeam(player);
                        if (team == null) {
                            sender.sendMessage(Messaging.TeamMessaging.NOT_IN_TEAM);
                            return true;
                        }
                        if (team.getMembers().size() == 0) {
                            sender.sendMessage(ChatColor.RED + "You must have at least one member to be able to use the team chat!");
                            return true;
                        }

                        TeamHandler.setTeamChat(player, !TeamHandler.isInTeamChat(player));
                        String status = TeamHandler.isInTeamChat(player) ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";
                        player.sendMessage(ChatColor.GRAY + "Your team chat has been " + status);
                        return true;

                    }
                    if (args[0].equalsIgnoreCase("accept")) {
                        sender.sendMessage(Messaging.TeamMessaging.CORRECT_USAGE_ACCEPT);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("info")) {
                        Team team = Team.getTeam(player);
                        if (team == null) {
                            sender.sendMessage(Messaging.TeamMessaging.NOT_IN_TEAM);
                            return true;
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7===== Team Info ====="));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bOwner: &e" + Bukkit.getPlayer(team.getOwner()).getName()));
                        List<UUID> members = team.getMembers();
                        if (members.isEmpty()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bMembers: &cno players"));
                            return true;
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bMembers:"));
                        for (UUID uid : members) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f- &e" + Bukkit.getPlayer(uid).getName()));
                        }
                        return true;
                    }

                    Team team = Team.getTeam(player);

                    Player target = Bukkit.getPlayer(args[0]); // argument player to invite
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "There is no player online with that username.");
                        return true;
                    }

                    if (target.getUniqueId().equals(player.getUniqueId())) {
                        sender.sendMessage(ChatColor.RED + "You can't invite yourself.");
                        return true;
                    }

                    // the user is online

                    if (team != null) {
                        // player is in a team
                        if (team.getOwner().equals(player.getUniqueId())) {
                            // player is owner of the team
                            team.invitePlayer(target);
                        } else {
                            // player is now the owner of the team, aborting
                            player.sendMessage(ChatColor.RED + "You must be the owner of the team to invite other players.");
                            return true;
                        }
                    } else {
                        // player is not in a team
                        team = new Team(player.getUniqueId(), Lists.newArrayList());
                        team.invitePlayer(target);
                        Main.get().addTeam(team);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.get(), () -> {
                        if (player.isOnline()) {
                            Team pTeam = Team.getTeam(player);
                            if (pTeam != null) {
                                pTeam.removeInvitedPlayer(target);
                                if (pTeam.getMembers().isEmpty()) {
                                    // team is empty, removing it
                                    player.sendMessage(ChatColor.RED + "No one accepted your invite(s), your party is now disbanded");
                                    pTeam.disbandTeam();
                                }
                            }

                        }
                    }, 60 * 20);

                } else {
                    if (args[0].equalsIgnoreCase("chat")) {
                        Team team = Team.getTeam(player);
                        if (team == null) {
                            sender.sendMessage(Messaging.TeamMessaging.NOT_IN_TEAM);
                            return true;
                        }
                        if (team.getMembers().size() == 0) {
                            sender.sendMessage(ChatColor.RED + "You must have at least one member to be able to use the team chat!");
                            return true;
                        }

                        StringBuilder chatMessage = new StringBuilder();
                        for (int i=1; i<args.length; i++) {
                            chatMessage.append(args[i]).append(" ");
                        }
                        team.sendMessage("&8[&bTeam&8] &a" + player.getName() + "&7:&e " + chatMessage);
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("accept")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "There is no player online with that username.");
                            return true;
                        }

                        Team team = Team.getTeam(target);
                        TeamInviteResponse response = team == null ? TeamInviteResponse.NOT_INVITED : team.acceptPlayer(player);
                        if (response == TeamInviteResponse.NOT_INVITED) {
                            sender.sendMessage(ChatColor.RED + "That player didn't invite you or their invite expired.");
                            return true;
                        } else if (response == TeamInviteResponse.ALREADY_IN_PARTY) {
                            sender.sendMessage(ChatColor.RED + "You must leave your current party before you can join another one!");
                            return true;
                        } else if (response == TeamInviteResponse.MAX_PLAYERS) {
                            sender.sendMessage(ChatColor.RED + "That team has reached its member limit! Try again later.");
                            return true;
                        }
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to perform this action");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("unteam")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Team team = Team.getTeam(player);

                if (team == null) {
                    player.sendMessage(ChatColor.RED + "You are not in a team!");
                    return true;
                }

                team.removePlayer(player);
                player.sendMessage(ChatColor.RED + "You left your current team.");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to perform this action");

            }
            return true;
        }

        return false;
    }


}
