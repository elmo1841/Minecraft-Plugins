package com.javaminecraft;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicBridge extends JavaPlugin implements Listener {
    Player me;
    World world;
    Location spot;
    boolean bridgeing = false;
    boolean stepping = false;
    ArrayList<Block> blocks = new ArrayList<>();
   
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        me = (Player) sender;
        world = me.getWorld();
        spot = me.getLocation();
        
        
        if (sender instanceof Player) {
            if (label.equalsIgnoreCase("magicbridge")) {
                bridgeing = true;
                me.sendMessage("Bridgeing on");
                return true;
            }
            if (label.equalsIgnoreCase("steps")) {
                me.sendMessage("Watch your step");
                stepping = true;
                return true;
            }
            if (label.equalsIgnoreCase("stepsoff")) {
                stepping = false;
                me.sendMessage("Step Off!");
                return true;
            }
            if (label.equalsIgnoreCase("bridgeoff")) {
                bridgeing = false;               
                me.sendMessage("Bridgeing off");
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
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
         Player player = event.getPlayer();
               
        if (!bridgeing) {
            return;
        }
        if (player != me) {
            return;
        }
       
        Location to = event.getTo();
        Block toBlock = to.getBlock();
        Block down = toBlock.getRelative(BlockFace.DOWN);
        //me.sendMessage("got it");
        
        if (stepping) {
            down = down.getRelative(BlockFace.DOWN);
        }
               
        if (airBlock(down)) {
            down.setType(Material.GLASS);
            blocks.add(down);
            if (blocks.size() > 1) {
                //me.sendMessage("if");
                breaking(blocks.get(0));
                blocks.remove(0);
                //me.sendMessage("done");
            }
        }            
    }
    
    public boolean airBlock(Block down) {
        if (down.getType() == Material.AIR) {
            return true;            
        }
        return false;
    }
    
    

    public void breaking(Block breaking) {
        breaking.breakNaturally();
        //me.sendMessage("break");
    }
}