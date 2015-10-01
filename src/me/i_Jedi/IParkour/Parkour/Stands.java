package me.i_Jedi.IParkour.Parkour;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Stands {

    //Summon title stands for points
    public void summonStands(Location loc, String subTitle){
        //Center the stand and increase it's Y
        loc.setX(loc.getX() + .5);
        loc.setY(loc.getY() + .7);
        loc.setZ(loc.getZ() + .5);

        //Title stand
        ArmorStand titleStand = loc.getWorld().spawn(loc, ArmorStand.class);
        titleStand.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour");
        titleStand.setCustomNameVisible(true);
        titleStand.setVisible(false);
        titleStand.setGravity(false);

        //Subtitle stand - .25 lower than title stand
        loc.setY(loc.getY() - .25);
        ArmorStand subTitleStand = loc.getWorld().spawn(loc, ArmorStand.class);
        subTitleStand.setCustomName(ChatColor.AQUA + "" + ChatColor.BOLD + subTitle);
        subTitleStand.setCustomNameVisible(true);
        subTitleStand.setVisible(false);
        subTitleStand.setGravity(false);
    }
}
