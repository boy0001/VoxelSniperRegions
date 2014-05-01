package com.empcraft.vsr;

import org.bukkit.entity.Player;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;

public class SniperManagerFeature {
	public Sniper sniper;
	public SniperManagerFeature() {
    }
	public String tool(Player player,VoxelSniper voxelsniper) {
		sniper = VoxelSniper.getInstance().getSniperManager().getSniperForPlayer(player);
		return sniper.getCurrentToolId();
    }
	public int radius(Player player,VoxelSniper voxelsniper) {
		sniper = VoxelSniper.getInstance().getSniperManager().getSniperForPlayer(player);
		return sniper.getSnipeData(sniper.getCurrentToolId()).getBrushSize();
	}
	public void undo(Player player,VoxelSniper voxelsniper) {
		sniper = VoxelSniper.getInstance().getSniperManager().getSniperForPlayer(player);
		try {
		Sniper sniper = VoxelSniper.getInstance().getSniperManager().getSniperForPlayer(player);
		sniper.undo();
		}
		catch (Exception e) {
			
		}
	}
}
