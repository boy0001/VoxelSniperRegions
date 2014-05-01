package com.empcraft.vsr;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class ProtocolIn {
	VoxelSniperRegions VSR;
	public ProtocolIn(VoxelSniperRegions plugin) {
		VSR = plugin;
		ProtocolLibrary.getProtocolManager().addPacketListener(
	    new PacketAdapter(VSR, PacketType.Play.Client.BLOCK_PLACE) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				VSR.setCheck(true);
				Player player = event.getPlayer();
				ItemStack helditem = player.getItemInHand();
				if (VSR.CheckPerm(player,"voxelsniper.sniper")||VSR.CheckPerm(player,"voxelsniper.litesniper")) {
					if (VSR.CheckPerm(player,"vsr.bypass")==false) {
						boolean isVoxel = false;
						if (VSR.sniperManager!=null) {
							if (helditem.getType().equals(Material.ARROW)) {
								isVoxel = true;
							}
							else if (helditem.getType().equals(Material.SULPHUR)) {
								isVoxel = true;
							}
							else if ((VSR.sniperManager.tool(player, VSR.voxelsniper)+"").equals("null")!=true) {
								isVoxel = true;
							}
							if (isVoxel) {
								int radius = VSR.sniperManager.radius(player, VSR.voxelsniper);
								if (VSR.getConfig().getInt("cooldown-brush-size")<=radius) {
									if (VSR.CheckPerm(player,"vsr.cooldown.bypass")==false) {
										Long currentTime = System.currentTimeMillis();
										if (VSR.coolDown.containsKey(player.getName())) {
											if (currentTime<VSR.coolDown.get(player.getName())) {
												VSR.Msg(player,VSR.GetMsg("MSG3"));
												event.setCancelled(true);
												return;
											}
										}
										
									}
								}
								Block block = player.getTargetBlock(null, 256);
								if (block==null) {
									VSR.Msg(player,VSR.GetMsg("MSG2"));
									event.setCancelled(true);
									return;
								}
								Location location = block.getLocation();
								if (VSR.lastMask.containsKey(player.getName())) {
									Location[] locs;
									try {
										locs = (Location[]) VSR.lastMask.get(player.getName());
									}
									catch (Exception e) {
										VSR.Msg(player,VSR.GetMsg("MSG1"));
										event.setCancelled(true);
										return;
									}
									Location pos1 = new Location(player.getWorld(), location.getBlockX()+radius, location.getBlockY()+radius, location.getBlockZ()+radius);
									Location pos2 = new Location(player.getWorld(), location.getBlockX()-radius, location.getBlockY()-radius, location.getBlockZ()-radius);
									if (VSR.isIn(locs, pos1)) {
										if (VSR.isIn(locs, pos2)) {
											//TODO CHECK IF BRUSH IS SAFE
											if (true) { //IF WHITELISTED
												VSR.setCheck(false);
												return;
											}
//											else {
//												return;
//											}
										}
									}
									VSR.Msg(player,VSR.GetMsg("MSG15"));
									event.setCancelled(true);
									return;
								}
								else {
									VSR.Msg(player,VSR.GetMsg("MSG1"));
									event.setCancelled(true);
									return;
								}
							}
						}
					}
				}
			}
		});
	}
}