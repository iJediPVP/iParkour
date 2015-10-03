package me.i_Jedi.IParkour.Inventories;

import me.i_Jedi.IParkour.Parkour.Point;
import me.i_Jedi.MenuAPI.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditInventory {

    //Variables
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;

    //Constructor
    public EditInventory(JavaPlugin plugin, String worldName){
        this.plugin = plugin;

        //Edit item list
        List<ItemStack> editItems = new ArrayList<>();
        try{
            World world = Bukkit.getWorld(worldName);
            file =  new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
            config = YamlConfiguration.loadConfiguration(file);
            for(String key : config.getConfigurationSection("").getKeys(false)){
                Point point = new Point(plugin, world, key);
                if(point.getExists()){
                    ItemStack iStack = new ItemStack(Material.GOLD_PLATE);
                    if(!key.equals("Start") && !key.equals("Finish")){
                        iStack = new ItemStack(Material.IRON_PLATE);
                    }
                    ItemMeta iMeta = iStack.getItemMeta();
                    iMeta.setDisplayName(ChatColor.GOLD + key);
                    iMeta.setLore(Arrays.asList(ChatColor.GOLD + "World: ", worldName, ChatColor.RED + "" + ChatColor.BOLD + "Click to remove this point!"));
                    iStack.setItemMeta(iMeta);
                    editItems.add(iStack);
                }
            }
        }catch(NullPointerException npe){
            plugin.getLogger().info("EditInventory - World not found.");
        }


        List<ItemStack> sortedItems = new ArrayList<>();
        ItemStack startItem = null, finishItem = null;
        for(ItemStack item : editItems){
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if(name.equals("Start")){
                startItem = item;
            }else if(name.equals("Finish")){
                finishItem = item;
            }else{
                sortedItems.add(item);
            }
        }

        try{
            if(!startItem.equals(null)){
                sortedItems.add(0, startItem);
            }
        }catch(NullPointerException npe){}
        try{
            if(!finishItem.equals(null)){
                sortedItems.add(finishItem);
            }

        }catch(NullPointerException npe){}

        //Convert to array
        ItemStack[] itemArray = sortedItems.toArray(new ItemStack[sortedItems.size()]);
        new Menu(plugin, new ItemStack(Material.ARROW), new ItemStack(Material.ARROW), new ItemStack(Material.ARROW), itemArray, "Point Editor");

    }
}
