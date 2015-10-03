/*TODO LIST

* */

package me.i_Jedi.IParkour;

import me.i_Jedi.IParkour.Commands.IParkourCom;
import me.i_Jedi.IParkour.Commands.IParkourTabComplete;
import me.i_Jedi.IParkour.Listeners.*;
import me.i_Jedi.IParkour.Parkour.PlayerInfo;
import me.i_Jedi.MenuAPI.MenuButtonListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    //Variables

    //Enabled
    @Override
    public void onEnable(){
        //Register commands
        getCommand("iparkour").setExecutor(new IParkourCom(this));
        getCommand("iparkour").setTabCompleter(new IParkourTabComplete());

        //Register events
        new InvClickEvent(this);
        new PBlockBreakEvent(this);
        new PChangeWorldEvent(this);
        new PInteractEvent(this);
        new PJoinEvent(this);
        new SChangeEvent(this);

        new MenuButtonListener(this);


        //Make sure all online player files are there
        for(Player p : Bukkit.getOnlinePlayers()){
            PlayerInfo pInfo = new PlayerInfo(p, this);
            if(!pInfo.fileExists()){
                pInfo.setDefaults();
            }
        }

        //Logger
        getLogger().info("iParkour has been enabled!");
    }

    //Disabled
    @Override
    public void onDisable(){
        //Logger
        getLogger().info("iParkour has been enabled!");
    }

}
