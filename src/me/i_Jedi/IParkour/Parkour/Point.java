package me.i_Jedi.IParkour.Parkour;

import me.i_Jedi.IParkour.Inventories.EditInventory;
import me.i_Jedi.IParkour.Inventories.WorldInventory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;

public class Point {

    //Variables
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;
    private String path;
    private World world;

    //Constructor
    public Point(JavaPlugin plugin, World world, String pointName){
        this.plugin = plugin;
        this.world = world;
        path = pointName;
        file = new File(plugin.getDataFolder() + "/worldData/" + world.getName() + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    //Save file
    public void saveFile(){
        try{
            config.save(file);
        }catch(IOException ioe){
            plugin.getLogger().info("Point - Error saving file.");
        }
    }

    //Set exists
    public void setExists(boolean bool){
        config.set(path + ".exists", bool);
        saveFile();
    }

    //Get exists
    public boolean getExists(){
        return config.getBoolean(path + ".exists");
    }

    //Set location
    public void setLocation(Location location){
        config.set(path + ".coords.x", location.getBlockX());
        config.set(path + ".coords.y", location.getBlockY());
        config.set(path + ".coords.z", location.getBlockZ());
        saveFile();
    }

    //Get location
    public Location getLocation(){
        double[] coords = new double[3];
        coords[0] = config.getDouble(path + ".coords.x");
        coords[1] = config.getDouble(path + ".coords.y");
        coords[2] = config.getDouble(path + ".coords.z");
        return new Location(world, coords[0], coords[1], coords[2]);
    }

    //Set checkpoint
    public void setCheckpoint(Block block, Player player){
        //Check if this point already exists
        if(!getExists()){
            //Change to iron plate
            block.setType(Material.IRON_PLATE);

            //Summon stands
            new Stands().summonStands(block.getLocation(), path);

            //Store location
            setLocation(block.getLocation());
            setExists(true);
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + path + ChatColor.GOLD + " has been set.");

            //Update editInv & worldInv in case someone is viewing it
            new EditInventory(plugin, world.getName());
            new WorldInventory(plugin);
        }else{
            //Set to air and tell player that this point already exists
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED +
            path + ChatColor.GOLD + " already exists in this world!");
            block.setType(Material.AIR);
        }
    }

    //Set start/finish
    public void setSF(Block block, Player player){
        //Check if this point already exists
        if(!getExists()){
            //Change to gold plate
            block.setType(Material.GOLD_PLATE);

            //Summon stands
            new Stands().summonStands(block.getLocation(), path);

            //Store location
            setLocation(block.getLocation());
            setExists(true);
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + path + ChatColor.GOLD + " has been set.");

            //Update editInv & worldInv in case someone is viewing it
            new EditInventory(plugin, world.getName());
            new WorldInventory(plugin);
        }else{
            //Set to air and tell player that this point already exists
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED +
            path + ChatColor.GOLD + " already exists in this world!");
            block.setType(Material.AIR);
        }
    }

    //Remove point
    public void removePoint(){
        //Get the location, adjust coords, and remove armor stands above it
        Location loc = getLocation();
        loc.getBlock().setType(Material.AIR);
        loc.setY(loc.getY() + .7);
        loc.setX(loc.getX() + .5);
        loc.setZ(loc.getZ() + .5);

        //Loop through armor stands in the world
        for(Entity e : world.getEntitiesByClass(ArmorStand.class)){
            //Compare its location to the above location
            Location eLoc = e.getLocation();
            if(loc.getX() == eLoc.getX() && loc.getZ() == eLoc.getZ() && (loc.getY() - eLoc.getY() <= 0.25D)){
                //Remove
                e.remove();
            }
        }
        config.set(path, null);
        setExists(false);

        //Update editInv & worldInv in case someone is viewing it
        new EditInventory(plugin, world.getName());
        new WorldInventory(plugin);
    }

    //Check for any points in this world
    public boolean anyPoints(){
        for(String key : config.getConfigurationSection("").getKeys(false)){
            path = key;
            if(getExists()){
                return true;
            }
        }
        return false;
    }


}
