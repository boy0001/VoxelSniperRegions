package com.empcraft.vsr;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;

public class PlotMeFeature implements Listener {
	VoxelSniperRegions plugin;
	Plugin plotme;
	public PlotMeFeature(Plugin plotmePlugin, VoxelSniperRegions p3) {
		plotme = plotmePlugin;
    	plugin = p3;
    	
    }
	public VoxelMask getMask(Player player,Location location) {
		
		final Plot plotid = PlotManager.getPlotById(location);
		if (plotid==null) {
			return null;
		}
		boolean isallowed = PlotManager.getPlotById(location).isAllowed(player.getName());
		if (isallowed) {
			Location pos1 = new Location(location.getWorld(),PlotManager.bottomX(plotid.id,player.getWorld()),0,PlotManager.bottomZ(plotid.id,player.getWorld()));
			Location pos2 = new Location(location.getWorld(),PlotManager.topX(plotid.id,player.getWorld()),256,PlotManager.topZ(plotid.id,player.getWorld()));
			return new VoxelMask(pos1, pos2) {
				@Override
				public String getName() {
					return plotid.id;
				}
			};
		}
		return null;
	}
}

