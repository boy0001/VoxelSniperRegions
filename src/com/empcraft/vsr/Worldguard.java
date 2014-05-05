package com.empcraft.vsr;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;



public class Worldguard implements Listener {
	WorldGuardPlugin worldguard;
	VoxelSniperRegions plugin;
	WorldEditPlugin worldedit;
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	public Worldguard(Plugin p2,VoxelSniperRegions p3) {
		worldedit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
    	worldguard = getWorldGuard();
    	plugin = p3;
    	
    }
	public ProtectedRegion isowner(Player player, Location location) {
		com.sk89q.worldguard.LocalPlayer localplayer = worldguard.wrapPlayer(player);
		ApplicableRegionSet regions = worldguard.getRegionManager(player.getWorld()).getApplicableRegions(location);
		for (ProtectedRegion region:regions) {
			if (region.isOwner(localplayer)) {
				return region;
			}
			else if (region.getId().toLowerCase().equals(player.getName().toLowerCase())) {
				return region;
			}
			else if (region.getId().toLowerCase().contains(player.getName().toLowerCase()+"//")) {
				return region;
			}
			else if (region.isOwner("*")) {
				return region;
			}
		}
		return null;
	}
	public ProtectedRegion getregion(Player player,BlockVector location) {
		com.sk89q.worldguard.LocalPlayer localplayer = worldguard.wrapPlayer(player);
		ApplicableRegionSet regions = worldguard.getRegionManager(player.getWorld()).getApplicableRegions(location);
		for (ProtectedRegion region:regions) {
			if (region.isOwner(localplayer)) {
				return region;
			}
			else if (region.getId().toLowerCase().equals(player.getName().toLowerCase())) {
				return region;
			}
			else if (region.getId().toLowerCase().contains(player.getName().toLowerCase()+"//")) {
				return region;
			}
			else if (region.isOwner("*")) {
				return region;
			}
		}
		return null;
	}
	public VoxelMask getMask(Player player, Location location) {
		final ProtectedRegion myregion = isowner(player,location);
		if (myregion!=null) {
			Location pos1 = new Location(location.getWorld(),myregion.getMinimumPoint().getBlockX(),myregion.getMinimumPoint().getBlockY(),myregion.getMinimumPoint().getBlockZ());
			Location pos2 = new Location(location.getWorld(),myregion.getMaximumPoint().getBlockX(),myregion.getMaximumPoint().getBlockY(),myregion.getMaximumPoint().getBlockZ());
			return new VoxelMask(pos1, pos2) {
				@Override
				public String getName() {
					return myregion.getId();
				}
			};
		}
		else {
			return null;
		}
		
		
	}
}

