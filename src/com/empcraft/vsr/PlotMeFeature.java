package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;

public class PlotMeFeature implements Listener {
    VoxelSniperRegions plugin;
    PlotMe_Core plotme;

    public PlotMeFeature(Plugin plotmePlugin, VoxelSniperRegions p3) {
        plotme = ((PlotMe_CorePlugin) plotmePlugin).getAPI();
        plugin = p3;

    }

    public VoxelMask getMask(Player player, Location location) {

        final Plot plot = plotme.getPlotMeCoreManager().getPlotById(new BukkitPlayer(player));
        if (plot == null) {
            return null;
        }
        boolean isallowed = plot.isAllowed(player.getUniqueId());
        if (isallowed) {
            Location pos1 = new Location(location.getWorld(), plotme.getGenManager(player.getWorld().getName()).bottomX(plot.getId(),
                    new BukkitWorld(player.getWorld())), 0, plotme.getGenManager(player.getWorld().getName()).bottomZ(plot.getId(),
                    new BukkitWorld(player.getWorld())));
            Location pos2 = new Location(location.getWorld(), plotme.getGenManager(player.getWorld().getName()).topX(plot.getId(),
                    new BukkitWorld(player.getWorld())), 256, plotme.getGenManager(player.getWorld().getName()).topZ(plot.getId(),
                    new BukkitWorld(player.getWorld())));
            return new VoxelMask(pos1, pos2) {
                @Override
                public String getName() {
                    return plot.getId();
                }
            };
        }
        return null;
    }
}