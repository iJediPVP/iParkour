package me.i_Jedi.IParkour.Inventories;

import me.i_Jedi.IParkour.Parkour.Point;
import me.ijedi.menulibrary.Menu;
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

public class WorldInventory {

    //Variables
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;

    //Constructor
    public WorldInventory(JavaPlugin plugin){
        this.plugin = plugin;

        //World item list
        List<ItemStack> worldItems = new ArrayList<>();
        boolean loadInv = false;
        int slot = 0;
        for(World world : Bukkit.getWorlds()){
            file = new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
            config = YamlConfiguration.loadConfiguration(file);
            int pointCount = 0;
            for(String key : config.getConfigurationSection("").getKeys(false)){
                Point point = new Point(plugin, world, key);
                if(point.getExists()){
                    pointCount++;
                }
            }

            //If there is at least one point add the world to the list
            if(pointCount > 0){
                ItemStack iStack = new ItemStack(Material.GLASS);
                //Set the material according to the worlds environment
                World.Environment worldEnv = world.getEnvironment();
                ChatColor cc = null;
                String type = null;

                //Overworld
                if(worldEnv.equals(World.Environment.NORMAL)){
                    iStack = new ItemStack(Material.GRASS);
                    cc = ChatColor.GREEN;
                    type = "Overworld";
                    //Nether
                }else if(worldEnv.equals(World.Environment.NETHER)){
                    iStack = new ItemStack(Material.NETHERRACK);
                    cc = ChatColor.RED;
                    type = "Nether";
                    //End
                }else if(worldEnv.equals(World.Environment.THE_END)){
                    iStack = new ItemStack(Material.ENDER_STONE);
                    cc = ChatColor.YELLOW;
                    type = "End";
                }

                //Set item meta
                ItemMeta iMeta = iStack.getItemMeta();
                iMeta.setDisplayName(cc + world.getName());
                iMeta.setLore(Arrays.asList(ChatColor.GOLD + "Points: " + pointCount, ChatColor.GOLD + "Type: " + cc + type));
                iStack.setItemMeta(iMeta);

                worldItems.add(iStack);
            }
        }

        //Create world menu
        ItemStack[] worldItemArray = worldItems.toArray(new ItemStack[worldItems.size()]);
        Menu menu = new Menu("World List");
        menu.setContents(worldItemArray);

        //Set buttons
        //Set buttons
        ItemStack exitButton = new ItemStack(Material.ARROW);
        ItemMeta exitMeta = exitButton.getItemMeta();
        exitMeta.setDisplayName(ChatColor.RED + "Exit");
        exitButton.setItemMeta(exitMeta);

        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back");
        backButton.setItemMeta(backMeta);

        ItemStack nextButton = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextButton.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "Next");
        nextButton.setItemMeta(nextMeta);

        menu.setButtons(exitButton, backButton, nextButton);
    }
}
