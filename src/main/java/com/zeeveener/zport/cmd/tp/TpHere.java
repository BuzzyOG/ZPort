package com.zeeveener.zport.cmd.tp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;

public class TpHere implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot summon players to itself.");
			return true;
		}
		
		if(args.length != 1){
			ZChat.error(s, "Invalid Number of Arguments.");
			return false;
		}
		
		Player p = (Player)s;
		Player get;
		
		if((get = Bukkit.getPlayer(args[0])) == null || !get.isOnline()){
			ZChat.error(s, "That player is offline.");
			return true;
		}
		
		if(!p.hasPermission("zp.tp.here.player")){
			ZChat.error(s, "You don't have permission to TP Players to You.");
			return true;
		}
		if(get.isOp() && !p.hasPermission("zp.tp.here.op")){
			ZChat.error(s, "You don't have permission to TP Ops to You.");
			return true;
		}
		
		/*
		 * Check if get is blocking teleportation here.
		 */
		
		get.teleport(p);
		ZChat.message(s, "You have Teleported " + ZChat.m + get.getName() + ZChat.g + " to yourself.");
		ZChat.message(get, ZChat.m + p.getName() + ZChat.g + " has teleported you to themselves.");
		
		return true;
	}
}
