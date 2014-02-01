package com.zeeveener.zport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.backend.Backend;
import com.zeeveener.zport.backend.Config;
import com.zeeveener.zport.events.PlayerLogin;
import com.zeeveener.zport.events.PlayerLogout;
import com.zeeveener.zport.events.PlayerTeleport;
import com.zeeveener.zport.objects.Warp;
import com.zeeveener.zport.objects.WarpSign;

public class ZPort extends JavaPlugin{
	
	public static Config config;
	
	public void onEnable(){
		config = new Config(this);
		new Backend(this);
		new ZChat(this);
		
		Warp.startCacheThread();
		WarpSign.startCacheThread();
		
		this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLogin(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLogout(), this);
	}
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
	}
}
