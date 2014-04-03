package com.zeeveener.zport.cmd.requests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;

public class Tphdeny implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){
		if(!(s instanceof Player)){
			ZChat.error(s, "Only players can Deny Teleport Requests at this time.");
			return true;
		}
		if(args.length != 0){ return false; }

		Player p = (Player) s;
		if(!p.hasPermission("zp.request.deny.tpahere")){
			ZChat.error(s, "You don't have permission to use /tphd");
			return true;
		}

		Request r = Request.getFromCache(p.getUniqueId());
		if(r == null || r.getSummonRequester() == null){
			ZChat.error(s, "You don't have any Teleport Requests at this time.");
			return true;
		}

		r.summonRequestFrom(null);

		ZChat.message(s, "Denied Summon Request.");
		Player requester = Bukkit.getPlayer(r.getSummonRequester());
		if(requester != null){
			ZChat.message(requester, p.getName() + " Denied your Summon Request.");
		}

		return true;
	}

}
