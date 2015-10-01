package me.i_Jedi.IParkour.Inventories;



import me.i_Jedi.IParkour.Parkour.Point;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditInventory {
    
    //Variables
    private Inventory editInventory = Bukkit.createInventory(null, 27, "Parkour Editor");
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;

    //Constructor
    public EditInventory(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(String worldName){
        //Determine the size of the inventory
        int pointCount = 0;
        try{
            World world = Bukkit.getWorld(worldName);
            file = new File(plugin.getDataFolder() + "/worldData/" + worldName + ".yml");
            config = YamlConfiguration.loadConfiguration(file);
            for(String key : config.getConfigurationSection("").getKeys(false)){
                Point point = new Point(plugin, world, key);
                if(point.getExists()){
                    pointCount++;
                }
            }
        }catch(IllegalArgumentException iae){
            return null;
        }


        //Load each point into this inv
        List<ItemStack> iStackList = new ArrayList<ItemStack>();
        try{
            World world = Bukkit.getWorld(worldName);
            file = new File(plugin.getDataFolder() + "/worldData/" + worldName + ".yml");
            config = YamlConfiguration.loadConfiguration(file);
            for(String key : config.getConfigurationSection("").getKeys(false)){
                Point point = new Point(plugin, world, key);
                if(point.getExists()){
                    ItemStack iStack = new ItemStack(Material.GOLD_PLATE, 1);
                    if(!key.equals("Start") && !key.equals("Finish")){
                        iStack = new ItemStack(Material.IRON_PLATE, 1);
                    }
                    ItemMeta iMeta = iStack.getItemMeta();
                    iMeta.setDisplayName(ChatColor.GOLD + key);
                    iMeta.setLore(Arrays.asList(ChatColor.GOLD + "World:", worldName, ChatColor.RED + "" + ChatColor.BOLD + "Click to remove this point!"));
                    iStack.setItemMeta(iMeta);
                    iStackList.add(iStack);

                }

            }
        }catch(NullPointerException npe){
            return null;
        }
        //Set start and finish to the 0 and last index
        for(ItemStack stack : iStackList){
            String itemName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
            if(itemName.equals("Start")){
                editInventory.setItem(editInventory.firstEmpty(), stack);
            }else if(itemName.equals("Finish")){
                editInventory.setItem(pointCount - 1, stack);
            }
        }

        //Add the other points in between
        for(ItemStack stack : iStackList){
            String itemName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
            if(!itemName.equals("Start") && !itemName.equals("Finish")){
                editInventory.setItem(editInventory.firstEmpty(), stack);
            }
        }



        return editInventory;
    }

    //Get inventory name
    public String getName(){
        return editInventory.getName();
    }
}
