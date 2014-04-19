package com.zeeveener.zport.cmd.requests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZUtils;
import com.zeeveener.zport.ZPort;

public class Tpahere implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){

		if(!(s instanceof Player)){
			ZPort.chat.error(s, "Only players can send Teleport Requests at this time.");
			return true;
		}
		if(args.length != 1){ return false; }

		Player p = (Player) s;
		Player summoned = ZUtils.getPlayerByName(args[0]);

		if(!p.hasPermission("zp.request.tpahere")){
			ZPort.chat.error(s, "You don't have permission to use /tpahere");
			return true;
		}
		if(summoned == null){
			ZPort.chat.error(s, "That player is not online.");
			return true;
		}

		Request r = Request.getFromCache(summoned.getUniqueId());
		if(r == null){
			r = new Request(summoned.getUniqueId());
			Request.addToCache(r);
		}
		r.summonRequestFrom(p.getUniqueId());

		List<String> msg = new ArrayList<String>();
		msg.add("To ACCEPT: " + ZPort.chat.m + "/tpha");
		msg.add("To DENY: " + ZPort.chat.m + "/tphd");
		ZPort.chat.message(summoned, "Summon Request from " + p.getName(), msg.toArray(new String[0]));

		ZPort.chat.message(s, "Summon Request Sent.");

		return true;
	}
}
