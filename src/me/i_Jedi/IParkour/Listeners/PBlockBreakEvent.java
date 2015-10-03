package me.i_Jedi.IParkour.Listeners;

import me.i_Jedi.IParkour.Parkour.Point;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PBlockBreakEvent implements Listener {

    //Variables
    private JavaPlugin plugin;
    //Constructor
    public PBlockBreakEvent(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void blockBreak(BlockBreakEvent event){
        //Get player and block.
        Player player = event.getPlayer();
        Block block = event.getBlock();

        //Get block coords, add 1 to Y, check for point there.
        Location loc = block.getLocation();
        loc.setY(loc.getY() + 1);
        File file = new File(plugin.getDataFolder() + "/worldData/" + block.getWorld().getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String key : config.getConfigurationSection("").getKeys(false)){
            Point pointInfo = new Point(plugin, block.getWorld(), key);
            if(pointInfo.getLocation().equals(loc)){
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You can't break this! It's part of the parkour course!");
                event.setCancelled(true);
                return;
            }
        }

    }
}
