package com.zeeveener.zport.cmd.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.objects.Home;

public class SetHome implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){

		if(!(s instanceof Player)){
			ZPort.chat.error(s, "Console cannot create a home.");
			return true;
		}

		Player p = (Player) s;

		if(!ZPort.config.getBoolean("Feature.Home", false) && !s.hasPermission("zp.exempt.toggles.home")){
			ZPort.chat.error(s, "Home features have been disabled.");
			return true;
		}

		if(!p.hasPermission("zp.home.set")){
			ZPort.chat.error(s, "You don't have permission to set homes in this world.");
			return true;
		}

		Home h = Home.createHome(p);
		ZPort.chat.message(s, "To return here: " + ZPort.chat.m + "/home " + h.getLocation().getWorld().getName());

		return true;
	}

}
