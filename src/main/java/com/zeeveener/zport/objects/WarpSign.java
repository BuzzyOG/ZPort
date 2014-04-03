package com.zeeveener.zport.objects;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;

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

public class WarpSign{

	private static HashMap<String, WarpSign> cache = new HashMap<String, WarpSign>();
	private static Object lock = new Object();
	private String name, owner;
	private int uses;
	private long created, lastUsed;
	private boolean priv;
	private Location loc;
	private WarpSign target;
	
	public WarpSign(String n, String o, Boolean p, Location l, Long c, Long lu, Integer u){
		name = n;
		owner = o;
		priv = p;
		loc = l;
		created = c;
		lastUsed = lu;
		uses = u;
		
		addToCache(this);
	}
	public WarpSign(String n, String o, Boolean p, Location l, Long c){
		this(n,o,p,l,c,0L,0);
	}
	public WarpSign(String n, String o, Boolean p, Location l){
		this(n,o,p,l,System.currentTimeMillis());
	}

	private static boolean existsInCache(String n){
		synchronized(lock){
			return cache.containsKey(n);
		}
	}
	private static void removeFromCache(WarpSign w){
		synchronized(lock){
			if(cache.containsKey(w.getName())) cache.remove(w.getName());
		}
	}
	private static WarpSign getFromCache(String n){
		synchronized(lock){
			if(!cache.containsKey(n)) return null;
			return cache.get(n);
		}
	}
	private static void addToCache(WarpSign w){
		synchronized(lock){
			if(!cache.containsKey(w.getName())) cache.put(w.getName(), w);
		}
	}
	
