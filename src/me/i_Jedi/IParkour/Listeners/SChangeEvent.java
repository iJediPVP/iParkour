package me.i_Jedi.IParkour.Listeners;

import me.i_Jedi.IParkour.Parkour.Signs;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SChangeEvent implements Listener {
    //Variables
    private JavaPlugin plugin;
    private List<String> signCmds = new ArrayList<String>(){{
        add("/pkstart"); //Start of parkour
        add("/pkfinish"); //End of parkour
        add("/pkcheckpoint"); //Checkpoints
    }};

    //Constructor
    public SChangeEvent(JavaPlugin jp) {
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void sChange(SignChangeEvent event) {
        //Get player
        Player player = event.getPlayer();

        //Store sign and get the text
        Block sign = event.getBlock();
        String[] signText = event.getLines();

        //Check for sign command
        String line1 = signText[0];
        if(signCmds.contains(line1)){
            //Check if the player has permission to set points.
            if(player.hasPermission("iParkour.iparkour.edit")){
                //Check gamemode
                if(player.getGameMode().equals(GameMode.CREATIVE)){
                    //Make sure there is a solid block underneath
                    Location testLoc = sign.getLocation();
                    testLoc.setY(testLoc.getY() - 1);
                    Block testBlock = testLoc.getBlock();
                    if(!testLoc.getBlock().getType().isSolid() || !testLoc.getBlock().getType().isOccluding()){
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You cannot set a point on a non solid block!");
                        sign.setType(Material.AIR);
                        return;
                    }
                    //Check which command
                    //Start
                    if(line1.equals(signCmds.get(0))){
                        new Signs(plugin, "Start", sign, player);
                        //Finish
                    }else if(line1.equals(signCmds.get(1))){
                        new Signs(plugin, "Finish", sign, player);
                        //Checkpoint
                    }else if(line1.equals(signCmds.get(2))){
                        //Check for an integer on line 2
                        try{
                            int pointNum = Integer.parseInt(signText[1]);
                            new Signs(plugin, "Checkpoint " + pointNum, sign, player);
                        }catch(NumberFormatException nfe){
                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "Please put the Checkpoint number on the second line of the sign.");
                            sign.setType(Material.AIR);
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You must be in CREATIVE mode before you can do this.");
                    return;
                }
            }else{ //No perms
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[iParkour] " + ChatColor.RED + "You do not have permission to set parkour points.");
                return;
            }


        }
    }
}
