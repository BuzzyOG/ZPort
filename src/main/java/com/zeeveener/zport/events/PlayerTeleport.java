package com.zeeveener.zport.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.zeeveener.zport.cmd.tp.related.Back;

public class PlayerTeleport implements Listener{

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Back.setPreviousLocation(p, from);
	}

}
