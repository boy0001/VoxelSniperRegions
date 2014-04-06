package com.empcraft;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.thevoxelbox.voxelsniper.VoxelSniper;


public final class VoxelSniperRegions extends JavaPlugin implements Listener {
	final Map<Plugin, VSRRegion> regions = new HashMap<Plugin, VSRRegion>();
	VoxelSniperRegions plugin;
	VSRWorldguard wgf;
	VSRPlotMe pmf;
	VSRResidence rf;
	VSRGriefPrevention gpf;
	VSRFactions ff;
	VSRPreciousStones psf;
	VSRTowny tf;
	VoxelSniper voxelsniper;
	VSRSniperManager snipermanager;
	private boolean blockchat = true;
	private boolean toundo = false;
	private String lastmsg;
	private Player lastplayer;
//	private Material lastblock;
//	private Byte lastblockdata;
	private Map<String, Object> lastmask = new HashMap<String, Object>();
	private Map<String, String> id = new HashMap<String, String>();
	private Map<String, Boolean> lastregion = new HashMap<String, Boolean>();
//	private ArrayList<BlockState> blocklist = new ArrayList<BlockState>();
//	private ArrayList<Material> blockid = new ArrayList<Material>();
//	private ArrayList<Location> blockloc = new ArrayList<Location>();
	
	
	public String getmsg(String key) {
		File yamlFile = new File(getDataFolder(), getConfig().getString("language").toLowerCase()+".yml"); 
		YamlConfiguration.loadConfiguration(yamlFile);
		try {
			return colorise(YamlConfiguration.loadConfiguration(yamlFile).getString(key));
		}
		catch (Exception e){
			return "";
		}
	}
    boolean checkperm(Player player,String perm) {
    	boolean hasperm = false;
    	String[] nodes = perm.split("\\.");
    	String n2 = "";
    	if (player==null) {
    		return true;
    	}
    	else if (player.hasPermission(perm)) {
    		hasperm = true;
    	}
    	else if (player.isOp()==true) {
    		hasperm = true;
    	}
    	else {
    		for(int i = 0; i < nodes.length-1; i++) {
    			n2+=nodes[i]+".";
            	if (player.hasPermission(n2+"*")) {
            		hasperm = true;
            	}
    		}
    	}
		return hasperm;
    }
    private void msg(Player player,String mystring) {
    	if (mystring==null||mystring.equals("")) {
    		return;
    	}
    	if (player==null) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else if (player instanceof Player==false) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else {
    		player.sendMessage(colorise(mystring));
    	}

    }
    private String colorise(String mystring) {
    	String[] codes = {"&1","&2","&3","&4","&5","&6","&7","&8","&9","&0","&a","&b","&c","&d","&e","&f","&r","&l","&m","&n","&o","&k"};
    	for (String code:codes) {
    		mystring = mystring.replace(code, "§"+code.charAt(1));
    	}
    	return mystring;
    }
    
