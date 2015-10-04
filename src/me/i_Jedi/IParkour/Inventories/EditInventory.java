package me.i_Jedi.IParkour.Inventories;

import me.i_Jedi.IParkour.Parkour.Point;
import me.i_Jedi.MenuAPI.Menu;
import me.i_Jedi.MenuAPI.MenuManager;
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
import java.util.Collections;
import java.util.List;

public class EditInventory {

    //Variables
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;

    //Constructor
    public EditInventory(JavaPlugin plugin, String worldName){
        this.plugin = plugin;
        //Get world
        World world;
        try{
            world = Bukkit.getWorld(worldName);
        }catch(IllegalArgumentException npe){
            plugin.getLogger().info("EditInventory - World not found.");
            return;
        }

        //Store checkpoint numbers
        List<Integer> cpNumList = new ArrayList<>();
        file = new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
        for(String key : config.getConfigurationSection("").getKeys(false)){
            if(!key.equals("Start") && !key.equals("Finish")){
                //Make sure it exists
                Point point = new Point(plugin, world, key);
                if(point.getExists()){
                    try{
                        int cpNum = Integer.parseInt(key.substring("Checkpoint ".length()));
                        cpNumList.add(cpNum);
                    }catch(NumberFormatException nfe){
                        continue;
                    }
                }
            }
        }
        //Sort cpNumList
        Collections.sort(cpNumList);

        //Get checkpoints by order of cpNumList
        List<ItemStack> editItems = new ArrayList<>();
        for(int x : cpNumList){
            String cpName = "Checkpoint " + x;
            ItemStack iStack = new ItemStack(Material.IRON_PLATE);
            ItemMeta iMeta = iStack.getItemMeta();
            iMeta.setDisplayName(ChatColor.GOLD + cpName);
            iMeta.setLore(Arrays.asList(ChatColor.GOLD + "World: ", worldName, ChatColor.RED + "" + ChatColor.BOLD + "Click to remove this point!"));
            iStack.setItemMeta(iMeta);
            editItems.add(iStack);
        }

        //Get Start/Finish
        String[] sfArray = new String[]{"Start", "Finish"};
        for(String str : sfArray){
            Point point = new Point(plugin, world, str);
            if(point.getExists()){
                ItemStack iStack = new ItemStack(Material.GOLD_PLATE);
                ItemMeta iMeta = iStack.getItemMeta();
                iMeta.setDisplayName(ChatColor.GOLD + str);
                iMeta.setLore(Arrays.asList(ChatColor.GOLD + "World: ", worldName, ChatColor.RED + "" + ChatColor.BOLD + "Click to remove this point!"));
                iStack.setItemMeta(iMeta);
                editItems.add(iStack);
            }
        }

        //Put Start at 0 and Finish at the end
        List<ItemStack> sortedItems = new ArrayList<>();
        ItemStack startItem = null;
        ItemStack finishItem = null;
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

        //Add custom return button if needed. MenuAPI would just use an "Exit" button.
        ItemStack iStack = new ItemStack(Material.ARROW);
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(ChatColor.GOLD + "Return");
        iMeta.setLore(Arrays.asList(ChatColor.GREEN + "Return to the World List"));
        iStack.setItemMeta(iMeta);
        MenuManager mm = new MenuManager();
        Menu menu = mm.getMenuByName("Point Editor");
        int invSize = menu.getList().get(0).getSize();
        ItemStack testItem = menu.getList().get(0).getItem(invSize - 5);
        try{
            String testName = testItem.getItemMeta().getDisplayName();
            if(!testName.isEmpty()){
                menu.getList().get(0).setItem(invSize - 5, iStack);
            }
        }catch(NullPointerException npe){} //Do nothing with this.

    }
}
