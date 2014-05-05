package com.empcraft.vsr;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.PlayerCache;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;


public class TownyFeature implements Listener {
	VoxelSniperRegions plugin;
	Plugin towny;
	public TownyFeature(Plugin townyPlugin, VoxelSniperRegions p3) {
		towny = townyPlugin;
    	plugin = p3;
    }
	public VoxelMask getMask(Player player,final Location location) {
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
					boolean isMember = false;
					try {
					if (myplot.getResident().getName().equals(player.getName())) {
						isMember = true;
					}
					}
					catch (Exception e) {
						
					}
					if (!isMember) {
					if (plugin.CheckPerm(player, "wrg.towny.*")) {
						isMember = true;
					}
					else if (myplot.getTown().isMayor(TownyUniverse.getDataSource().getResident(player.getName()))) {
						isMember = true;
					}
					}
					if (isMember) {
						Chunk chunk = location.getChunk();
						Location pos1 = new Location(location.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
						Location pos2 = new Location(location.getWorld(), (chunk.getX() * 16) + 15, 156, (chunk.getZ() * 16)+15);
						return new VoxelMask(pos1, pos2) {
							@Override
							public String getName() {
								return "PLOT:"+location.getChunk().getX()+","+location.getChunk().getZ();
							}
						};
					}
				}
			}
			}
			catch (Exception e) {
			}
			return null;
	}
}