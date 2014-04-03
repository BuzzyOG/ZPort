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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.backend.Backend;
import com.zeeveener.zport.checks.Warmup;

public class Home {

	private static HashMap<String, Home> cache = new HashMap<String, Home>();
	private static Object lock = new Object();
	private Location location;
	private UUID owner;
	
	public Home(Player p, Location l){
		owner = p.getUniqueId();
		location = l;
	}
	
	public static boolean existsInCache(Player p, String w){
		synchronized(lock){
			return cache.containsKey(p.getUniqueId().toString() + "-" + w);
		}
	}
	public static Home getFromCache(Player p, String w){
		synchronized(lock){
			if(!cache.containsKey(p.getUniqueId().toString() + "-" + w)) return null;
			return cache.get(p.getUniqueId().toString() + "-" + w);
		}
	}
	public static void removeFromCache(Home h){
		if(h == null)return;
		synchronized(lock){
			if(!cache.containsKey(h.getOwner() + "-" + h.getLocation().getWorld().getName())) return;
			cache.remove(h.getOwner() + "-" + h.getLocation().getWorld().getName());
		}
	}
	public static void addToCache(Home h){
		if(h == null)return;
		synchronized(lock){
			if(cache.containsKey(h.getOwner() + "-" + h.getLocation().getWorld().getName())) return;
			cache.put(h.getOwner() + "-" + h.getLocation().getWorld().getName(), h);
		}
	}
	
	public static Home getHome(Player p, String w){
		Home h = null;
		if((h = getFromCache(p,w)) != null) return h;
		if(Backend.isSQL()){
			ResultSet rs = Backend.getSQL().preparedQuery("SELECT * FROM zp_homes WHERE name='?' && world='?'", new Object[]{p.getUniqueId().toString(),w});
			try {
				rs.next();
				Location l = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
				h =  new Home(p,l);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			File f = new File(Backend.getHomeFolder(), p.getUniqueId().toString()+"-"+w+".yml");
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			Location l = new Location(Bukkit.getWorld(c.getString("Location.World")), c.getDouble("Location.X"), c.getDouble("Location.Y"), c.getDouble("Location.Z"),
					Float.parseFloat(c.getString("Location.Yaw")), Float.parseFloat(c.getString("Location.Pitch")));
			h = new Home(p,l);
		}
		return h;
	}
	public static boolean exists(Player p, String w){
		if(Backend.isSQL()){
			ResultSet rs = Backend.getSQL().preparedQuery("SELECT * FROM zp_homes WHERE name='?' && world='?'", new Object[]{p.getUniqueId().toString(), w});
			try {
				return (rs != null && rs.next());
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}else{
			File f = new File(Backend.getHomeFolder(), p.getUniqueId().toString()+"-"+w+".yml");
			return f.exists();
		}
	}
	public static Home createHome(Player p){
		Home h = new Home(p, p.getLocation());
		h.save();
		return h;
	}
	
	public String getOwner(){
		return owner.toString();
	}
	public Location getLocation(){
		return location;
	}
	public void setLocation(Location l){
		location = l;
	}
	
	public boolean goTo(Player p){
		if(p.getUniqueId().toString().equals(owner)){
			Warmup.warmup(p, ZPort.config.getInt("Warmup.Home", 0), location);
			return true;
		}
		return false;
	}
	
	private void save(){
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		float yaw = location.getYaw();
		float pitch = location.getPitch();
		String world = location.getWorld().getName();
		if(Backend.isSQL()){
			Backend.getSQL().preparedUpdate("REPLACE INTO zp_homes(owner,x,y,z,yaw,pitch,world) VALUES(?,?,?,?,?,?,?);", 
					new Object[]{owner,x,y,z,yaw,pitch,world});
		}else{
			File f = new File(Backend.getHomeFolder(), owner+"-"+world+".yml");
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			c.set("Owner", owner);
			c.set("Location.World", world);
			c.set("Location.X", x);
			c.set("Location.Y", y);
			c.set("Location.Z", z);
			c.set("Location.Yaw", yaw);
			c.set("Location.Pitch", pitch);
			try {
				c.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String toStringSimple(){
		return owner + "(" + location.getWorld().getName() + ":" + rtd(location.getX()) + "," + rtd(location.getY()) + "," + rtd(location.getZ()) + ")";
	}
	private double rtd(final double d) {
		final DecimalFormat threeDForm = new DecimalFormat("#.###");
		return Double.valueOf(threeDForm.format(d));
	}
}