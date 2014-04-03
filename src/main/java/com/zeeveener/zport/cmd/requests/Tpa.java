package com.zeeveener.zport.cmd.requests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zcore.bukkit.ZUtils;

public class Tpa implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){

		if(!(s instanceof Player)){
			ZChat.error(s, "Only players can send Teleport Requests at this time.");
			return true;
		}
		if(args.length != 1){ return false; }

		Player to = ZUtils.getPlayerByName(args[0]);
		Player p = (Player) s;

		if(!p.hasPermission("zp.request.tpa")){
			ZChat.error(s, "You don't have permission to use /tpa");
			return true;
		}
		if(to == null){
			ZChat.error(s, "That player is not online.");
			return true;
		}

		Request r = Request.getFromCache(to.getUniqueId());
		if(r == null){
			r = new Request(to.getUniqueId());
			Request.addToCache(r);
		}
		r.teleportRequestFrom(p.getUniqueId());

		List<String> msg = new ArrayList<String>();
		msg.add("To ACCEPT: " + ZChat.m + "/tpaa");
		msg.add("To DENY: " + ZChat.m + "/tpad");
		ZChat.message(to, "Teleport Request from " + p.getName(), msg.toArray(new String[0]));

		ZChat.message(s, "Teleport Request Sent.");

		return true;
	}
}
