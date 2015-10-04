/*TODO LIST
Add worldName to record reset message.
* */

package me.i_Jedi.IParkour;

import me.i_Jedi.IParkour.Commands.IParkourCom;
import me.i_Jedi.IParkour.Commands.IParkourTabComplete;
import me.i_Jedi.IParkour.Listeners.*;
import me.i_Jedi.IParkour.Parkour.PlayerInfo;
import me.i_Jedi.MenuAPI.MenuButtonListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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
        getLogger().info("iParkour has been disable!");
    }

    //Test command to be removed later.
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        String command = cmd.getName().toUpperCase();
        if(command.equals("TEST")){
            int count = Integer.parseInt(args[0]);
            File file = new File(this.getDataFolder() + "/worldData/" + player.getWorld().getName() + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for(int x = 1; x <= count; x++){
                config.set("Checkpoint " + x + ".exists", true);
            }
            try{
                config.save(file);
            }catch(IOException ioe){}
        }

        return true;
    }
}