    public synchronized void addRegion(VSRRegion region) {
    	regions.put(region.getPlugin(), region);
    }
    public synchronized VSRRegion getRegion (String key) {
    	for (VSRRegion current:regions.values()) {
    		if (current.getKey().equals(key)) {
    			return current;
    		}
    	}
    	return null;
    }
    public synchronized VSRRegion getRegion (Plugin myplugin) {
    	return regions.get(myplugin);
    }
    public synchronized List<VSRRegion> getRegions() {
    	return new ArrayList<VSRRegion>(regions.values());
    }
    public synchronized List<Plugin> getRegionPlugins() {
    	return new ArrayList<Plugin>(regions.keySet());
    }
    public synchronized List<String> getRegionKeys() {
    	List<String> toreturn = new ArrayList<String>();
    	for (VSRRegion current:regions.values()) {
    		toreturn.add(current.getKey());
    	}
    	return toreturn;
    }
    public synchronized VSRRegion removeRegion (VSRRegion region) {
    	return regions.remove(region.getKey());
    }
    public synchronized VSRRegion removeRegion (Plugin myplugin) {
    	return regions.remove(myplugin);
    }
    public synchronized VSRRegion removeRegion (String key) {
    	for (VSRRegion current:regions.values()) {
    		if (current.getKey().equals(key)) {
    			return regions.remove(current.getKey());
    		}
    	}
    	return null;
    }
    
    
    
    
    
    
	@Override
	public void onDisable() {
    	this.reloadConfig();
    	this.saveConfig();
        msg(null,"&f&oThanks for using &aVoxelSniper Regionss&f by &dEmpire92&f!");
	}
	@Override
	public void onEnable(){
		plugin = this;
		getServer().getPluginManager().registerEvents(this, this);
		VoxelSniper voxelsniper = (VoxelSniper) getServer().getPluginManager().getPlugin("voxelsniper");
		msg(null,"&fRunning VS "+voxelsniper.getInstance().getDescription().getVersion());
		if (voxelsniper.getInstance().getDescription().getVersion().split("-")[0].startsWith("5.168")) {
			msg(null,"&c[WARNING] An older version of VoxelSniper has been detected. Some features may not work properly");
			VSRSniperManager snipermanager = new VSRSniperManager();
		}
		else {
			
		}
		

		
		Plugin worldguardPlugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if((worldguardPlugin != null) && worldguardPlugin.isEnabled()) {
        	wgf = new VSRWorldguard(worldguardPlugin,this);
    		addRegion(new VSRRegion(worldguardPlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return wgf.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return wgf.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(wgf,this);
            msg(null,"Plugin 'WorldGuard' found. Using it now.");
        } else {
            msg(null,"Plugin 'WorldGuard' not found. Worldguard features disabled.");
        }
        
        
		Plugin plotmePlugin = getServer().getPluginManager().getPlugin("PlotMe");
        if((plotmePlugin != null) && plotmePlugin.isEnabled()) {
        	pmf = new VSRPlotMe(plotmePlugin,this);
    		addRegion(new VSRRegion(plotmePlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return pmf.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return pmf.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(pmf,this);
            msg(null,"Plugin 'PlotMe' found. Using it now.");
        } else {
            msg(null,"Plugin 'PlotMe' not found. PlotMe features disabled.");
        }
        
		Plugin townyPlugin = getServer().getPluginManager().getPlugin("Towny");
        if((townyPlugin != null) && townyPlugin.isEnabled()) {
        	tf = new VSRTowny(townyPlugin,this);
    		addRegion(new VSRRegion(townyPlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return tf.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return tf.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(tf,this);
            msg(null,"Plugin 'Towny' found. Using it now.");
        } else {
            msg(null,"Plugin 'Towny' not found. Towny features disabled.");
        }
        
        Plugin factionsPlugin = getServer().getPluginManager().getPlugin("Factions");
        if((factionsPlugin != null) && factionsPlugin.isEnabled()) {
        	ff = new VSRFactions(factionsPlugin,this);
    		addRegion(new VSRRegion(factionsPlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return ff.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return ff.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(ff,this);
            msg(null,"Plugin 'Factions' found. Using it now.");
        } else {
            msg(null,"Plugin 'Factions' not found. Factions features disabled.");
        }
        Plugin residencePlugin = getServer().getPluginManager().getPlugin("Residence");
        if((residencePlugin != null) && residencePlugin.isEnabled()) {
        	rf = new VSRResidence(residencePlugin,this);
    		addRegion(new VSRRegion(residencePlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return rf.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return rf.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(rf,this);
            msg(null,"Plugin 'Residence' found. Using it now.");
        } else {
            msg(null,"Plugin 'Residence' not found. Factions features disabled.");
        }
        Plugin griefpreventionPlugin = getServer().getPluginManager().getPlugin("GriefPrevention");
        if((griefpreventionPlugin != null) && griefpreventionPlugin.isEnabled()) {
        	gpf = new VSRGriefPrevention(griefpreventionPlugin,this);
    		addRegion(new VSRRegion(griefpreventionPlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return gpf.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return gpf.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(gpf,this);
            msg(null,"Plugin 'GriefPrevention' found. Using it now.");
        } else {
            msg(null,"Plugin 'GriefPrevention' not found. GriefPrevention features disabled.");
        }
        
        Plugin preciousstonesPlugin = getServer().getPluginManager().getPlugin("PreciousStones");
        if((preciousstonesPlugin != null) && preciousstonesPlugin.isEnabled()) {
        	psf = new VSRPreciousStones(preciousstonesPlugin,this);
    		addRegion(new VSRRegion(preciousstonesPlugin,this) {
    			@Override
    			public String getid(Player player) {
    				return psf.getid(player);
    			}
    			
    			@Override
    			public Location[] getcuboid(Player player) {
    				return psf.getcuboid(player);
    			}
    		});
            getServer().getPluginManager().registerEvents(psf,this);
            msg(null,"Plugin 'PreciousStones' found. Using it now.");
        } else {
            msg(null,"Plugin 'PreciousStones' not found. PreciousStones features disabled.");
        }
        saveResource("english.yml", true);
        getConfig().options().copyDefaults(true);
        final Map<String, Object> options = new HashMap<String, Object>();
        getConfig().set("version", "0.2.0");
        options.put("language","english");
        for (final Entry<String, Object> node : options.entrySet()) {
        	 if (!getConfig().contains(node.getKey())) {
        		 getConfig().set(node.getKey(), node.getValue());
        	 }
        }
    	saveConfig();
    	this.saveDefaultConfig();
    	getServer().getPluginManager().registerEvents(this, this);   
		for (Player player:Bukkit.getOnlinePlayers()) {
    		lastmask.put(player.getName(),"~NULL");
    		id.put(player.getName(),"~NULL");
    		lastregion.put(player.getName(),false);
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if (cmd.getName().equalsIgnoreCase("vsr")) {
    		Player player;
    		if (sender instanceof Player==false) {
    			player = null;
    			msg(player,getmsg("MSG0"));
    			return false;
    		}
    		else {
    			player = (Player) sender;
    			msg(player,getmsg("CREDITS"));
    		}
    	}
    	return false;
	}
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		lastmask.put(player.getName(),"~NULL");
		id.put(player.getName(),"~NULL");
		lastregion.put(player.getName(),false);
	}
	@EventHandler(priority=EventPriority.LOWEST)
	private void onPlayerInteract(PlayerInteractEvent event) {
		toundo = false;
//		lastblock = event.getPlayer().getTargetBlock(null, 256).getType();
//		lastblockdata = event.getPlayer().getTargetBlock(null, 256).getData();
		Player player = event.getPlayer();
		ItemStack helditem = player.getItemInHand();
		if (checkperm(player,"voxelsniper.sniper")||checkperm(player,"voxelsniper.litesniper")) {
			if (checkperm(player,"vsr.bypass")==false) {
				if (helditem.getType().equals(Material.ARROW)) {
					lastplayer = player;
				}
				else if (helditem.getType().equals(Material.SULPHUR)) {
					lastplayer = player;
				}
				else {
					try {
						if (snipermanager!=null) {
							if ((snipermanager.tool(player, voxelsniper)+"").equals("null")!=true) {
								lastplayer = player;
							}
						}
						else {
							
						}
					}
					catch (Exception e) {
					}
				}
			}
		}
		
//		voxelsniper.sniper: This user has Sniper rank.
//		voxelsniper.litesniper: This user has LiteSniper rank.
//		voxelsniper.punish: This user has access to the punish brush.

	}
	@EventHandler(priority=EventPriority.MONITOR)
	private void onPlayerInteract2(PlayerInteractEvent event) {
		if (lastplayer!=null) {
			if (toundo) {
				try {
					if (snipermanager!=null) {
						snipermanager.undo(event.getPlayer(), voxelsniper);
					}
					else {
						Bukkit.getServer().dispatchCommand(event.getPlayer(), "u");
					}
				}
				catch (Exception e) {
					Bukkit.getServer().dispatchCommand(event.getPlayer(), "u");
				}
			}
		}
		lastplayer = null;
		lastmsg = null;
	}
	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
		lastmask.remove(event.getPlayer().getName());
		id.remove(event.getPlayer().getName());
		lastregion.remove(event.getPlayer().getName());
	}
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		try {setmask(event.getPlayer()); } catch (Exception e) {  }
	}
	@EventHandler
	private void onPlayerPortal(PlayerPortalEvent event) {
		if (lastmask.containsKey(event.getPlayer().getName())) {
			Object last = lastmask.get(event.getPlayer().getName());
			if (last instanceof Location[]) {
				if ((((Location[]) last)[0]).getWorld().equals(event.getPlayer().getWorld())==false) {
		    		lastmask.put(event.getPlayer().getName(),"~NULL");
		    		id.put(event.getPlayer().getName(),"~NULL");
		    		lastregion.put(event.getPlayer().getName(),false);
				}
			}
		else {
			
		}
		}
	}
	@EventHandler
	private void onPlayerTeleport(PlayerTeleportEvent event) {
		if (lastmask.containsKey(event.getPlayer().getName())) {
			Object last = lastmask.get(event.getPlayer().getName());
			if (last instanceof Location[]) {
				if ((((Location[]) last)[0]).getWorld().equals(event.getPlayer().getWorld())==false) {
		    		lastmask.put(event.getPlayer().getName(),"~NULL");
		    		id.put(event.getPlayer().getName(),"~NULL");
		    		lastregion.put(event.getPlayer().getName(),false);
				}
			}
		else {
			
		}
		}
	}
	
	private boolean isin(Location[] locs,Location loc){
		if (locs[0].getWorld().equals(loc.getWorld())) {
			if (loc.getBlockX() < locs[0].getBlockX()-1) { return false; }
			if (loc.getBlockX() > locs[1].getBlockX()+1) { return false; }
			if (loc.getBlockZ() < locs[0].getBlockZ()-1) { return false; }
			if (loc.getBlockZ() > locs[1].getBlockZ()+1) { return false; }
			if (loc.getBlockY() < locs[0].getBlockY()-1) { return false; }
			if (loc.getBlockY() > locs[1].getBlockY()+1) { return false; }
		}
		else {
			return false;
		}
		return true;
	}
	private boolean isin2(Location[] locs,Location loc){
		if (locs[0].getWorld().equals(loc.getWorld())) {
			if (loc.getBlockX() < locs[0].getBlockX()) { return false; }
			if (loc.getBlockX() > locs[1].getBlockX()) { return false; }
			if (loc.getBlockZ() < locs[0].getBlockZ()) { return false; }
			if (loc.getBlockZ() > locs[1].getBlockZ()) { return false; }
			if (loc.getBlockY() < locs[0].getBlockY()) { return false; }
			if (loc.getBlockY() > locs[1].getBlockY()) { return false; }
		}
		else {
			return false;
		}
		return true;
	}
	private void setmask(Player player) {
		if (checkperm(player,"vsr.bypass")==false) {
			Location[] mymask = null;
			String myid = "";
			for (VSRRegion current:regions.values()) {
				if (mymask==null) {
					if (checkperm(player,"vsr."+current.getKey())) {
						mymask = current.getcuboid(player);
						myid = current.getid(player);
					}
				}
			}
			if (wgf!=null&&(checkperm(player,"vsr.worldguard"))) {
				mymask = wgf.getcuboid(player);
				myid = wgf.getid(player);
			}
			if (pmf!=null&&mymask==null&&(checkperm(player,"vsr.plotme"))) {
				mymask = pmf.getcuboid(player);
				myid = pmf.getid(player);
			}
			if ((gpf!=null&&mymask==null)&&(checkperm(player,"vsr.griefprevention"))) {
				mymask = gpf.getcuboid(player);
				myid = gpf.getid(player);
			}
			if ((rf!=null&&mymask==null)&&(checkperm(player,"vsr.residence"))) {
				mymask = rf.getcuboid(player);
				myid = rf.getid(player);
			}
			if ((psf!=null&&mymask==null)&&(checkperm(player,"vsr.preciousstones"))) {
				mymask = psf.getcuboid(player);
				myid = psf.getid(player);
			}
			if ((tf!=null&&mymask==null)&&(checkperm(player,"vsr.towny"))) {
				mymask = tf.getcuboid(player);
				myid = tf.getid(player);
			}
			if ((ff!=null&&mymask==null)&&(checkperm(player,"vsr.factions"))) {
				mymask = ff.getcuboid(player);
				myid = ff.getid(player);
			}
			Object test = ff;
			if (mymask != null) {
				try {
				if ((id.get(player.getName()).equals(myid))==false) {
					msg(player,getmsg("MSG5")+" &a"+myid+"&7.");
					lastmask.put(player.getName(),mymask);
					id.put(player.getName(),myid);
					lastregion.put(player.getName(),true);
				}
				else {
					if (checkperm(player,"vsr.notify.greeting")) {
						if (lastregion.containsKey(player.getName())) {
							if (lastregion.get(player.getName())==false) {
								msg(player,getmsg("GREETING"));
							}
						}
					}
					lastregion.put(player.getName(),true);
				}
				}
				catch (Exception e) {
					
				}
			}
			else {
				if (lastmask.get(player.getName())!=null) {
					if (lastmask.get(player.getName()).equals("~NULL")==false) {
					Location[] locs = (Location[]) lastmask.get(player.getName());
					if (isin2(locs,player.getLocation())) {
			    		lastmask.put(player.getName(),"~NULL");
			    		id.put(player.getName(),"~NULL");
			    		lastregion.put(player.getName(),false);
						if (checkperm(player,"vsr.notify")) {
							msg(player,getmsg("MSG1")+"&7.");
						}
					}
				}
				}
				if (checkperm(player,"vsr.notify.farewell")) {
					if (lastregion.containsKey(player.getName())) {
						if (lastregion.get(player.getName())==true) {
							msg(player,getmsg("FAREWELL"));
						}
					}
				}
				lastregion.put(player.getName(),false);
			}
		}
		else {
    		lastmask.put(player.getName(),"~NULL");
    		id.put(player.getName(),"~NULL");
    		lastregion.put(player.getName(),false);
		}
//		lastmask must contains
//		 - LOCATION1 (contains world)
//		 - LOCATION2
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] args = event.getMessage().split(" ");
		Player player = event.getPlayer();
		if (checkperm(player,"vsr.bypass")) {
			return;
		}
		
		if (event.getMessage().startsWith("/btool")) {
			if (snipermanager==null) {
				msg(player,"&c[Warning] Update VoxelSniper to 5.169 to use this command.");
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
    private void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		if (lastplayer!=null) {
			Location loc = new Location(lastplayer.getWorld(),event.getBlock().getX(),event.getBlock().getY(),event.getBlock().getZ());
			if (lastmask.containsKey(lastplayer.getName())) {
				try {
				Location[] locs = (Location[]) lastmask.get(lastplayer.getName());
				if (isin(locs,loc)) {
					return;
				}
				else {
					if (lastmsg==null) {
						lastmsg = getmsg("MSG15");
						msg(lastplayer,lastmsg);
					}
					toundo=true;
					return;
				}
				}
				catch (Exception e) {
					if (lastmsg==null) {
						lastmsg = getmsg("MSG1");
						msg(lastplayer,lastmsg);
					}
					toundo=true;
					return;
				}
			}
			else {
				if (lastmsg==null) {
					lastmsg = getmsg("MSG15");
					msg(lastplayer,lastmsg);
				}
				toundo=true;
				return;
			}
		}
		else {
		}
    }
}
