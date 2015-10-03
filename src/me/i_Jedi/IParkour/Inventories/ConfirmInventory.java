package me.i_Jedi.IParkour.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ConfirmInventory {

    //Variables
    private Inventory confirmInv = Bukkit.createInventory(null, 27, "Parkour Confirmation");

    //Get inventory
    public Inventory getInventory(String pointName, String worldName){

        //YES
        ItemStack iStack = new ItemStack(Material.WOOL, 1, (short) 13);
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(ChatColor.GREEN + "YES");
        if(pointName.equals("Start") || pointName.equals("Finish")){
            iMeta.setLore(Arrays.asList(ChatColor.RED + "Are you sure you want to delete this point?", ChatColor.RED + "WARNING! This action can NOT be undone!", ChatColor.GOLD + "PointName:", pointName, ChatColor.GOLD + "World:", worldName, ChatColor.RED + "This will reset player records!"));
        }else{
            iMeta.setLore(Arrays.asList(ChatColor.RED + "Are you sure you want to delete this point?", ChatColor.RED + "WARNING! This action can NOT be undone!", ChatColor.GOLD + "PointName:", pointName, ChatColor.GOLD + "World:", worldName));
        }
        iStack.setItemMeta(iMeta);
        confirmInv.setItem(12, iStack);

        //NO
        iStack = new ItemStack(Material.WOOL, 1, (short) 14);
        iMeta.setDisplayName(ChatColor.RED + "NO");
        if(pointName.equals("Start") || pointName.equals("Finish")){
            iMeta.setLore(Arrays.asList(ChatColor.RED + "Are you sure you want to delete this point?", ChatColor.RED + "WARNING! This action can NOT be undone!", ChatColor.GOLD + "PointName:", pointName, ChatColor.GOLD + "World:", worldName, ChatColor.RED + "This will NOT reset player records!"));
        }else{
            iMeta.setLore(Arrays.asList(ChatColor.RED + "Are you sure you want to delete this point?", ChatColor.RED + "WARNING! This action can NOT be undone!", ChatColor.GOLD + "PointName:", pointName, ChatColor.GOLD + "World:", worldName));
        }
        iStack.setItemMeta(iMeta);
        confirmInv.setItem(14, iStack);

        //BACK
        iStack = new ItemStack(Material.ARROW);
        iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(ChatColor.GOLD + "Back");
        iStack.setItemMeta(iMeta);
        confirmInv.setItem(22, iStack);

        return confirmInv;
    }

    //Get inventory name
    public String getName(){
        return confirmInv.getName();
    }
}
