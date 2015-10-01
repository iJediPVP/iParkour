package me.i_Jedi.IParkour.Commands;

import me.i_Jedi.IParkour.Inventories.InventoryHandler;
import me.i_Jedi.IParkour.Parkour.PlayerInfo;
import me.i_Jedi.IParkour.Parkour.Point;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class IParkourCom implements CommandExecutor {

    //Variables
    private JavaPlugin plugin;
    private List<String> helpMsgs = new ArrayList<String>(){{
        add(ChatColor.AQUA + "" + ChatColor.BOLD + "===== iParkour Commands =====");
        add(ChatColor.GOLD + "" + ChatColor.BOLD + "Use " + ChatColor.GREEN + "" + ChatColor.BOLD + "/iparkour edit " + ChatColor.GOLD + "" + ChatColor.BOLD + "to edit the parkour course.");
        add(ChatColor.GOLD + "" + ChatColor.BOLD + "Use " + ChatColor.GREEN + "" + ChatColor.BOLD + "/iparkour checkpoint " + ChatColor.GOLD + "" + ChatColor.BOLD + "to return to the last checkpoint you reached.");
    }};

    //Constructor
    public IParkourCom(JavaPlugin jp){
        plugin = jp;
    }

    //Command
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        //Check for player
        if(sender instanceof Player){
            //Store player and command
            Player player = (Player) sender;
            String command = cmd.getName().toUpperCase();

            //Check command
            if(command.equals("IPARKOUR")){
                //Check for perms
                if(player.hasPermission("iParkour.iparkour")){
                    //Check args
                    if(args.length == 0) {
                        //Display a list of commands
                        for (String str : helpMsgs) {
                            player.sendMessage(str);
                        }
                        return true;

                    }else if(args.length == 1){
                        //See which sub command
                        String subCMD = args[0].toUpperCase();

                        //EDIT
                        if(subCMD.equals("EDIT")){
                            //Check for perms
                            if(player.hasPermission("iParkour.iparkour.edit")){
                                //Check player's gamemode
                                if(player.getGameMode().equals(GameMode.CREATIVE)){
                                    player.openInventory(new InventoryHandler(plugin).getWorldInventory().getInventory());
                                    return true;
                                }else{
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You must be in CREATIVE mode to use this command.");
                                }

                            }else{ //No perms
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You do not have permission to use this command.");
                                return true;
                            }
                        //CHECKPOINT
                        }else if(subCMD.equals("CHECKPOINT")){
                            //Check for perms
                            if(player.hasPermission("iParkour.iparkour.checkpoint")){
                                //Check if the player has started
                                PlayerInfo pInfo = new PlayerInfo(player, plugin);
                                if(pInfo.getHasStarted()){
                                    String lastCP = pInfo.getLastCP();
                                    if(lastCP.equals("null")){
                                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.GOLD + "You have not reached a Checkpoint yet!");
                                        return true;
                                    }else{
                                        Point pointInfo = new Point(plugin, player.getWorld(), pInfo.getLastCP());
                                        Location cpLoc = pointInfo.getLocation();
                                        cpLoc.setX(cpLoc.getX() + .5);
                                        cpLoc.setY(cpLoc.getY() + .5);
                                        cpLoc.setZ(cpLoc.getZ() + .5);
                                        cpLoc.setYaw(player.getLocation().getYaw());
                                        player.teleport(cpLoc);
                                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.GOLD + "You were teleported to " + ChatColor.GREEN + "" + ChatColor.BOLD + lastCP + ChatColor.GOLD + "!");
                                        return true;
                                    }
                                }else{
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.GOLD + "You have to start before you can use a Checkpoint!");
                                    return true;
                                }
                            }else{ //No perms
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You do not have permission to use this command.");
                                return true;
                            }
                        }
                        return true;
                    }
                }else{ //No perms
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You do not have permission to use this command.");
                    return true;
                }
            }//Else do nothing

        //Else give sender error
        }else{
            sender.sendMessage("[iParkour] This command is only for players!");
        }
        return true;
    }
}
