package com.zeeveener.zport.backend;

import com.zeeveener.zcore.bukkit.ZConfig;
import com.zeeveener.zport.ZPort;

public class Config extends ZConfig{

	public Config(ZPort instance){
		super(instance);
		init();
	}

	private void init(){
		if(!configContains("Backend.Type")) set("Backend.Type", "file");
		if(!configContains("Backend.MySQL.Host")) set("Backend.MySQL.Host", "localhost");
		if(!configContains("Backend.MySQL.Database")) set("Backend.MySQL.Database", "mcdb");
		if(!configContains("Backend.MySQL.User")) set("Backend.MySQL.User", "root");
		if(!configContains("Backend.MySQL.Pass")) set("Backend.MySQL.Pass", "toor");
		if(!configContains("Backend.MySQL.Port")) set("Backend.MySQL.Port", 3306);
		// if(!configContains("Updater.Check")) set("Updater.Check", true);
		// if(!configContains("Updater.Download")) set("Updater.Download",
		// true);
		if(!configContains("Feature.Warp")) set("Feature.Warp", true);
		if(!configContains("Feature.Home")) set("Feature.Home", true);
		if(!configContains("Feature.Teleportation")) set("Feature.Teleportation", true);
		if(!configContains("Feature.WarpSign")) set("Feature.WarpSign", true);
		if(!configContains("Cooldown.Warp")) set("Cooldown.Warp", 0);
		if(!configContains("Cooldown.Home")) set("Cooldown.Home", 0);
		if(!configContains("Cooldown.Back")) set("Cooldown.Back", 0);
		if(!configContains("Cooldown.Teleport")) set("Cooldown.Teleport", 0);
		if(!configContains("Warmup.Warp")) set("Warmup.Warp", 0);
		if(!configContains("Warmup.Home")) set("Warmup.Home", 0);
		if(!configContains("Warmup.Back")) set("Warmup.Back", 0);
		if(!configContains("Warmup.Teleport")) set("Warmup.Teleport", 0);
	}
}
