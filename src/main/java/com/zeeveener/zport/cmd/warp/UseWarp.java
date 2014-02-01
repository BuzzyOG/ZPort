package com.zeeveener.zport.cmd.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.checks.Cooldown;
import com.zeeveener.zport.objects.Warp;

public class UseWarp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot travel to Warps.");
			return true;
		}
		
		if(args.length != 1){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}
		
		Player p = (Player)s;
		
		if(!Warp.exists(args[0])){
			ZChat.error(s, "That Warp doesn't exist.");
			return true;
		}
		
		Warp w = Warp.getWarp(args[0]);
		
		if(w.getOwner().equals(p.getName())){
			if(w.getPrivate()){
				if(!p.hasPermission("zp.warp.use.own.private")){
					ZChat.error(s, "You don't have permission to use your private warps.");
					return true;
				}
			}else{
				if(!p.hasPermission("zp.warp.use.own.public")){
					ZChat.error(s, "You don't have permission to use your public warps.");
					return true;
				}
			}
		}else{
			if(w.getPrivate()){
				if(!p.hasPermission("zp.warp.use.others.private")){
					ZChat.error(s, "You don't have permission to use others' private warps.");
					return true;
				}
			}else{
				if(!p.hasPermission("zp.warp.use.others.public")){
					ZChat.error(s, "You don't have permission to use others' public warps.");
					return true;
				}
			}
		}
		
		/*
		 * Add check for allowed to travel to warps in other worlds here
		 */
		
		if(!Cooldown.doneCooldown(p, "warp")){
			ZChat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}
		
		ZChat.message(s, "Travelling to: " + ZChat.m + w.getName());
		w.goTo(p);
		new Cooldown(p, "warp", ZPort.config.getInt("Cooldown.Warp", 0));
		return true;
	}
}
