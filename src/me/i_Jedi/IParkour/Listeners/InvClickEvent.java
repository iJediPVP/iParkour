package me.i_Jedi.IParkour.Listeners;

import me.i_Jedi.IParkour.Inventories.ConfirmInventory;
import me.i_Jedi.IParkour.Inventories.EditInventory;
import me.i_Jedi.IParkour.Inventories.WorldInventory;
import me.i_Jedi.IParkour.Parkour.Point;
import me.ijedi.menulibrary.MenuManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvClickEvent implements Listener {
    //Variables
    private JavaPlugin plugin;

    //Constructor
    public InvClickEvent(JavaPlugin jp) {
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void invClick(InventoryClickEvent event) {
        //Check for player
        if(event.getWhoClicked() instanceof Player){
            //Store player, inventory name, and item name
            Player player = (Player) event.getWhoClicked();
            String invName = event.getInventory().getName();
            String itemName = null;
            try{
                itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            }catch(NullPointerException npe){
                return;
            }

            //Find a matching inventory
            ConfirmInventory cInv = new ConfirmInventory();
            if(invName.equals("World List")){
                //Open edit inv
                try{
                    new EditInventory(plugin, itemName);
                    player.openInventory(new MenuManager().getMenu("Point Editor"));
                    event.setCancelled(true);
                    return;
                }catch(NullPointerException npe){
                    event.setCancelled(true);
                    return;
                }

            }else if(invName.equals("Point Editor")){
                if(itemName.equals("Return")){
                    //Go back to world inv
                    new WorldInventory(plugin);
                    player.openInventory(new MenuManager().getMenu("World List"));
                    event.setCancelled(true);
                    return;
                }//Else do this stuff VVV

                //Open confirm inv
                try{
                    String worldName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(1));
                    player.openInventory(cInv.getInventory(itemName, worldName));
                    event.setCancelled(true);
                    return;
                }catch(NullPointerException npe){
                    event.setCancelled(true);
                    return;
                }

            }else if(invName.equals(cInv.getName())){
                //Store pointName and worldName
                List<String> lore = event.getInventory().getItem(12).getItemMeta().getLore();
                String pointName = lore.get(3);
                String worldName = lore.get(5);
                Point pointInfo = new Point(plugin, Bukkit.getWorld(worldName), pointName);
                //See which option was clicked
                if(itemName.equals("YES")){
                    try{
                        //Remove the point
                        pointInfo.removePoint();
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "" + pointName + ChatColor.GOLD + " has been removed.");
                    }catch(NullPointerException npe){}


                    //If start or finish point, reset player records
                    if(pointName.equals("Start") || pointName.equals("Finish")){
                        //Reset player record times.
                        File dir = new File(plugin.getDataFolder() + "/playerData");
                        File[] dirList = dir.listFiles();
                        for(File file : dirList){
                            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                            config.set(worldName + ".recordTime", null);
                            try{
                                config.save(file);
                            }catch(IOException ioe){}
                        }
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "Player records have been reset in " + ChatColor.GREEN + worldName + ChatColor.RED + ".");
                    }

                }//Else do nothing special for NO and Back. They both have the same function.
                //If there are not any more points in this world, go back to the main menu.
                if(pointInfo.anyPoints()){
                    new EditInventory(plugin, worldName);
                    player.openInventory(new MenuManager().getMenu("Point Editor"));
                    event.setCancelled(true);
                    return;
                }else{
                    //See if there are any points in any of the worlds
                    for(World world : Bukkit.getWorlds()){
                        Point pInfo = new Point(plugin, world, "");
                        if(pInfo.anyPoints()){
                            new WorldInventory(plugin);
                            player.openInventory(new MenuManager().getMenu("World List"));
                            event.setCancelled(true);
                            return;
                        }
                    }

                    //Else nothing is left. Close everyone out of the edit menu's
                    List<Player> pList = new ArrayList<>();
                    List<Inventory> worldInvList = new MenuManager().getMenuPageList("World List");
                    List<Inventory> editInvList = new MenuManager().getMenuPageList("Point Editor");
                    for(Inventory inv : worldInvList){
                        for(HumanEntity he : inv.getViewers()){
                            Player p = (Player) he;
                            if(!pList.contains(p)){
                                pList.add(p);
                            }
                        }
                    }
                    for(Inventory inv : editInvList){
                        for(HumanEntity he : inv.getViewers()){
                            Player p = (Player) he;
                            if(!pList.contains(p)){
                                pList.add(p);
                            }
                        }
                    }
                    pList.add(player);
                    for(Player p : pList){
                        if(!p.equals(player)){
                            p.closeInventory();
                            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + player.getPlayerListName() + ChatColor.RED + " has removed the last point on the server.");
                        }

                    }
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "There are no more points registered on the server.");
                    player.closeInventory();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
