package com.zeeveener.zport.checks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;

public class Warmup {
	
	public static void warmup(final Player p, int t, final Location l){
		int time = t;
		if(p.hasPermission("zp.exempt.warmups")) time = 0;
		if(time != 0) ZChat.message(p, "You will travel in: " + ZChat.m + time + " seconds...");
		Bukkit.getScheduler().runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("ZPort"), new Runnable(){
			@Override
			public void run() {
				p.teleport(l);
			}
		}, time*20L);
	}
	
}
