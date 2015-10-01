package me.i_Jedi.IParkour.Inventories;

import org.bukkit.plugin.java.JavaPlugin;

public class InventoryHandler {
    //Variables
    private ConfirmInventory confirmInventory;
    private EditInventory editInventory;
    private WorldInventory worldInventory;
    private JavaPlugin plugin;

    //Constructor
    public InventoryHandler(JavaPlugin plugin){
        this.plugin = plugin;
        initializeInv();
    }

    //Initialize inventories
    private void initializeInv(){
        confirmInventory = new ConfirmInventory();
        editInventory = new EditInventory(plugin);
        worldInventory = new WorldInventory(plugin);
    }

    //Get inventories
    public ConfirmInventory getConfirmInventory(){
        return confirmInventory;
    }

    public EditInventory getEditInventory(){
        return editInventory;
    }

    public WorldInventory getWorldInventory(){
        return worldInventory;
    }


}
