package com.zeeveener.zport.cmd.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.objects.Home;

public class SetHome implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!(s instanceof Player)){
			ZChat.error(s, "Console cannot create a home.");
			return true;
		}
		
		Player p = (Player)s;
		
		if(!p.hasPermission("zp.home.set")){
			ZChat.error(s, "You don't have permission to set homes in this world.");
			return true;
		}
		
		Home h = Home.createHome(p);
		ZChat.message(s, "To return here: " + ZChat.m + "/home " + h.getLocation().getWorld().getName());
		
		return true;
	}

}
