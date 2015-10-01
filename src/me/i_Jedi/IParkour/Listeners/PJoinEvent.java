package me.i_Jedi.IParkour.Listeners;

import me.i_Jedi.IParkour.Parkour.PlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PJoinEvent implements Listener {
    //Variables
    private JavaPlugin plugin;

    //Constructor
    public PJoinEvent(JavaPlugin jp){
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void pJoin(PlayerJoinEvent event){
        //Make sure the player's info is there
        PlayerInfo pInfo = new PlayerInfo(event.getPlayer(), plugin);
        if(!pInfo.fileExists()){
            pInfo.setDefaults();
        }
    }
}
