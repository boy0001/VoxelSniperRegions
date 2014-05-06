package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class VoxelMaskManager
{
	private final String key;
	private final Plugin myplugin;
    public VoxelMaskManager(Plugin plugin)
    {
    	key = plugin.getName();
    	myplugin = plugin;
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
    
    public abstract VoxelMask getMask(Player player, Location location);
}