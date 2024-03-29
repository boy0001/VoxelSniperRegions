package com.empcraft.vsr;

import org.bukkit.Location;

public class VoxelMask
{
	private String description = null;
	private Location position1;
	private Location position2;
    public VoxelMask(Location pos1,Location pos2,String id)
    {
    	if (pos1==null||pos2==null) {
    		throw new IllegalArgumentException("Locations cannot be null!");
    	}
    	if (pos1.getWorld().equals(pos2.getWorld())==false) {
    		throw new IllegalArgumentException("Locations must be in the same world!");
    	}
    	description = id;
    	position1 = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    	position2 = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    public VoxelMask(Location pos1,Location pos2)
    {
    	if (pos1==null||pos2==null) {
    		throw new IllegalArgumentException("Locations cannot be null!");
    	}
    	if (pos1.getWorld().equals(pos2.getWorld())==false) {
    		throw new IllegalArgumentException("Locations must be in the same world!");
    	}
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
    	if (pos1==null||pos2==null) {
    		throw new IllegalArgumentException("Locations cannot be null!");
    	}
    	if (pos1.getWorld().equals(pos2.getWorld())==false) {
    		throw new IllegalArgumentException("Locations must be in the same world!");
    	}
    	position1 = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    	position2 = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    public Location[] getBounds() {
    	Location[] locations = {position1,position2};
    	return locations;
    }
    public boolean contains(Location loc) {
		if (position1.getWorld().equals(loc.getWorld())) {
			if (loc.getBlockX() < position1.getBlockX()) { return false; }
			if (loc.getBlockX() > position2.getBlockX()) { return false; }
			if (loc.getBlockZ() < position1.getBlockZ()) { return false; }
			if (loc.getBlockZ() > position2.getBlockZ()) { return false; }
			if (loc.getBlockY() < position1.getBlockY()) { return false; }
			if (loc.getBlockY() > position2.getBlockY()) { return false; }
		}
		else {
			return false;
		}
		return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.hashCode() != hashCode()) {
            return false;
        }

        if (obj instanceof VoxelMask) {
            return ((VoxelMask) obj).getLowerBound().equals(getLowerBound()) && ((VoxelMask) obj).getUpperBound().equals(getUpperBound());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        if (description == null) {
            return 0;
        }
        return description.hashCode();
    }
}
