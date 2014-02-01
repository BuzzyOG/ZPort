package com.zeeveener.zport.cmd.tp.related;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.checks.Cooldown;
import com.zeeveener.zport.checks.Warmup;

public class Back implements CommandExecutor{

	private static HashMap<Player, Location> prevs = new HashMap<Player, Location>();
	
	public synchronized static void setPreviousLocation(Player p, Location l){
		prevs.put(p, l);
	}
	public synchronized static void removePreviousLocation(Player p){
		prevs.remove(p);
	}
	public synchronized static Location getPreviousLocation(Player p){
		return prevs.get(p);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label,
			String[] args) {
		
		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot go back to a previous location.");
			return true;
		}
		if(args.length != 0){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}
		
		Player p = (Player)s;
		
		if(!p.hasPermission("zp.tp.back")){
			ZChat.error(s, "You don't have permission to go Back.");
			return true;
		}
		
		if(!Cooldown.doneCooldown(p, "back")){
			ZChat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}
		
		if(getPreviousLocation(p) == null){
			ZChat.error(s, "You don't have a previous location set.");
			return true;
		}
		Location l = prevs.get(p);
		Warmup.warmup(p, ZPort.config.getInt("Warmup.Back", 0), l);
		new Cooldown(p, "back", ZPort.config.getInt("Cooldown.Back", 0));
		ZChat.message(s, "You have returned to your previous location.");
		
		return true;
	}
}
