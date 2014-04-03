package com.zeeveener.zport.objects;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.backend.Backend;
import com.zeeveener.zport.checks.Warmup;

public class Warp {

	private static HashMap<String, Warp> warpCache = new HashMap<String, Warp>();
	private static Object lock = new Object();
	private String name;
	private UUID owner;
	private Location location;
	private Boolean priv;
	private Long lastUsed, created;
	private Integer uses;
	
	public Warp(String n, UUID o, Location l, Boolean p, Long c, Long lu, Integer u){
		name = n;
		owner = o;
		location = l;
		priv = p;
		created = c;
		lastUsed = lu;
		uses = u;
		
		addToWarpCache(this);
	}
	public Warp(String n, UUID o, Location l, Boolean p, Long c){
		this(n,o,l,p,c,0L,0);
	}
	public Warp(String n, UUID o, Location l, Boolean p){
		this(n,o,l,p,System.currentTimeMillis());
	}
	
	private static boolean existsInWarpCache(String n){
		synchronized(lock){
			return warpCache.containsKey(n);
		}
	}
	private static void removeFromWarpCache(String n){
		synchronized(lock){
			if(warpCache.containsKey(n)) warpCache.remove(n);
		}
	}
	private static void addToWarpCache(Warp w){
		synchronized(lock){
			if(!warpCache.containsKey(w.getName())) warpCache.put(w.getName(), w);
		}
	}
	private static Warp getFromWarpCache(String n){
		synchronized(lock){
			if(!warpCache.containsKey(n)) return null;
			return warpCache.get(n);
		}
	}
	
	public static Warp create(Player p, String n, Boolean pr){
		Warp w = new Warp(n, p.getUniqueId(), p.getLocation(), pr);
		w.save();
		return w;
	}
	public void delete(){
		removeFromWarpCache(name);
		
		if(Backend.isSQL()){
			//Backend.getSQL().preparedUpdate("", new Object[]{});
		}else{
			File f = new File(Backend.getWarpFolder(), name+".yml");
			if(f.exists()){
				f.setWritable(true);
				f.delete();
			}
		}
	}
	public void goTo(Player p){
		Warmup.warmup(p, ZPort.config.getInt("Warmup.Warp", 0), location);
		lastUsed = System.currentTimeMillis();
		uses++;
		addToWarpCache(this);
	}
	
	public String getName(){
		return name;
	}
	public UUID getOwner(){
		return owner;
	}
	public Location getLocation(){
		return location;
	}
	public boolean getPrivate(){
		return priv;
	}
	public long getCreated(){
		return created;
	}
	public long getLastUsed(){
		return lastUsed;
	}
	public int getUses(){
		return uses;
	}
	
	public void setLocation(Location l){
		location = l;
	}
	public void setUses(int u){
		uses = u;
	}
	public void setLastUsed(long lu){
		lastUsed = lu;
	}
	
