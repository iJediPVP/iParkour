package me.i_Jedi.IParkour.Listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PChangeWorldEvent implements Listener {

    //Variables
    private JavaPlugin plugin;

    //Constructor
    public PChangeWorldEvent(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void pChangeWorld(PlayerChangedWorldEvent event){
        //Store worldFrom and player
        String worldName = event.getFrom().getName();
        Player player = event.getPlayer();

        //Set has started for this world to false
        File file = new File(plugin.getDataFolder() + "/playerData/" + player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(worldName + ".hasStarted", false);
        config.set(worldName + ".lastCheckpoint", "null");
        try{
            config.save(file);
        }catch(IOException ioe){
            plugin.getLogger().info("PChangeWorldEvent - Error saving file.");
        }
    }
}
