package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class VoxelMask
{
	private String description = null;
	private Location position1;
	private Location position2;
    public VoxelMask(Location pos1,Location pos2,String id)
    {
    	description = id;
    	position1 = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    	position2 = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    public VoxelMask(Location pos1,Location pos2)
    {
    	position1 = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    	position2 = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    public String getName()
    {
    	return description;
    }
    public Location getLowerBound() {
    	return position1;
    }
    public Location getUpperBound() {
    	return position2;
    }
    public void setBounds(Location pos1,Location pos2) {
    	position1 = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    	position2 = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    public Location[] getBounds() {
    	Location[] locations = {position1,position2};
    	return locations;
    }
}
