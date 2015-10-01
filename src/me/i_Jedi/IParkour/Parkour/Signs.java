package me.i_Jedi.IParkour.Parkour;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Signs {

    //Constructor
    public Signs(JavaPlugin plugin, String pointName, Block block, Player player){
        World world = block.getWorld();

        //Set the point
        Point pointInfo = new Point(plugin, world, pointName);
        if(pointName.equals("Start") || pointName.equals("Finish")){
            pointInfo.setSF(block, player);

        }else{
            pointInfo.setCheckpoint(block, player);
        }
    }
}
