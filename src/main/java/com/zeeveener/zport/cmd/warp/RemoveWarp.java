package com.zeeveener.zport.cmd.warp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.objects.Warp;

public class RemoveWarp implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!ZPort.config.getBoolean("Feature.Warp", false) && !s.hasPermission("zp.exempt.toggles.warp")){
			ZChat.error(s, "Warp features have been disabled.");
			return true;
		}
		
		if(args.length != 1){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}
		
		if(!Warp.exists(args[0])){
			ZChat.error(s, "That Warp doesn't exist.");
			return true;
		}
		
		Warp w = Warp.getWarp(args[0]);
		if(!(s instanceof Player)){
			try {
				ZChat.message(Bukkit.getPlayer(w.getOwner()), "Console Deleted your warp: " + ZChat.m + w.getName());
			} catch (NullPointerException e){}
			w.delete();
			return true;
		}
		Player p = (Player)s;
		
		if(p.getName().equals(w.getOwner())){
			if(w.getPrivate()){
				if(p.hasPermission("zp.warp.destroy.own.private")){
					ZChat.message(s, "Warp Deleted: " + ZChat.m + w.getName());
					w.delete();
					return true;
				}else{
					ZChat.error(s, "You don't have permission to delete your private warps.");
					return true;
				}
			}else{
				if(p.hasPermission("zp.warp.destroy.own.public")){
					ZChat.message(s, "Warp Deleted: " + ZChat.m + w.getName());
					w.delete();
					return true;
				}else{
					ZChat.error(s, "You don't have permission to delete your public warps.");
					return true;
				}
			}
		}
		
		if(w.getPrivate()){
			if(p.hasPermission("zp.warp.destroy.others.private")){
				try {
					ZChat.message(Bukkit.getPlayer(w.getOwner()), p.getName() + " Deleted your private warp: " + ZChat.m + w.getName());
				} catch (NullPointerException e){}
				ZChat.message(s, "Deleted " + w.getOwner() + "'s Private Warp: " + ZChat.m + w.getName());
				w.delete();
				return true;
			}else{
				ZChat.error(s, "You don't have permission to delete others' private warps.");
				return true;
			}
		}else if(p.hasPermission("zp.warp.destroy.others.public")){
			try {
				ZChat.message(Bukkit.getPlayer(w.getOwner()), p.getName() + " Deleted your public warp: " + ZChat.m + w.getName());
			} catch (NullPointerException e){}
			ZChat.message(s, "Deleted " + w.getOwner() + "'s Public Warp: " + ZChat.m + w.getName());
			w.delete();
			return true;
		}else{
			ZChat.error(s, "You don't have permission to delete others' public warps.");
			return true;
		}
	}
}
