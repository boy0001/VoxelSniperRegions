package com.empcraft.vsr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
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
	volatile Map<Plugin, VoxelMaskManager> regions = new ConcurrentHashMap<Plugin, VoxelMaskManager>();
	volatile Map<String, Long> coolDown = new ConcurrentHashMap<String, Long>();
	volatile Map<String, VoxelMask> lastMask = new ConcurrentHashMap<String, VoxelMask>();
	volatile Map<String, Boolean> lastregion = new ConcurrentHashMap<String, Boolean>();
	VoxelSniperRegions plugin;
	ProtocolIn protocolin = null;
	Worldguard wgf;
	PlotMeFeature pmf;
	ResidenceFeature rf;
	GriefPreventionFeature gpf;
	FactionsFeature ff;
	PreciousStonesFeature psf;
	TownyFeature tf;
	VoxelSniper voxelsniper;
	SniperManagerFeature sniperManager;
	private boolean toUndo = false;
	private volatile boolean toCheck = true;
	volatile String lastMsg;
	private Player lastPlayer;
	synchronized void setCheck(boolean value) {
		toCheck = value;
	}
	synchronized boolean getCheck() {
		return toCheck;
	}
	synchronized void addCoolDown(String player,Long value) {
		coolDown.put(player,value);
	}
	public String GetMsg(String key) {
		File yamlFile = new File(getDataFolder(), getConfig().getString("language").toLowerCase()+".yml"); 
		YamlConfiguration.loadConfiguration(yamlFile);
		try {
			return Colorise(YamlConfiguration.loadConfiguration(yamlFile).getString(key));
		}
		catch (Exception e){
			return "";
		}
	}
    boolean CheckPerm(Player player,String perm) {
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
    synchronized void Msg(Player player,String mystring) {
    	if (mystring==null||mystring.equals("")) {
    		return;
    	}
    	if (player==null) {
    		getServer().getConsoleSender().sendMessage(Colorise(mystring));
    	}
    	else if (player instanceof Player==false) {
    		getServer().getConsoleSender().sendMessage(Colorise(mystring));
    	}
    	else {
    		player.sendMessage(Colorise(mystring));
    	}
    }
    private String Colorise(String mystring) {
    	String[] codes = {"&1","&2","&3","&4","&5","&6","&7","&8","&9","&0","&a","&b","&c","&d","&e","&f","&r","&l","&m","&n","&o","&k"};
    	for (String code:codes) {
    		mystring = mystring.replace(code, "§"+code.charAt(1));
    	}
    	return mystring;
    }
    public synchronized void addMaskManager(VoxelMaskManager region) {
    	regions.put(region.getPlugin(), region);
    }
    public synchronized VoxelMaskManager getMaskManager (String key) {
    	for (VoxelMaskManager current:regions.values()) {
    		if (current.getKey().equals(key)) {
    			return current;
    		}
    	}
    	return null;
    }
    public synchronized VoxelMaskManager getMaskManager (Plugin myplugin) {
    	return regions.get(myplugin);
    }
    public synchronized List<VoxelMaskManager> getRegions() {
    	return new ArrayList<VoxelMaskManager>(regions.values());
    }
    public synchronized List<Plugin> getRegionPlugins() {
    	return new ArrayList<Plugin>(regions.keySet());
    }
    public synchronized List<String> getRegionKeys() {
    	List<String> toreturn = new ArrayList<String>();
    	for (VoxelMaskManager current:regions.values()) {
    		toreturn.add(current.getKey());
    	}
    	return toreturn;
    }
    public synchronized VoxelMaskManager removeMaskManager (VoxelMaskManager region) {
    	return regions.remove(region.getKey());
    }
    public synchronized VoxelMaskManager removeMaskManager (Plugin myplugin) {
    	return regions.remove(myplugin);
    }
    public synchronized VoxelMaskManager removeMaskManager (String key) {
    	for (VoxelMaskManager current:regions.values()) {
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
        Msg(null,GetMsg("CREDITS"));
	}
	@Override
	public void onEnable(){
		Msg(null,"&8======&6VoxelSniperRegions&8======");
		plugin = this;
		getServer().getPluginManager().registerEvents(this, this);
		voxelsniper = (VoxelSniper) getServer().getPluginManager().getPlugin("voxelsniper");
		Msg(null,"&fRunning VS "+VoxelSniper.getInstance().getDescription().getVersion());
		if (VoxelSniper.getInstance().getDescription().getVersion().split("-")[0].startsWith("5.168")) {
			Msg(null,"&c[WARNING] An older version of VoxelSniper has been detected. Some features may not work properly");
		}
		else {
			sniperManager = new SniperManagerFeature();
		}
		Plugin worldguardPlugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if((worldguardPlugin != null) && worldguardPlugin.isEnabled()) {
        	wgf = new Worldguard(worldguardPlugin,this);
    		addMaskManager(new VoxelMaskManager(worldguardPlugin,this) {
    			@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return wgf.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(wgf,this);
            Msg(null,"Plugin 'WorldGuard' found. Using it now.");
        } else {
            Msg(null,"Plugin 'WorldGuard' not found. Worldguard features disabled.");
        }
		Plugin plotmePlugin = getServer().getPluginManager().getPlugin("PlotMe");
        if((plotmePlugin != null) && plotmePlugin.isEnabled()) {
        	pmf = new PlotMeFeature(plotmePlugin,this);
        	addMaskManager(new VoxelMaskManager(plotmePlugin,this) {
        		@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return pmf.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(pmf,this);
            Msg(null,"Plugin 'PlotMe' found. Using it now.");
        } else {
            Msg(null,"Plugin 'PlotMe' not found. PlotMe features disabled.");
        }
		Plugin townyPlugin = getServer().getPluginManager().getPlugin("Towny");
        if((townyPlugin != null) && townyPlugin.isEnabled()) {
        	tf = new TownyFeature(townyPlugin,this);
        	addMaskManager(new VoxelMaskManager(townyPlugin,this) {
        		@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return tf.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(tf,this);
            Msg(null,"Plugin 'Towny' found. Using it now.");
        } else {
            Msg(null,"Plugin 'Towny' not found. Towny features disabled.");
        }
        Plugin factionsPlugin = getServer().getPluginManager().getPlugin("Factions");
        if((factionsPlugin != null) && factionsPlugin.isEnabled()) {
        	ff = new FactionsFeature(factionsPlugin,this);
        	addMaskManager(new VoxelMaskManager(factionsPlugin,this) {
        		@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return ff.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(ff,this);
            Msg(null,"Plugin 'Factions' found. Using it now.");
        } else {
            Msg(null,"Plugin 'Factions' not found. Factions features disabled.");
        }
        Plugin residencePlugin = getServer().getPluginManager().getPlugin("Residence");
        if((residencePlugin != null) && residencePlugin.isEnabled()) {
        	rf = new ResidenceFeature(residencePlugin,this);
        	addMaskManager(new VoxelMaskManager(residencePlugin,this) {
        		@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return rf.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(rf,this);
            Msg(null,"Plugin 'Residence' found. Using it now.");
        } else {
            Msg(null,"Plugin 'Residence' not found. Factions features disabled.");
        }
        Plugin griefpreventionPlugin = getServer().getPluginManager().getPlugin("GriefPrevention");
        if((griefpreventionPlugin != null) && griefpreventionPlugin.isEnabled()) {
        	gpf = new GriefPreventionFeature(griefpreventionPlugin,this);
        	addMaskManager(new VoxelMaskManager(griefpreventionPlugin,this) {
        		@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return gpf.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(gpf,this);
            Msg(null,"Plugin 'GriefPrevention' found. Using it now.");
        } else {
            Msg(null,"Plugin 'GriefPrevention' not found. GriefPrevention features disabled.");
        }
        Plugin preciousstonesPlugin = getServer().getPluginManager().getPlugin("PreciousStones");
        if((preciousstonesPlugin != null) && preciousstonesPlugin.isEnabled()) {
        	psf = new PreciousStonesFeature(preciousstonesPlugin,this);
        	addMaskManager(new VoxelMaskManager(preciousstonesPlugin,this) {
        		@Override
    			public VoxelMask getMask(Player player,Location location) {
    				return psf.getMask(player,location);
    			}
    		});
            getServer().getPluginManager().registerEvents(psf,this);
            Msg(null,"Plugin 'PreciousStones' found. Using it now.");
        } else {
            Msg(null,"Plugin 'PreciousStones' not found. PreciousStones features disabled.");
        }
        saveResource("english.yml", true);
        getConfig().options().copyDefaults(true);
        Map<String, Object> options = new HashMap<String, Object>();
        getConfig().set("version", "0.3.3");
        options.put("language","english");
        options.put("fast-mode",true);
        options.put("cooldown-ms",100);
        options.put("cooldown-brush-size",5);
        for (Entry<String, Object> node : options.entrySet()) {
        	 if (!getConfig().contains(node.getKey())) {
        		 getConfig().set(node.getKey(), node.getValue());
        	 }
        }
    	saveConfig();
    	this.saveDefaultConfig();
    	getServer().getPluginManager().registerEvents(this, this);   
		for (Player player:Bukkit.getOnlinePlayers()) {
    		lastMask.remove(player.getName());
    		lastregion.put(player.getName(),false);
		}
		Plugin protocolLibPlugin = getServer().getPluginManager().getPlugin("ProtocolLib");
		if (protocolLibPlugin!=null) {
			if (protocolLibPlugin.isEnabled()) {
				if (getConfig().getBoolean("fast-mode")) {
					protocolin = new ProtocolIn(this);
					Msg(null,"&6fast-mode enabled for VoxelSniperRegions!");
				}
				else {
					Msg(null,"&6fast-mode is disabled for VoxelSniperRegions, you can enable it in the config.yml");
				}
			}
		}
		else {
			Msg(null,"&c[Warning][VoxelSniperRegions] &fProtocolLib is required for fast mode. Please install it.");
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if (cmd.getName().equalsIgnoreCase("vsr")) {
    		Player player;
    		if (sender instanceof Player==false) {
    			player = null;
    			return false;
    		}
    		else {
    			player = (Player) sender;
    		}
    		
    		if (args.length>0) {
    			if (args[0].equalsIgnoreCase("reload" )) {
    				if (CheckPerm(player, "vsr.reload")) {
        				reloadConfig();
        				Msg(player, GetMsg("MSG7"));
        			}
        			else {
        				Msg(player, GetMsg("MSG6")+"vsr.reload");
        			}
    				return true;
    			}
    			if (args[0].equalsIgnoreCase("help" )) {
    				if (CheckPerm(player, "vsr.help")) {
        				Msg(player,GetMsg("MSG4"));
        			}
        			else {
        				Msg(player, GetMsg("MSG6")+"vsr.help");
        			}
    				return true;
    			}
    			if (args[0].equalsIgnoreCase("credits" )) {
    				if (CheckPerm(player, "vsr.help")) {
        				Msg(player,GetMsg("CREDITS"));
        			}
        			else {
        				Msg(player, GetMsg("MSG6")+"vsr.credits");
        			}
    				return true;
    			}
    		}
    		else {
    			if (CheckPerm(player, "vsr.help")) {
    				Msg(player,GetMsg("MSG4"));
    			}
    			else {
    				Msg(player, GetMsg("MSG6")+"vsr.help");
    			}
    		}
    	}
    	return false;
	}
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		lastMask.remove(player.getName());
		lastregion.put(player.getName(),false);
	}
	@EventHandler(priority=EventPriority.LOWEST)
	private void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.LEFT_CLICK_AIR)||event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		toUndo = false;
		Player player = event.getPlayer();
		ItemStack helditem = player.getItemInHand();
		if (CheckPerm(player,"voxelsniper.sniper")||CheckPerm(player,"voxelsniper.litesniper")) {
			if (CheckPerm(player,"vsr.bypass")==false) {
				try {
					if (sniperManager!=null) {
						if (helditem.getType().equals(Material.ARROW)) {
							lastPlayer = player;
						}
						else if (helditem.getType().equals(Material.SULPHUR)) {
							lastPlayer = player;
						}
						else if ((sniperManager.tool(player, voxelsniper)+"").equals("null")!=true) {
							lastPlayer = player;
						}
						if (getCheck()==false) {
							lastPlayer = null;
							int radius = sniperManager.radius(player, voxelsniper);
							if (getConfig().getInt("cooldown-brush-size")<=radius) {
								addCoolDown(player.getName(), System.currentTimeMillis()+getConfig().getLong("cooldown-ms"));
							}
							return;
						}
					}
					else {
						if (helditem.getType().equals(Material.ARROW)) {
							lastPlayer = player;
						}
						else if (helditem.getType().equals(Material.SULPHUR)) {
							lastPlayer = player;
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	private void onPlayerInteract2(PlayerInteractEvent event) {
		if (lastPlayer!=null) {
			if (toUndo) {
				try {
					if (sniperManager!=null) {
						sniperManager.undo(event.getPlayer(), voxelsniper);
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
		lastPlayer = null;
		lastMsg = null;
	}
	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
		if (coolDown.containsKey(event.getPlayer().getName())) { coolDown.remove(event.getPlayer().getName()); }
		lastMask.remove(event.getPlayer().getName());
		lastregion.remove(event.getPlayer().getName());
	}
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		if ((event.getFrom().getBlockX()!=(event.getTo().getBlockX()))) { return; }
		if ((event.getFrom().getBlockY()!=(event.getTo().getBlockY()))) { return; }
		if ((event.getFrom().getBlockZ()!=(event.getTo().getBlockZ()))) { return; }
		try {updateMask(event.getPlayer(),event.getPlayer().getLocation()); } catch (Exception e) {  }
	}
	@EventHandler
	private void onPlayerPortal(PlayerPortalEvent event) {
		if (lastMask.containsKey(event.getPlayer().getName())) {
			Object last = lastMask.get(event.getPlayer().getName());
			if (last instanceof Location[]) {
				if ((((Location[]) last)[0]).getWorld().equals(event.getPlayer().getWorld())==false) {
		    		lastMask.remove(event.getPlayer().getName());
		    		lastregion.put(event.getPlayer().getName(),false);
				}
			}
		}
	}
	@EventHandler
	private void onPlayerTeleport(PlayerTeleportEvent event) {
		if (lastMask.containsKey(event.getPlayer().getName())) {
			Object last = lastMask.get(event.getPlayer().getName());
			if (last instanceof Location[]) {
				if ((((Location[]) last)[0]).getWorld().equals(event.getPlayer().getWorld())==false) {
					lastMask.remove(event.getPlayer().getName());
		    		lastregion.put(event.getPlayer().getName(),false);
				}
			}
		}
	}
	public synchronized VoxelMask getMask(Player player) {
		if (lastMask.containsKey(player.getName())) {
				return lastMask.get(player.getName());
		}
		return null;
	}
	public synchronized VoxelMask[] getMasks() {
		return lastMask.values().toArray(new VoxelMask[lastMask.values().size()]);
	}
	public synchronized String[] getMaskedPlayerNames() {
		return lastMask.keySet().toArray(new String[lastMask.keySet().size()]);
	}
	public synchronized void setMask(Player player,VoxelMask mask) {
		lastMask.put(player.getName(), mask);
	}
	public synchronized void removeMask(Player player) {
		lastMask.remove(player.getName());
	}
	public synchronized void updateMask(Player player, Location location) {
		if (CheckPerm(player,"vsr.bypass")==false) {
			VoxelMask voxelmask = null;
			String myid = "";
			for (VoxelMaskManager current:regions.values()) {
				if (voxelmask==null) {
					if (CheckPerm(player,"vsr."+current.getKey())) {
						voxelmask = current.getMask(player,location);
					}
				}
			}
			if (voxelmask != null) {
				if (lastMask.containsKey(player.getName())) {
					if ((((VoxelMask) lastMask.get(player.getName())).getName().equals(myid))==false) {
						Msg(player,GetMsg("MSG5")+" &a"+myid+"&7.");
					}
					else {
						if (CheckPerm(player,"vsr.notify.greeting")) {
							if (lastregion.containsKey(player.getName())) {
								if (lastregion.get(player.getName())==false) {
									Msg(player,GetMsg("GREETING"));
								}
							}
						}
					}
				}
				else {
					Msg(player,GetMsg("MSG5")+" &a"+myid+"&7.");
				}
				lastMask.put(player.getName(),voxelmask);
				lastregion.put(player.getName(),true);
			}
			else {
				if (lastMask.containsKey(player.getName())) {
					VoxelMask mask = lastMask.get(player.getName());
					if (mask.contains(player.getLocation())) {
			    		lastMask.remove(player.getName());
			    		lastregion.put(player.getName(),false);
						if (CheckPerm(player,"vsr.notify")) {
							Msg(player,GetMsg("MSG1"));
						}
					}
					else if (CheckPerm(player,"vsr.notify.farewell")) {
						if (lastregion.containsKey(player.getName())) {
							if (lastregion.get(player.getName())==true) {
								Msg(player,GetMsg("FAREWELL"));
							}
						}
					}
				}
				else if (CheckPerm(player,"vsr.notify.farewell")) {
					if (lastregion.containsKey(player.getName())) {
						if (lastregion.get(player.getName())==true) {
							Msg(player,GetMsg("FAREWELL"));
						}
					}
				}
				lastregion.put(player.getName(),false);
			}
		}
		else {
			lastMask.remove(player.getName());
    		lastregion.put(player.getName(),false);
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (CheckPerm(player,"vsr.bypass")) {
			return;
		}
		if (event.getMessage().startsWith("/btool")) {
			if (sniperManager==null) {
				Msg(player,"&c[Warning] Update VoxelSniper to 5.169 to use this command.");
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
    private void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		if (toUndo==false) {
			if (lastPlayer!=null) {
				Location loc = new Location(lastPlayer.getWorld(),event.getBlock().getX(),event.getBlock().getY(),event.getBlock().getZ());
				if (lastMask.containsKey(lastPlayer.getName())) {
					VoxelMask mymask = lastMask.get(lastPlayer.getName());
					if (mymask.contains(loc)) {
						return;
					}
					else {
						if (lastMsg==null) {
							lastMsg = GetMsg("MSG15");
							Msg(lastPlayer,lastMsg);
						}
						toUndo=true;
						return;
					}
				}
				else {
					if (lastMsg==null) {
						lastMsg = GetMsg("MSG15");
						Msg(lastPlayer,lastMsg);
					}
					toUndo=true;
					return;
				}
			}
			else {
			}
		}
    }
}