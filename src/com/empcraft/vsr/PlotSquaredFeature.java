package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.MainUtil;
import com.intellectualcrafters.plot.util.bukkit.BukkitUtil;

public class PlotSquaredFeature implements Listener {
	VoxelSniperRegions plugin;
	public PlotSquaredFeature(Plugin plotPlugin, VoxelSniperRegions p3) {
		plugin = p3;
    }
	public VoxelMask getMask(Player player,final Location location) {
	    PlotPlayer pp = BukkitUtil.getPlayer(player);
		final Plot plot = MainUtil.getPlot(pp.getLocation());
		if (plot!=null) {
			boolean hasPerm = false;
			if (plot.owner!=null) {
				if (plot.owner.equals(pp.getUUID())) {
					hasPerm = true;
				}
				else if (plot.isAdded(pp.getUUID()) && pp.hasPermission("vsr.plotsquared.member")) {
					hasPerm = true;
				}
				if (hasPerm) {
				    World world = player.getWorld();
				    String worldname = world.getName();
					com.intellectualcrafters.plot.object.Location p1 = MainUtil.getPlotBottomLoc(worldname, plot.id).add(1,0,1);
					com.intellectualcrafters.plot.object.Location p2 = MainUtil.getPlotTopLoc(worldname, plot.id);
					
					Location pos1 = new Location(world, p1.getX(), 0, p1.getZ());
					Location pos2 = new Location(world, p2.getX(), 256, p2.getZ());
					
					
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