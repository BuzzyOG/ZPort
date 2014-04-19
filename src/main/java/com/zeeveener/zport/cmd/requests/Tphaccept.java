package com.zeeveener.zport.cmd.requests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zport.ZPort;

public class Tphaccept implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){
		if(!(s instanceof Player)){
			ZPort.chat.error(s, "Only players can Accept Teleport Requests at this time.");
			return true;
		}
		if(args.length != 0){ return false; }

		Player p = (Player) s;
		if(!p.hasPermission("zp.request.accept.tpahere")){
			ZPort.chat.error(s, "You don't have permission to use /tpha");
			return true;
		}

		Request r = Request.getFromCache(p.getUniqueId());
		if(r == null || r.getSummonRequester() == null){
			ZPort.chat.error(s, "You don't have any Summon Requests at this time.");
			return true;
		}

		Player requester = Bukkit.getPlayer(r.getSummonRequester());
		if(requester == null){
			ZPort.chat.message(s, "The player that sent the request is not online.");
			return true;
		}

		r.fulfillSummonRequest();

		ZPort.chat.message(s, "Accepted Summon Request.");
		ZPort.chat.message(requester, p.getName() + " Accepted your Summon Request.");

		return true;
	}

}
