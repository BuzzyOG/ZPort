package com.zeeveener.zport.checks;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class TpBlocks{

	private static HashMap<Player, Boolean>	blocks	= new HashMap<Player, Boolean>();

	public synchronized static boolean isBlocking(Player p){
		if(!blocks.containsKey(p)) return false;
		return blocks.get(p);
	}

	public synchronized static void setBlocking(Player p, Boolean b){
		blocks.put(p, b);
	}

	public synchronized static void removeFromCache(Player p){
		if(!blocks.containsKey(p)) return;
		blocks.remove(p);
	}
}
