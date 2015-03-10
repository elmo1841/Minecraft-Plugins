package com.javaminecraft;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WallBuilder extends JavaPlugin implements Listener {
    
    Player me;
    World world;
    Location spot;
    ArrayList<Loc> placed = new ArrayList<>();
    boolean wallbuilding = false;
    boolean up = true;
    
    class Loc {
        double x, y, z;
        
        Loc(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
        public double getZ() {
            return z;
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, 
            String label, String[] arguments) {
        me = (Player) sender;
        world = me.getWorld();
        spot = me.getLocation();
        
        if (sender instanceof Player) {
            if (label.equalsIgnoreCase("wallbuilder")) {
                wallbuilding = true;
                
                me.sendMessage("Place blocks now.");            
                return true;
            }
            if (label.equalsIgnoreCase("finishwall")) {
                wallbuilding = false;
                up = true;
                me.sendMessage("Building wall now.");
                buildWall(sender, arguments, placed, up);
                return true;
            }
            if (label.equalsIgnoreCase("walldown")) {
                wallbuilding = false;
                up = false;
                me.sendMessage("Building down now.");
                buildWall(sender, arguments, placed, up);
                return true;
            }
                
            }
        
        return false;
    }
    
    @Override
    public void onEnable() {
        Server server = getServer();
        PluginManager manager = server.getPluginManager();
        manager.registerEvents(this, this);
    }
    
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        
        if (!wallbuilding) {
            return;
        }
        if (player != me) {
            return;
        }
        
        Block block = event.getBlock();
        double X = block.getX();
        double Y = block.getY();
        double Z = block.getZ();
        
        Loc loc = new Loc(X, Y, Z);
        
        placed.add(loc);
        
    }
    private void buildWall(CommandSender sender, String[] arguments, ArrayList<Loc> placed, boolean up) {
        if (up) {
            for (Loc loc: placed) {
                placeMoreBlocks(loc, arguments, placed);
            }
        }
        else {
            for (Loc loc: placed) {
                placeBlocksDown(loc, arguments, placed);
            }
        }
        placed.removeAll(placed);
    }
private void placeMoreBlocks(Loc loc, String[] arguments, ArrayList<Loc> placed) {
        double height = 10;
        double X = loc.getX();
        double Y = loc.getY();
        double Z = loc.getZ();
        Location here = new Location(world,X,Y,Z);
        Block block = here.getBlock();
        Material material = block.getType();
        if (arguments.length > 0) {
            try {
                height = Double.parseDouble(arguments[0]);
            }
            catch (NumberFormatException exception) {                
            }            
        }
        for (double i = 1; i < height + 1; i ++) {
            Location current = new Location(world, X, (Y+i), Z);
            Block build = current.getBlock();
            build.setType(material);
        }
        
    }

private void placeBlocksDown(Loc loc, String[] arguments, ArrayList<Loc> placed) {
        double height = 10;
        double X = loc.getX();
        double Y = loc.getY();
        double Z = loc.getZ();
        Location here = new Location(world,X,Y,Z);
        Block block = here.getBlock();
        Material material = block.getType();
        if (arguments.length > 0) {
            try {
                height = Double.parseDouble(arguments[0]);
            }
            catch (NumberFormatException exception) {                
            }            
        }
        for (double i = 1; i < height + 1; i ++) {
            Location current = new Location(world, X, (Y-i), Z);
            Block build = current.getBlock();
            build.setType(material);
        }
        
    }
}