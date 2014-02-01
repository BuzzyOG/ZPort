package com.zeeveener.zport.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.zeeveener.zport.objects.Home;

public class PlayerLogin implements Listener{
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		Player p = e.getPlayer();
		for(World w : Bukkit.getWorlds()){
			Home.addToCache(Home.getHome(p, w.getName()));
		}
	}
}
