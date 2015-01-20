package com.empcraft.vsr;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;




public class GriefPreventionFeature implements Listener {
	VoxelSniperRegions plugin;
	Plugin griefprevention;
	public GriefPreventionFeature(Plugin griefpreventionPlugin, VoxelSniperRegions p3) {
		griefprevention = griefpreventionPlugin;
    	plugin = p3;
    }
	public VoxelMask getMask(Player player,Location location) {
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
		if (claim!=null) {
			if (claim.getOwnerName().equalsIgnoreCase(player.getName())) {
				claim.getGreaterBoundaryCorner().getBlockX();
				Location pos1 = new Location(location.getWorld(), claim.getLesserBoundaryCorner().getBlockX(), 0, claim.getLesserBoundaryCorner().getBlockZ());
				Location pos2 = new Location(location.getWorld(), claim.getGreaterBoundaryCorner().getBlockX(), 256, claim.getGreaterBoundaryCorner().getBlockZ());
				return new VoxelMask(pos1, pos2) {
					@Override
					public String getName() {
						return "CLAIM:"+claim.toString();
					}
				};
			}
		}
		return null;
		
	}
}