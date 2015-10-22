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

        //Convert to array & set contents
        ItemStack[] itemArray = sortedItems.toArray(new ItemStack[sortedItems.size()]);
        Menu menu = new Menu("Point Editor");
        menu.setContents(itemArray);

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

        //Override "Exit" button. MenuLibrary would close the Menu but we need to return to WorldInventory
        ItemStack iStack = new ItemStack(Material.ARROW);
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(ChatColor.GOLD + "Return");
        iMeta.setLore(Arrays.asList(ChatColor.GREEN + "Return to the World List"));
        iStack.setItemMeta(iMeta);

        int slot = menu.getFirstPage().getSize() - 5;
        try{
            String testName = menu.getFirstPage().getItem(slot).getItemMeta().getDisplayName();
            if(!testName.isEmpty()){
                menu.getFirstPage().setItem(slot, iStack);
            }
        }catch(NullPointerException npe){} //Do nothing.

    }
}
