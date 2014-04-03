package com.zeeveener.zport.checks;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Cooldown{

	private static HashMap<Player, Cooldown>	warp	= new HashMap<Player, Cooldown>();
	private static HashMap<Player, Cooldown>	home	= new HashMap<Player, Cooldown>();
	private static HashMap<Player, Cooldown>	back	= new HashMap<Player, Cooldown>();
	private static HashMap<Player, Cooldown>	tele	= new HashMap<Player, Cooldown>();

	private long								time;

	public synchronized static boolean doneCooldown(Player p, String type){
		if(p.hasPermission("zp.exempt.cooldowns")) return true;
		if(type.equalsIgnoreCase("warp")){
			if(warp.containsKey(p) && warp.get(p).getDoneTime() < System.currentTimeMillis()){
				removeCooldown(p, type);
				return true;
			}
		}else if(type.equalsIgnoreCase("home")){
			if(home.containsKey(p) && home.get(p).getDoneTime() < System.currentTimeMillis()){
				removeCooldown(p, type);
				return true;
			}
		}else if(type.equalsIgnoreCase("back")){
			if(back.containsKey(p) && back.get(p).getDoneTime() < System.currentTimeMillis()){
				removeCooldown(p, type);
				return true;
			}
		}else if(type.equalsIgnoreCase("tele")){
			if(tele.containsKey(p) && tele.get(p).getDoneTime() < System.currentTimeMillis()){
				removeCooldown(p, type);
				return true;
			}
		}
		return false;
	}

	private synchronized static void addCooldown(Player p, String type, Cooldown c){
		if(type.equalsIgnoreCase("warp")){
			if(!warp.containsKey(p)) warp.put(p, c);
		}else if(type.equalsIgnoreCase("home")){
			if(!home.containsKey(p)) home.put(p, c);
		}else if(type.equalsIgnoreCase("back")){
			if(!back.containsKey(p)) back.put(p, c);
		}else if(type.equalsIgnoreCase("tele")){
			if(!tele.containsKey(p)) tele.put(p, c);
		}
	}

	public synchronized static void removeCooldown(Player p, String type){
		if(type.equalsIgnoreCase("warp")){
			if(warp.containsKey(p)) warp.remove(p);
		}else if(type.equalsIgnoreCase("home")){
			if(home.containsKey(p)) home.remove(p);
		}else if(type.equalsIgnoreCase("back")){
			if(back.containsKey(p)) back.remove(p);
		}else if(type.equalsIgnoreCase("tele")){
			if(tele.containsKey(p)) tele.remove(p);
		}
	}

	public Cooldown(Player p, String type, int wait){
		time = wait;
		addCooldown(p, type, this);
	}

	public long getDoneTime(){
		return time;
	}
}
