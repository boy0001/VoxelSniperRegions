package com.empcraft.vsr;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.ps.PS;

public class FactionsFeature implements Listener {
	VoxelSniperRegions plugin;
	Plugin factions;
	public FactionsFeature(Plugin factionsPlugin, VoxelSniperRegions p3) {
		factions = factionsPlugin;
    	plugin = p3;
    	
    }
	public VoxelMask getMask(Player player,final Location location) {
		Faction fac = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
		
		if (fac!=null) {
			if (fac.getOnlinePlayers().contains(player)) {
				if (fac.getComparisonName().equals("wilderness")==false) {
					Chunk chunk = location.getChunk();
					Location pos1 = new Location(location.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
					Location pos2 = new Location(location.getWorld(), (chunk.getX() * 16) + 15, 156, (chunk.getZ() * 16)+15);
					return new VoxelMask(pos1, pos2) {
						@Override
						public String getName() {
							return "CHUNK:"+location.getChunk().getX()+","+location.getChunk().getZ();
						}
					};
				}
			}
		}
		return null;
	}
}

