package com.zeeveener.zport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.backend.Backend;
import com.zeeveener.zport.backend.Config;
import com.zeeveener.zport.objects.Warp;
import com.zeeveener.zport.objects.WarpSign;

public class ZPort extends JavaPlugin{
	
	public static Config config;
	
	public void onEnable(){
		config = new Config(this);
		new Backend(this);
		
		new ZChat(this);
		Warp.startCacheThread();
		WarpSign.startWarpSignCache();
	}
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
	}
}