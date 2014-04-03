package com.zeeveener.zport.cmd.warp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.objects.Warp;

public class SetWarp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(!(s instanceof Player)){
			ZChat.toConsole("ERROR: Console cannot set Warps.");
			return true;
		}

		Player p = (Player) s;

		if(!ZPort.config.getBoolean("Feature.Warp", false) && !p.hasPermission("zp.exempt.toggles.warp")){
			ZChat.error(s, "Warp features have been disabled.");
			return true;
		}

		if(args.length == 0 || args.length >= 3){
			ZChat.error(p, "Invalid Number of Arguments.");
			return false;
		}

		if(Warp.exists(args[0])){
			Warp w = Warp.getWarp(args[0]);
			if(w.getName().equals(p.getName())){
				w.setLocation(p.getLocation());
				ZChat.message(p, "New Warp Location: " + ZChat.m + w.toStringSimple());
				return true;
			}else if(w.getPrivate()){
				if(p.hasPermission("zp.warp.modify.private")){
					w.setLocation(p.getLocation());
					ZChat.message(p, "New Warp Location: " + ZChat.m + w.toStringSimple());
					try{
						ZChat.message(Bukkit.getPlayer(w.getOwner()), p.getName() + " has changed your warp " + ZChat.m + w.getName() + ZChat.g + " to "
								+ ZChat.m + w.toStringSimple());
					}catch(NullPointerException e){}
					return true;
				}else{
					ZChat.error(p, "You don't have permission to modify private warps.");
					return true;
				}
			}else if(p.hasPermission("zp.warp.modify.public")){
				w.setLocation(p.getLocation());
				ZChat.message(p, "New Warp Location: " + ZChat.m + w.toStringSimple());
				try{
					ZChat.message(Bukkit.getPlayer(w.getOwner()), p.getName() + " has changed your warp " + ZChat.m + w.getName() + ZChat.g + " to " + ZChat.m
							+ w.toStringSimple());
				}catch(NullPointerException e){}
				return true;
			}else{
				ZChat.error(p, "You don't have permission to modify other players' warps.");
				return true;
			}
		}

		/*
		 * Add check for warp limit and warp limit per world here
		 */

		if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("t") || args[1].equalsIgnoreCase("p") || args[1].equalsIgnoreCase("private")){
			if(p.hasPermission("zp.warp.create.private")){
				Warp w = Warp.create(p, args[0], true);
				ZChat.message(p, "Created New Warp: " + ZChat.m + w.toStringSimple());
				return true;
			}else{
				ZChat.error(p, "You don't have permission to create private warps.");
				return true;
			}
		}else if(p.hasPermission("zp.warp.create.public")){
			Warp w = Warp.create(p, args[0], false);
			ZChat.message(p, "Created New Warp: " + ZChat.m + w.toStringSimple());
			return true;
		}else{
			ZChat.error(p, "You don't have permission to create warps.");
			return true;
		}
	}
}