	public static Warp loadFromString(String s){
		try{
			s = s.replaceAll("[", "").replaceAll("]", "");
			String[] w = s.split(":");
			
			String n = w[0];
			UUID o = UUID.fromString(w[1]);
			boolean p = Boolean.parseBoolean(w[2]);
			long c = Long.parseLong(w[3]);
			long l = Long.parseLong(w[4]);
			int u = Integer.parseInt(w[5]);
			double x = Double.parseDouble(w[6]);
			double y = Double.parseDouble(w[7]);
			double z = Double.parseDouble(w[8]);
			float yaw = Float.parseFloat(w[9]);
			float pitch = Float.parseFloat(w[10]);
			World world = Bukkit.getServer().getWorld(w[11]);
			Location loc = new Location(world, x, y, z, yaw, pitch);
			
			Warp warp = new Warp(n, o, loc, p, c);
			warp.setLastUsed(l);
			warp.setUses(u);
			return warp;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public String toString(){
		return "[" + name + ":" + owner.toString() + ":" + priv + ":" 
				+ created + ":" + lastUsed + ":" + uses + ":" 
				+ rtd(location.getX()) + ":" + rtd(location.getY()) + ":" 
				+ rtd(location.getZ()) + ":" + rtd(location.getYaw()) + ":" 
				+ rtd(location.getPitch()) + ":" + location.getWorld().getName() + "]";
	}
	public String toStringSimple(){
		return name + "(" + rtd(location.getX()) + "," + rtd(location.getY()) + "," + rtd(location.getZ()) + ")";
	}
	public static boolean exists(String n){
		if(Warp.existsInWarpCache(n)) return true;
		
		if(Backend.isSQL()){
			ResultSet rs = Backend.getSQL().preparedQuery("SELECT * FROM zp_warps WHERE name='?';", new Object[]{n});
			try {
				return (rs != null && rs.next());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			File f = new File(Backend.getWarpFolder(), n+".yml");
			return f.exists();
		}
		return false;
	}
	public static Warp getWarp(String n){
		Warp w;
		if((w = Warp.getFromWarpCache(n)) == null){
			if(Backend.isSQL()){
				ResultSet rs = Backend.getSQL().preparedQuery("SELECT * FROM zp_warps WHERE name='?';", new Object[]{n});
				try {
					rs.next();
					Location loc = new Location(Bukkit.getServer().getWorld(rs.getString("world")), 
							rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), 
							rs.getFloat("yaw"), rs.getFloat("pitch"));
					w = new Warp(rs.getString("name"), UUID.fromString(rs.getString("owner")), 
							loc, rs.getBoolean("private"), rs.getLong("date_created"), 
							rs.getLong("last_used"), rs.getInt("total_uses"));
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}else{
				File f = new File(Backend.getWarpFolder(), n+".yml");
				FileConfiguration c = YamlConfiguration.loadConfiguration(f);
				w = Warp.loadFromString(c.getString("Object"));
			}
		}
		return w;
	}
	public void save(){
		String n = getName();
		UUID o = getOwner();
		boolean p = getPrivate();
		int u = getUses();
		long c = getCreated();
		long lu = getLastUsed();
		Location l = getLocation();
		
		if(Backend.isSQL()){
			Backend.getSQL().preparedUpdate("INSERT INTO zp_warps(date_created,last_used,total_uses,name,owner,private,x,y,z,yaw,pitch,world) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE last_used=?, total_uses=?", 
					new Object[]{c,lu,u,n,o.toString(),p,l.getX(),l.getY(),l.getZ(),l.getYaw(),l.getPitch(),l.getWorld().getName(),lu,u});
		}else{
			File f = new File(Backend.getWarpFolder(), n+".yml");
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileConfiguration config = YamlConfiguration.loadConfiguration(f);
			config.set("Name", n);
			config.set("Owner", o.toString());
			config.set("Private", p);
			config.set("Uses", u);
			config.set("Created", c);
			config.set("LastUsed", lu);
			config.set("Location.World", l.getWorld().getName());
			config.set("Location.X", l.getX());
			config.set("Location.Y", l.getY());
			config.set("Location.Z", l.getZ());
			config.set("Location.Yaw", l.getYaw());
			config.set("Location.Pitch", l.getPitch());
			config.set("Object", toString());
			try {
				config.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private double rtd(final double d) {
		final DecimalFormat threeDForm = new DecimalFormat("#.###");
		return Double.valueOf(threeDForm.format(d));
	}

	public static void startCacheThread(){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Bukkit.getPluginManager().getPlugin("ZPort"), new Runnable(){
			@Override
			public void run() {
				ZChat.toConsole("Cleaning Warp Cache...");
				long current = System.currentTimeMillis();
				int cleared = 0;
				for(String s : warpCache.keySet()){
					Warp w = getFromWarpCache(s);
					if(w.getLastUsed() <= current-10*60*1000){
						w.save();
						Warp.removeFromWarpCache(s);
						cleared++;
					}
				}
				ZChat.toConsole("Cleared " + cleared + " entries from the Warp Cache.");
			}
		}, 10*60*20L, 10*60*20L);
	}
}
