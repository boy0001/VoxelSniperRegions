package com.empcraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;



public class VSRResidence implements Listener {
	VoxelSniperRegions plugin;
	Plugin residence;
	public VSRResidence(Plugin residencePlugin, VoxelSniperRegions p3) {
		residence = residencePlugin;
    	plugin = p3;
    	
    }
	public Location[] getcuboid(Player player) {
		Location[] toreturn = new Location[2];
		
		ClaimedResidence residence = Residence.getResidenceManager().getByLoc(player.getLocation());
		if (residence!=null) {
			if (residence.getPlayersInResidence().contains(player)) {
				String resname = residence.getName();
				CuboidArea area = residence.getAreaArray()[0];
				Location pos1 = area.getHighLoc();
				Location pos2 = area.getLowLoc();
				toreturn[0] = new Location(player.getWorld(), pos2.getBlockX(),pos2.getBlockY(),pos2.getBlockZ());
				toreturn[1] = new Location(player.getWorld(), pos1.getBlockX(),pos1.getBlockY(),pos1.getBlockZ());
				return toreturn;
			}
		}
		return null;
	}
	public String getid(Player player) {
		ClaimedResidence residence = Residence.getResidenceManager().getByLoc(player.getLocation());
		if (residence!=null) {
			return "RESIDENCE: " + residence.getName();
		}
		return null;
	}

}

