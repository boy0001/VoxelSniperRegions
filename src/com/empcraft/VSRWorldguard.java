package com.empcraft;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;



public class VSRWorldguard implements Listener {
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
	public VSRWorldguard(Plugin p2,VoxelSniperRegions p3) {
		worldedit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
    	worldguard = getWorldGuard();
    	plugin = p3;
    	
    }
	public ProtectedRegion isowner(Player player) {
		com.sk89q.worldguard.LocalPlayer localplayer = worldguard.wrapPlayer(player);
		ApplicableRegionSet regions = worldguard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
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
	public Location[] getcuboid(Player player) {
		Location[] toreturn = new Location[2];
		ProtectedRegion myregion = isowner(player);
		if (myregion!=null) {
			CuboidRegion cuboid = new CuboidRegion(myregion.getMinimumPoint(), myregion.getMaximumPoint());
			toreturn[0] = new Location(player.getWorld(),myregion.getMinimumPoint().getBlockX(),myregion.getMinimumPoint().getBlockY(),myregion.getMinimumPoint().getBlockZ());
			toreturn[1] = new Location(player.getWorld(),myregion.getMaximumPoint().getBlockX(),myregion.getMaximumPoint().getBlockY(),myregion.getMaximumPoint().getBlockZ());
			return toreturn;
		}
		else {
			return null;
		}
		
		
	}
	public String getid(Player player) {
		
		ProtectedRegion myregion = isowner(player);
		if (myregion!=null) {
			return myregion.getId();
		}
		else {
			return null;
		}
	}

}

