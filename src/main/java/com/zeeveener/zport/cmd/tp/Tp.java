package com.zeeveener.zport.cmd.tp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;

public class Tp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot teleport to players.");
			return true;
		}
		
		if(args.length != 1){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}
		
		Player p = (Player)s;
		Player to;
		
		if((to = Bukkit.getPlayer(args[0])) == null || !to.isOnline()){
			ZChat.error(s, "That player is offline.");
			return true;
		}
		
		if(!p.hasPermission("zp.tp.to.player")){
			ZChat.error(s, "You don't have permission to TP to players.");
			return true;
		}
		if(to.isOp() && !p.hasPermission("zp.tp.to.op")){
			ZChat.error(s, "You don't have permission to TP to ops.");
			return true;
		}
		
		/*
		 * Check for if target player is blocking teleportation here
		 */
		
		p.teleport(to);
		ZChat.message(s, "You have Teleported to: " + ZChat.m + to.getName());
		ZChat.message(to, ZChat.m + p.getName() + ZChat.g + " has Teleported to you.");
		
		return true;
	}
}