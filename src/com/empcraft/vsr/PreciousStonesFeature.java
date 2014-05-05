package com.empcraft.vsr;

import java.util.List;

import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.vectors.Field;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;



public class PreciousStonesFeature implements Listener {
	VoxelSniperRegions plugin;
	Plugin preciousstones;
	public PreciousStonesFeature(Plugin preciousstonesPlugin, VoxelSniperRegions p3) {
		preciousstones = preciousstonesPlugin;
    	plugin = p3;
    	
    }
	public VoxelMask getMask(Player player,Location location) {
		List<Field> fields = PreciousStones.API().getFieldsProtectingArea(FieldFlag.PLOT, location);
		for (final Field myfield:fields) {
			if (myfield.getOwner().equalsIgnoreCase(player.getName())||(myfield.getAllowed().contains(player.getName()))) {
				Location pos1 = new Location(location.getWorld(), myfield.getCorners().get(0).getBlockX(),myfield.getCorners().get(0).getBlockY(),myfield.getCorners().get(0).getBlockZ());
				Location pos2 = new Location(location.getWorld(), myfield.getCorners().get(1).getBlockX(),myfield.getCorners().get(1).getBlockY(),myfield.getCorners().get(1).getBlockZ());
				return new VoxelMask(pos1, pos2) {
					@Override
					public String getName() {
						return "FIELD:"+myfield.toString();
					}
				};
			}
		}
		return null;
	}
}

