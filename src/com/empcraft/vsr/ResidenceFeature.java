package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;



public class ResidenceFeature implements Listener {
	VoxelSniperRegions plugin;
	Plugin residence;
	public ResidenceFeature(Plugin residencePlugin, VoxelSniperRegions p3) {
		residence = residencePlugin;
    	plugin = p3;
    	
    }
	public VoxelMask getMask(Player player, Location location) {
		final ClaimedResidence residence = Residence.getResidenceManager().getByLoc(location);
		if (residence!=null) {
			if (residence.getPlayersInResidence().contains(player)) {
				CuboidArea area = residence.getAreaArray()[0];
				Location pos1 = area.getHighLoc();
				Location pos2 = area.getLowLoc();
				return new VoxelMask(pos1, pos2) {
					@Override
					public String getName() {
						return "RESIDENCE: " + residence.getName();
					}
				};
			}
		}
		return null;
	}
}

