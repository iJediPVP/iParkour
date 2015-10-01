package me.i_Jedi.IParkour.Inventories;

import me.i_Jedi.IParkour.Parkour.Point;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public class WorldInventory {
    
    //Variables
    private Inventory worldInv = Bukkit.createInventory(null, 27, "Parkour World List");
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;


    //Constructor
    public WorldInventory(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(){
        //Only get the worlds that have points in them
        int slot = 0;
        for(World world : Bukkit.getWorlds()){
            //Check if this world has atleast ONE point
            file = new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
            config = YamlConfiguration.loadConfiguration(file);
            int pointCount = 0;
            for(String key : config.getConfigurationSection("").getKeys(false)){
                Point point = new Point(plugin, world, key);
                if(point.getExists()){
                    pointCount++;
                }
            }

            //If there is at least one point add this world ot the list.
            if(pointCount > 0){
                ItemStack iStack = new ItemStack(Material.GLASS);
                //Set the material according to the worlds environment
                World.Environment worldEnv = world.getEnvironment();
                ChatColor cc = null;
                String type = null;

                //Overworld
                if(worldEnv.equals(Environment.NORMAL)){
                    iStack = new ItemStack(Material.GRASS);
                    cc = ChatColor.GREEN;
                    type = "Overworld";
                //Nether
                }else if(worldEnv.equals(Environment.NETHER)){
                    iStack = new ItemStack(Material.NETHERRACK);
                    cc = ChatColor.RED;
                    type = "Nether";
                //End
                }else if(worldEnv.equals(Environment.THE_END)){
                    iStack = new ItemStack(Material.ENDER_STONE);
                    cc = ChatColor.YELLOW;
                    type = "End";
                }

                //Set item meta
                ItemMeta iMeta = iStack.getItemMeta();
                iMeta.setDisplayName(cc + world.getName());
                iMeta.setLore(Arrays.asList(ChatColor.GOLD + "Points: " + pointCount, ChatColor.GOLD + "Type: " + cc + type));
                iStack.setItemMeta(iMeta);
                //Keep from going out of array bounds
                if(slot < worldInv.getSize()){
                    worldInv.setItem(slot, iStack);
                    slot++;
                }else{
                    return worldInv;
                }
            }
        }


        return worldInv;
    }

    //Get inventory name
    public String getName(){
        return worldInv.getName();
    }
}
