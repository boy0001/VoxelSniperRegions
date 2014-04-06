package com.empcraft;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;

public class VSRSniperManager {
	public Sniper sniper;
	public VSRSniperManager() {
    }
	public String tool(Player player,VoxelSniper voxelsniper) {
		sniper = voxelsniper.getInstance().getSniperManager().getSniperForPlayer(player);
		return sniper.getCurrentToolId();
    }
	public void undo(Player player,VoxelSniper voxelsniper) {
		sniper = voxelsniper.getInstance().getSniperManager().getSniperForPlayer(player);
		try {
		Sniper sniper = voxelsniper.getInstance().getSniperManager().getSniperForPlayer(player);
		sniper.undo();
		}
		catch (Exception e) {
			
		}
	}
}
