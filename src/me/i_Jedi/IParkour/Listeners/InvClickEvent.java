package me.i_Jedi.IParkour.Listeners;

import me.i_Jedi.IParkour.Inventories.InventoryHandler;
import me.i_Jedi.IParkour.Parkour.Point;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

            //Check which inventory
            InventoryHandler invHandler = new InventoryHandler(plugin);
            //Edit inventory
            if(invHandler.getEditInventory().getName().equals(invName)){
                //Open confirm inventory
                List<String> itemLore = event.getCurrentItem().getItemMeta().getLore();
                player.openInventory(invHandler.getConfirmInventory().getInventory(itemName, itemLore.get(1)));
                event.setCancelled(true);

            //World inventory
            }else if(invHandler.getWorldInventory().getName().equals(invName)){
                for(World w : Bukkit.getWorlds()){
                    if(w.getName().equals(itemName)){
                        player.openInventory(invHandler.getEditInventory().getInventory(itemName));
                        event.setCancelled(true);
                    }
                }
                event.setCancelled(true);
                return;
            //Confirm inventory
            }else if(invHandler.getConfirmInventory().getName().equals(invName)){
                List<String> lore = event.getInventory().getItem(12).getItemMeta().getLore();
                String pointName = lore.get(3);
                String worldName = lore.get(5);
                Point pointInfo = new Point(plugin, Bukkit.getWorld(worldName), pointName);
                //See which option was clicked
                if(itemName.equals("YES")){
                    //Remove the point
                    pointInfo.removePoint();
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "" + pointName + ChatColor.GOLD + " has been removed.");

                    //Check if there are any points left for this world. If not take the player back to the world selection inventory
                    if(pointInfo.anyPoints()){
                        player.openInventory(invHandler.getEditInventory().getInventory(worldName));
                        event.setCancelled(true);
                        return;
                    }else{
                        player.openInventory(invHandler.getWorldInventory().getInventory());
                        event.setCancelled(true);
                        return;
                    }

                }//Else do nothing for the other options. They both take the player back to the editInventory
                player.openInventory(invHandler.getEditInventory().getInventory(worldName));
                event.setCancelled(true);
            }

        }
    }
}
