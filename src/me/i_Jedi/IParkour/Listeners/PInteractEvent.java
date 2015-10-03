package me.i_Jedi.IParkour.Listeners;

import me.i_Jedi.IParkour.Parkour.PlayerInfo;
import me.i_Jedi.IParkour.Parkour.Point;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PInteractEvent implements Listener {
    //Variables
    private JavaPlugin plugin;

    //Constructor
    public PInteractEvent(JavaPlugin jp) {
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void pInteract(PlayerInteractEvent event) {
        //Store player, action, block, world, location
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        World world;
        try{
            world = block.getWorld();
        }catch(NullPointerException npe){
            return;
        }
        Location loc = block.getLocation();

        //Keep players from clicking the points
        if(action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)){
            //Check if its a gold or iron plate
            if(block.getType().equals(Material.GOLD_PLATE) || block.getType().equals(Material.IRON_PLATE)){
                //Check if this blocks coords match coords in the file
                File file = new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                for(String key : config.getConfigurationSection("").getKeys(false)){
                    Point pointInfo = new Point(plugin, world, key);
                    Location keyLoc = pointInfo.getLocation();
                    if(keyLoc.equals(loc)){
                        //Make sure it exists
                        if(pointInfo.getExists()){
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            //Else player stepped on it
        }else if(action.equals(Action.PHYSICAL)){
            PlayerInfo pInfo = new PlayerInfo(player, plugin);
            //Check if iron or gold
            //Gold
            if(block.getType().equals(Material.GOLD_PLATE)){
                //See if the coords match the Start or Finish for this world
                //***** Start *****
                Point pointInfo = new Point(plugin, world, "Start");
                if(pointInfo.getLocation().equals(loc)){
                    if(pointInfo.getExists()){
                        //Check if the player has a cooldown
                        if(!pInfo.getHasStartCD()){
                            //Check for finish point
                            Point point = new Point(plugin, world, "Finish");
                            if(point.getExists()){
                                pInfo.setStartTime(System.currentTimeMillis());
                                pInfo.setLastCP("null");
                                pInfo.setHasStartCD(true);
                                pInfo.setHasFinishCD(false);
                                //Check if started
                                if(pInfo.getHasStarted()){
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour]" + ChatColor.GOLD + " Your time has been reset! Good luck!");
                                }else{
                                    pInfo.setHasStarted(true);
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour]" + ChatColor.GOLD + " The timer has started! Good luck!");
                                }
                                //Finish doesn't exist
                            }else{
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour]" + ChatColor.RED + " There has to be a Finish point before you can start!");
                                pInfo.setHasStartCD(true);
                            }


                        }
                    }//Else do nothing

                 //***** Finish *****
                }else{
                    pointInfo = new Point(plugin, world, "Finish");
                    if(pointInfo.getLocation().equals(loc)){
                        //Check if exists
                        if(pointInfo.getExists()){
                            //Check for Start
                            Point point = new Point(plugin, world, "Start");
                            if(point.getExists()){
                                //Check if the player has started
                                if(pInfo.getHasStarted()){
                                    //Check for finish cooldown
                                    if(!pInfo.getHasFinishCD()){
                                        Long endTime = System.currentTimeMillis();
                                        pInfo.checkForRecord(endTime);
                                        pInfo.setHasStarted(false);
                                        pInfo.setHasStartCD(false);
                                        pInfo.setHasFinishCD(true);
                                        pInfo.setLastCP("null");

                                    }
                                }else{
                                    if(!pInfo.getHasFinishCD()){
                                        pInfo.setHasFinishCD(true);
                                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.GOLD + "You have to start at the beginning!");
                                    }
                                }

                                //Start doesn't exist
                            }else{
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour]" + ChatColor.RED + " There has to be a Start point before you can finish!");
                                pInfo.setHasStartCD(true);
                            }


                        }//Else do nothing
                    }
                }

                //Iron
            }else if(block.getType().equals(Material.IRON_PLATE)){
                File file = new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                for(String key : config.getConfigurationSection("").getKeys(false)){
                    //If start or finish ignore it
                    if(!key.equals("Start") && !key.equals("Finish")){
                        Point pointInfo = new Point(plugin, world, key);
                        if(loc.equals(pointInfo.getLocation()) && pointInfo.getExists()){
                            //Check if player has started
                            if(pInfo.getHasStarted()){
                                //Check for new checkpoint
                                String lastCP = pInfo.getLastCP();
                                if(lastCP.equals("null") || !lastCP.equals(key)){
                                    pInfo.setLastCP(key);
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.GOLD + "You have reached a Checkpoint! Use the command " +
                                            ChatColor.GREEN + "" + ChatColor.BOLD + "/iparkour checkpoint" + ChatColor.GOLD + " to return here!");
                                    return;
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}
