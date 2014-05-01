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
	public Location[] getcuboid(Player player) {
		Location[] toreturn = new Location[2];
		Faction fac = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
		
		if (fac!=null) {
			if (fac.getOnlinePlayers().contains(player)) {
				if (fac.getComparisonName().equals("wilderness")==false) {
					Chunk chunk = player.getLocation().getChunk();
					toreturn[0] = new Location(player.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
					toreturn[1] = new Location(player.getWorld(), (chunk.getX() * 16) + 15, 156, (chunk.getZ() * 16)+15);
					return toreturn;
				}
			}
			else {
			}
			return null;
		}
		else {
			return null;
		}
	}
	public String getid(Player player) {
		return "CHUNK:"+player.getLocation().getChunk().getX()+","+player.getLocation().getChunk().getZ();
	}

}

