package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class VoxelMaskManager
{
	private final String key;
	private final Plugin myplugin;
//	private final VoxelSniperRegions myVSR;
  
    public VoxelMaskManager(Plugin plugin,VoxelSniperRegions VSR)
    {
    	key = plugin.getName();
    	myplugin = plugin;
//    	myVSR = VSR;
    }
    public String getKey()
    {
    	return this.key;
    }
    public String toString()
    {
    	return this.key;
    }
    public Plugin getPlugin() {
    	return myplugin;
    }
    
    public abstract Location[] getcuboid(Player player);
    
    public abstract String getid(Player player);
	
}