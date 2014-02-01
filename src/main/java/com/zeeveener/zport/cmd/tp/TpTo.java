package com.zeeveener.zport.cmd.tp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.checks.Cooldown;
import com.zeeveener.zport.checks.Warmup;

public class TpTo implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot travel to locations.");
			return true;
		}
		
		if(args.length < 3 || args.length > 4){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}
		
		Player p = (Player)s;
		
		if(!p.hasPermission("zp.tp.to.coord")){
			ZChat.error(p, "You don't have permission to TP to Coordinates.");
			return true;
		}
		
		double x,y,z;
		World w = p.getWorld();
		
		try {
			x = Double.parseDouble(args[0]);
			y = Double.parseDouble(args[1]);
			z = Double.parseDouble(args[2]);
			if(args.length == 4) w = Bukkit.getWorld(args[3]);
		} catch (NumberFormatException e) {
			ZChat.error(s, "Expected a number, got a word...");
			return false;
		}
		
		if(!Cooldown.doneCooldown(p, "tele")){
			ZChat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}
		
		Warmup.warmup(p, ZPort.config.getInt("Warmup.Teleport", 0), new Location(w, x,y,z));
		new Cooldown(p, "tele", ZPort.config.getInt("Cooldown.Teleport", 0));
		ZChat.message(s, "You have arrived at: " + ZChat.m + x + "," + y + "," + z + ZChat.g + " in " + ZChat.m + w.getName());
		
		return true;
	}
}
