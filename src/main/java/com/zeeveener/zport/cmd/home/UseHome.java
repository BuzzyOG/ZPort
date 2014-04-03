package com.zeeveener.zport.cmd.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.checks.Cooldown;
import com.zeeveener.zport.objects.Home;

public class UseHome implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){

		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot travel to a home.");
			return true;
		}
		if(args.length >= 2){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}

		Player p = (Player) s;

		if(!ZPort.config.getBoolean("Feature.Home", false) && !s.hasPermission("zp.exempt.toggles.home")){
			ZChat.error(s, "Home features have been disabled.");
			return true;
		}

		Home h;
		if(args.length == 1){
			if(Home.exists(p, args[0])){
				if(!p.hasPermission("zp.home.use")){
					ZChat.error(s, "You don't have permission to travel to homes from this world.");
					return true;
				}else{
					h = Home.getHome(p, args[0]);
				}
			}else{
				ZChat.error(s, "You don't have a home in that world.");
				return true;
			}
		}else if(Home.exists(p, p.getLocation().getWorld().getName())){
			if(!p.hasPermission("zp.home.use")){
				ZChat.error(s, "You don't have permission to travel to homes from this world.");
				return true;
			}else{
				h = Home.getHome(p, p.getLocation().getWorld().getName());
			}
		}else{
			ZChat.error(s, "You don't have a home in this world.");
			return true;
		}

		if(!Cooldown.doneCooldown(p, "home")){
			ZChat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}

		h.goTo(p);
		new Cooldown(p, "home", ZPort.config.getInt("Cooldown.Home", 0));
		ZChat.message(s, "Arrived Home in: " + ZChat.m + h.getLocation().getWorld().getName());
		return true;
	}
}
