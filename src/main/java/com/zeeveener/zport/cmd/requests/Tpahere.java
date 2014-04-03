package com.zeeveener.zport.cmd.requests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zcore.bukkit.ZUtils;

public class Tpahere implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){

		if(!(s instanceof Player)){
			ZChat.error(s, "Only players can send Teleport Requests at this time.");
			return true;
		}
		if(args.length != 1){
			return false;
		}
		
		Player p = (Player)s;
		Player summoned = ZUtils.getPlayerByName(args[0]);
		
		if(!p.hasPermission("zp.request.tpahere")){
			ZChat.error(s, "You don't have permission to use /tpahere");
			return true;
		}
		if(summoned == null){
			ZChat.error(s, "That player is not online.");
			return true;
		}
		
		Request r = Request.getFromCache(summoned.getUniqueId());
		if(r == null){
			r = new Request(summoned.getUniqueId());
			Request.addToCache(r);
		}
		r.summonRequestFrom(p.getUniqueId());
		
		List<String> msg = new ArrayList<String>();
		msg.add("To ACCEPT: " + ZChat.m + "/tpha");
		msg.add("To DENY: " + ZChat.m + "/tphd");
		ZChat.message(summoned, "Summon Request from " + p.getName(), msg.toArray(new String[0]));
		
		ZChat.message(s, "Summon Request Sent.");
		
		return true;
	}
}