	public static WarpSign getWarpSign(String n){
		if(n.equalsIgnoreCase("NOTSET")) return null;
		if(!exists(n)) return null;
		WarpSign w;
		if((w = getFromCache(n)) == null){
			
			if(Backend.isSQL()){
				ResultSet rs = Backend.getSQL().preparedQuery("SELECT * FROM zp_signs WHERE name='?';", new Object[]{n});
				try {
					if(rs == null || !rs.next()) return null;
					double x = rs.getDouble("x");
					double y = rs.getDouble("y");
					double z = rs.getDouble("z");
					World world = Bukkit.getWorld(rs.getString("world"));
					float yaw = rs.getFloat("yaw");
					float pitch = rs.getFloat("pitch");
					Location l = new Location(world, x, y, z, yaw, pitch);
					String na = rs.getString("name");
					Boolean p = rs.getBoolean("private");
					String o = rs.getString("owner");
					String t = rs.getString("target");
					long c = rs.getLong("date_created");
					long lu = rs.getLong("last_used");
					int u = rs.getInt("total_uses");
					
					w = new WarpSign(na,o,p,l,c);
					w.setLastUsed(lu);
					w.setUses(u);
					w.setTarget(getWarpSign(t));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				File f = new File(Backend.getSignFolder(), n+".yml");
				if(!f.exists()) return null;
				FileConfiguration config = YamlConfiguration.loadConfiguration(f);
				w = fromString(config.getString("Object"));
			}
		}
		addToCache(w);
		return w;
	}
	
	public void goTo(Player p){
		Warmup.warmup(p, ZPort.config.getInt("Warmup.Warp", 0), target.getLocation());
		lastUsed = System.currentTimeMillis();
		uses++;
		addToCache(this);
	}
	public void goTo(Player p, Location l){
		Warmup.warmup(p, ZPort.config.getInt("Warmup.Warp", 0), l);
		lastUsed = System.currentTimeMillis();
		uses++;
		addToCache(this);
	}
	
	public String getOwner(){ return owner;}
	public String getName(){ return name;}
	public WarpSign getTarget(){ return target;}
	public int getUses(){ return uses;}
	public long getCreated(){ return created;}
	public long getLastUsed(){ return lastUsed;}
	public boolean getPrivate(){ return priv;}
	public Location getLocation(){ return loc;}
	
	public void setTarget(WarpSign w){
		target = w;
	}
	public void setLastUsed(long l){
		lastUsed = l;
	}
	public void setUses(int u){
		uses = u;
	}
	
	public static WarpSign create(Player p, String n, boolean b){
		WarpSign w = new WarpSign(n, p.getName(), b, p.getLocation());
		w.save();
		return w;
	}
	public void save(){
		
		if(Backend.isSQL()){
			String tN = "NOTSET";
			if(target != null) tN = target.getName();
			Backend.getSQL().preparedUpdate("INSERT INTO zp_signs(date_created,last_used,total_uses,name,target,owner,private,x,y,z,yaw,pitch,world) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE last_used=?, total_uses=?, target='?'", 
					new Object[]{created, lastUsed, uses, name, tN, 
							owner, priv, loc.getX(), loc.getY(), 
							loc.getZ(), loc.getYaw(), loc.getPitch(), 
							loc.getWorld().getName(), lastUsed, uses, tN});
		}else{
			File f = new File(Backend.getSignFolder(), name+".yml");
			try {
				if(!f.exists()){
					f.createNewFile();
				}
				FileConfiguration c = YamlConfiguration.loadConfiguration(f);
				c.set("Name", name);
				c.set("Owner", owner);
				if(target == null){
					c.set("Target", "NOTSET");
				}else{
					c.set("Target", target.getName());
				}
				c.set("Private", priv);
				c.set("Uses", uses);
				c.set("Created", created);
				c.set("LastUsed", lastUsed);
				c.set("Location.World", loc.getWorld().getName());
				c.set("Location.X", rtd(loc.getX()));
				c.set("Location.Y", rtd(loc.getY()));
				c.set("Location.Z", rtd(loc.getZ()));
				c.set("Location.Yaw", rtd(loc.getYaw()));
				c.set("Location.Pitch", rtd(loc.getPitch()));
				c.set("Object", toString());
				c.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void delete(){
		removeFromCache(this);
		
		if(Backend.isSQL()){
			Backend.getSQL().preparedUpdate("DELETE FROM zp_signs WHERE name='?' && owner='?';", new Object[]{name, owner});
		}else{
			File f = new File(Backend.getSignFolder(), name+".yml");
			if(f.exists()){
				f.setWritable(true);
				f.delete();
			}
		}
	}
	public static boolean exists(String n){
		if(existsInCache(n)) return true;
		
		if(Backend.isSQL()){
			ResultSet rs = Backend.getSQL().preparedQuery("SELECT * FROM zp_signs WHERE name='?';", new Object[]{n});
			try {
				return (rs != null && rs.next());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			File f = new File(Backend.getSignFolder(), n+".yml");
			return f.exists();
		}
		return false;
	}
	private double rtd(final double d) {
		final DecimalFormat threeDForm = new DecimalFormat("#.###");
		return Double.valueOf(threeDForm.format(d));
	}
	
	public String toString(){
		String n;
		if(target == null){
			n = "NOTSET";
		}else{
			n = target.getName();
		}
		return "[" + name + ":" + owner + ":" + priv + ":" + n + ":" 
				+ created + ":" + lastUsed + ":" + uses + ":" 
				+ rtd(loc.getX()) + ":" + rtd(loc.getY()) + ":" 
				+ rtd(loc.getZ()) + ":" + rtd(loc.getYaw()) + ":" 
				+ rtd(loc.getPitch()) + ":" + loc.getWorld().getName() + "]";
	}
	public static WarpSign fromString(String s){
		try{
			s = s.replaceAll("[", "").replaceAll("]", "");
			String[] w = s.split(":");
			
			String n = w[0];
			String o = w[1];
			boolean p = Boolean.parseBoolean(w[2]);
			WarpSign t = WarpSign.getWarpSign(w[3]);
			long c = Long.parseLong(w[4]);
			long l = Long.parseLong(w[5]);
			int u = Integer.parseInt(w[6]);
			double x = Double.parseDouble(w[7]);
			double y = Double.parseDouble(w[8]);
			double z = Double.parseDouble(w[9]);
			float yaw = Float.parseFloat(w[10]);
			float pitch = Float.parseFloat(w[11]);
			World world = Bukkit.getServer().getWorld(w[12]);
			Location loc = new Location(world, x, y, z, yaw, pitch);
			
			WarpSign ws = new WarpSign(n, o, p, loc, c);
			ws.setLastUsed(l);
			ws.setUses(u);
			ws.setTarget(t);
			return ws;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static void startCacheThread(){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Bukkit.getPluginManager().getPlugin("ZPort"), new Runnable(){
			@Override
			public void run() {
				ZChat.toConsole("Clearing WarpSign Cache...");
				int count = 0;
				for(WarpSign w : cache.values()){
					if(w.getLastUsed() <= (System.currentTimeMillis() - 10*60*1000)){
						w.save();
						removeFromCache(w);
						count++;
					}
				}
				ZChat.toConsole("Cleared " + count + " entries from WarpSign Cache.");
			}
		}, 5*60*20L, 10*60*20L);
	}
}
