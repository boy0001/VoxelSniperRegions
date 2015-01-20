package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.PlotHelper;
import com.intellectualcrafters.plot.util.UUIDHandler;

public class PlotSquaredFeature implements Listener {
	VoxelSniperRegions plugin;
	public PlotSquaredFeature(Plugin plotPlugin, VoxelSniperRegions p3) {
		plugin = p3;
    }
	public VoxelMask getMask(Player player,final Location location) {
		final Plot plot = PlotHelper.getCurrentPlot(player.getLocation());
		if (plot!=null) {
			boolean hasPerm = false;
			if (plot.getOwner()!=null) {
				if (plot.getOwner().equals(UUIDHandler.uuidWrapper.getUUID(player))) {
					hasPerm = true;
				}
				else if (plot.hasRights(player) && plugin.CheckPerm(player, "wrg.plotsquared.member")) {
					hasPerm = true;
				}
				if (hasPerm) {
				    World world = player.getWorld();
					Location pos1 = PlotHelper.getPlotBottomLoc(world, plot.id).add(1,0,1);
					pos1.setY(0);
					Location pos2 = PlotHelper.getPlotTopLoc(world, plot.id);
					pos2.setY(256);
					return new VoxelMask(pos1, pos2) {
						@Override
						public String getName() {
							return "PLOT^2:"+plot.getId();
						}
					};
				}
			}
		}
		return null;
	}
}