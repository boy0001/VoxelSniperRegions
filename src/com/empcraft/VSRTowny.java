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

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.PlayerCache;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;


public class VSRTowny implements Listener {
	VoxelSniperRegions plugin;
	Plugin towny;
	public VSRTowny(Plugin townyPlugin, VoxelSniperRegions p3) {
		towny = townyPlugin;
    	plugin = p3;
    	
    }
	public Location[] getcuboid(Player player) {
		Location[] toreturn = new Location[2];
		
		
		
		
		try {
			PlayerCache cache = ((Towny) towny).getCache(player);
			WorldCoord mycoord = cache.getLastTownBlock();
			if (mycoord==null) {
				return null;
			}
			else {
				TownBlock myplot = mycoord.getTownBlock();
				if (myplot==null) {
					return null;
				}
				else {
					try {
					if (myplot.getResident().getName().equals(player.getName())) {
						Chunk chunk = player.getLocation().getChunk();
						toreturn[0] = new Location(player.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
						toreturn[1] = new Location(player.getWorld(), (chunk.getX() * 16) + 15, 156, (chunk.getZ() * 16)+15);
						return toreturn;
					}
					}
					catch (Exception e) {
						
					}
					if (plugin.checkperm(player, "wrg.towny.*")) {
						if (myplot.getTown().hasResident(player.getName())) {
							Chunk chunk = player.getLocation().getChunk();
							toreturn[0] = new Location(player.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
							toreturn[1] = new Location(player.getWorld(), (chunk.getX() * 16) + 15, 156, (chunk.getZ() * 16)+15);
							return toreturn;
						}
					}
					else if (myplot.getTown().isMayor(TownyUniverse.getDataSource().getResident(player.getName()))) {
						Chunk chunk = player.getLocation().getChunk();
						toreturn[0] = new Location(player.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
						toreturn[1] = new Location(player.getWorld(), (chunk.getX() * 16) + 15, 156, (chunk.getZ() * 16)+15);
						return toreturn;
					}
				}
			}
			}
			catch (Exception e) {
			}
			return null;
			
			
		
		
	}
	public String getid(Player player) {
		return "PLOT:"+player.getLocation().getChunk().getX()+","+player.getLocation().getChunk().getZ();
	}

}

