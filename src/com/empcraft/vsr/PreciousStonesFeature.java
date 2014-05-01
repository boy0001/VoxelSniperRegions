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
	public Location[] getcuboid(Player player) {
		Location[] toreturn = new Location[2];
		List<Field> fields = PreciousStones.API().getFieldsProtectingArea(FieldFlag.PLOT, player.getLocation());
		for (Field myfield:fields) {
			if (myfield.getOwner().equalsIgnoreCase(player.getName())) {
				toreturn[0] = new Location(player.getWorld(), myfield.getCorners().get(0).getBlockX(),myfield.getCorners().get(0).getBlockY(),myfield.getCorners().get(0).getBlockZ());
				toreturn[1] = new Location(player.getWorld(), myfield.getCorners().get(1).getBlockX(),myfield.getCorners().get(1).getBlockY(),myfield.getCorners().get(1).getBlockZ());
				return toreturn;
			}
		}
		return null;
	}
	public String getid(Player player) {
		List<Field> fields = PreciousStones.API().getFieldsProtectingArea(FieldFlag.PLOT, player.getLocation());
		for (Field myfield:fields) {
			if (myfield.getOwner().equalsIgnoreCase(player.getName())) {
				return "FIELD:"+myfield.toString();
			}
		}
		return null;
	}

}

