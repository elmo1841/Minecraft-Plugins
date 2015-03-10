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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DigPit extends JavaPlugin implements Listener {
    
    Player me;
    World world;
    Location spot;
    ArrayList<Loc> broke = new ArrayList<>();
    boolean pitdigging = false;
    boolean down = true;
    
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
            if (label.equalsIgnoreCase("digpit")) {
                pitdigging = true;
                me.sendMessage("Break blocks now.");            
                return true;
            }
            if (label.equalsIgnoreCase("finishpit")) {
                pitdigging = false;
                down = true;
                me.sendMessage("Diging pit now.");
                digPit(sender, arguments, broke, down);
                return true;
            }
            if (label.equalsIgnoreCase("pitup")) {
                pitdigging = false;
                down = false;
                me.sendMessage("Diging up now.");
                digPit(sender, arguments, broke, down);
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
    public void onBreakBlockEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        if (!pitdigging) {
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
        
        broke.add(loc);
        
    }
    private void digPit(CommandSender sender, String[] arguments, ArrayList<Loc> broke, boolean down) {
        if (down) {
            for (Loc loc: broke) {
                digMoreBlocks(loc, arguments, broke);
            }
        }
        else {
            for (Loc loc: broke) {
                breakBlocksUp(loc, arguments, broke);
            }
        }
        broke.removeAll(broke);
        
    }
    private void digMoreBlocks(Loc loc, String[] arguments, ArrayList<Loc> broke) {
        double depth = 10;
        double X = loc.getX();
        double Y = loc.getY();
        double Z = loc.getZ();
        Location here = new Location(world,X,Y,Z);
        Block block = here.getBlock();
        if (arguments.length > 0) {
            try {
                depth = Double.parseDouble(arguments[0]);
            }
            catch (NumberFormatException exception) {                
            }            
        }
        for (double i = 0; i < depth; i ++) {
            Location current = new Location(world, X, (Y-i), Z);
            Block build = current.getBlock();
            build.breakNaturally();
        }
        
        
        
    }
    
private void breakBlocksUp(Loc loc, String[] arguments, ArrayList<Loc> broke) {
        double depth = 10;
        double X = loc.getX();
        double Y = loc.getY();
        double Z = loc.getZ();
        Location here = new Location(world,X,Y,Z);
        Block block = here.getBlock();
        if (arguments.length > 0) {
            try {
                depth = Double.parseDouble(arguments[0]);
            }
            catch (NumberFormatException exception) {                
            }            
        }
        for (double i = 0; i < depth; i ++) {
            Location current = new Location(world, X, (Y+i), Z);
            Block build = current.getBlock();
            build.breakNaturally();
        }
        
        
        
    }

}