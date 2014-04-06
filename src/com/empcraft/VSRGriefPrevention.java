package com.empcraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;




public class VSRGriefPrevention implements Listener {
	VoxelSniperRegions plugin;
	Plugin griefprevention;
	public VSRGriefPrevention(Plugin griefpreventionPlugin, VoxelSniperRegions p3) {
		griefprevention = griefpreventionPlugin;
    	plugin = p3;
    	
    }
	public Location[] getcuboid(Player player) {
		Location[] toreturn = new Location[2];
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
		if (claim!=null) {
			if (claim.getOwnerName().equalsIgnoreCase(player.getName())) {
				claim.getGreaterBoundaryCorner().getBlockX();
				toreturn[0] = new Location(player.getWorld(), claim.getGreaterBoundaryCorner().getBlockX(), claim.getGreaterBoundaryCorner().getBlockY(), claim.getGreaterBoundaryCorner().getBlockZ());
				toreturn[1] = new Location(player.getWorld(), claim.getLesserBoundaryCorner().getBlockX(), claim.getLesserBoundaryCorner().getBlockY(), claim.getLesserBoundaryCorner().getBlockZ());
				return toreturn;
			}
		}
		return null;
		
	}
	public String getid(Player player) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
		if (claim==null) {
			return null;
		}
		return "CLAIM:"+claim.toString();
	}

}

