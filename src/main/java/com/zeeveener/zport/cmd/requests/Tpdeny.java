package com.zeeveener.zport.cmd.requests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;

public class Tpdeny implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(!(s instanceof Player)){
			ZChat.error(s, "Only players can Deny Teleport Requests at this time.");
			return true;
		}
		if(args.length != 0){ return false; }

		Player p = (Player) s;
		if(!p.hasPermission("zp.request.deny.tpa")){
			ZChat.error(s, "You don't have permission to use /tpad");
			return true;
		}

		Request r = Request.getFromCache(p.getUniqueId());
		if(r == null || r.getTeleportRequester() == null){
			ZChat.error(s, "You don't have any Teleport Requests at this time.");
			return true;
		}

		r.teleportRequestFrom(null);

		ZChat.message(s, "Denied Teleport Request.");
		Player requester = Bukkit.getPlayer(r.getTeleportRequester());
		if(requester != null){
			ZChat.message(requester, p.getName() + " Denied your Teleport Request.");
		}

		return true;
	}

}
