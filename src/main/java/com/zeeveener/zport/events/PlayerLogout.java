package com.zeeveener.zport.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.zeeveener.zport.cmd.tp.related.Back;
import com.zeeveener.zport.objects.Home;

public class PlayerLogout implements Listener{

	/*
	 * - Clear all pending requests by and to this player
	 */
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		Player p = e.getPlayer();
		for(World w : Bukkit.getWorlds()){
			Home.removeFromCache(Home.getHome(p, w.getName()));
		}
		Back.removePreviousLocation(p);
	}
	
}
