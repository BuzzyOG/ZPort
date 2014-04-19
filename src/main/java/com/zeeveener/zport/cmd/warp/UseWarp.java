package com.zeeveener.zport.cmd.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.checks.Cooldown;
import com.zeeveener.zport.objects.Warp;

public class UseWarp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){

		if(!(s instanceof Player)){
			ZPort.chat.error(s, "Console cannot travel to Warps.");
			return true;
		}

		if(args.length != 1){
			ZPort.chat.error(s, "Invalid Number of Arguments.");
			return false;
		}

		Player p = (Player) s;

		if(!ZPort.config.getBoolean("Feature.Warp", false) && !p.hasPermission("zp.exempt.toggles.warp")){
			ZPort.chat.error(s, "Warp features have been disabled.");
			return true;
		}

		if(!Warp.exists(args[0])){
			ZPort.chat.error(s, "That Warp doesn't exist.");
			return true;
		}

		Warp w = Warp.getWarp(args[0]);

		if(w.getOwner().equals(p.getName())){
			if(w.getPrivate()){
				if(!p.hasPermission("zp.warp.use.own.private")){
					ZPort.chat.error(s, "You don't have permission to use your private warps.");
					return true;
				}
			}else{
				if(!p.hasPermission("zp.warp.use.own.public")){
					ZPort.chat.error(s, "You don't have permission to use your public warps.");
					return true;
				}
			}
		}else{
			if(w.getPrivate()){
				if(!p.hasPermission("zp.warp.use.others.private")){
					ZPort.chat.error(s, "You don't have permission to use others' private warps.");
					return true;
				}
			}else{
				if(!p.hasPermission("zp.warp.use.others.public")){
					ZPort.chat.error(s, "You don't have permission to use others' public warps.");
					return true;
				}
			}
		}

		/*
		 * Add check for allowed to travel to warps in other worlds here
		 */

		if(!Cooldown.doneCooldown(p, "warp")){
			ZPort.chat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}

		ZPort.chat.message(s, "Travelling to: " + ZPort.chat.m + w.getName());
		w.goTo(p);
		new Cooldown(p, "warp", ZPort.config.getInt("Cooldown.Warp", 0));
		return true;
	}
}
