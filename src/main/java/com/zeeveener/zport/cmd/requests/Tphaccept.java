package com.zeeveener.zport.cmd.requests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;

public class Tphaccept implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){
		if(!(s instanceof Player)){
			ZChat.error(s, "Only players can Accept Teleport Requests at this time.");
			return true;
		}
		if(args.length != 0){
			return false;
		}
		
		Player p = (Player)s;
		if(!p.hasPermission("zp.request.accept.tpahere")){
			ZChat.error(s, "You don't have permission to use /tpha");
			return true;
		}
		
		Request r = Request.getFromCache(p.getUniqueId());
		if(r == null || r.getSummonRequester() == null){
			ZChat.error(s, "You don't have any Summon Requests at this time.");
			return true;
		}

		Player requester = Bukkit.getPlayer(r.getSummonRequester());
		if(requester == null){
			ZChat.message(s, "The player that sent the request is not online.");
			return true;
		}
		
		r.fulfillSummonRequest();
		
		ZChat.message(s, "Accepted Summon Request.");
		ZChat.message(requester, p.getName() + " Accepted your Summon Request."); 
		
		return true;
	}

}
