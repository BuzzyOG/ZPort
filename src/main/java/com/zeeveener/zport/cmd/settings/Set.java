package com.zeeveener.zport.cmd.settings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.ZPort;

public class Set implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label,
			String[] args) {
		
		if(args.length == 0){
			ZChat.error(s, "Invalid Number of Arguments");
			return false;
		}
		
		/*
		 * Toggle Plugin Features
		 */
		if(args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("t")){
			if(args.length != 2){
				ZChat.error(s, "Usage: /zpset toggle [warp/home/tele/sign]");
				return true;
			}
			boolean old;
			String enabled = "Enabled";
			if(args[1].equalsIgnoreCase("warp") || args[1].equalsIgnoreCase("w")){
				if(!s.hasPermission("zp.set.toggle.warp")){
					ZChat.error(s, "You don't have permission to toggle Warps.");
					return true;
				}
				
				old = ZPort.config.getBoolean("Feature.Warp", false);
				ZPort.config.set("Feature.Warp", !old);
				if(old) enabled = "Disabled";
				ZChat.message(s, "Warps have been " + enabled);
			}else if(args[1].equalsIgnoreCase("tele") || args[1].equalsIgnoreCase("t")){
				if(!s.hasPermission("zp.set.toggle.tele")){
					ZChat.error(s, "You don't have permission to toggle Teleportation.");
					return true;
				}
				
				old = ZPort.config.getBoolean("Feature.Teleportation", false);
				ZPort.config.set("Feature.Teleportation", !old);
				if(old) enabled = "Disabled";
				ZChat.message(s, "Teleportation has been " + enabled);
			}else if(args[1].equalsIgnoreCase("home") || args[1].equalsIgnoreCase("h")){
				if(!s.hasPermission("zp.set.toggle.home")){
					ZChat.error(s, "You don't have permission to toggle Homes.");
					return true;
				}
				
				old = ZPort.config.getBoolean("Feature.Home", false);
				ZPort.config.set("Feature.Home", !old);
				if(old) enabled = "Disabled";
				ZChat.message(s, "Homes have been " + enabled);
			}else if(args[1].equalsIgnoreCase("sign") || args[1].equalsIgnoreCase("s")){
				if(!s.hasPermission("zp.set.toggle.sign")){
					ZChat.error(s, "You don't have permission to toggle WarpSigns.");
					return true;
				}
				
				old = ZPort.config.getBoolean("Feature.WarpSign", false);
				ZPort.config.set("Feature.WarpSign", !old);
				if(old) enabled = "Disabled";
				ZChat.message(s, "WarpSigns have been " + enabled);
			}else{
				ZChat.error(s, "Usage: /zpset toggle [warp/home/tele/sign]");
				return true;
			}
			return true;
		}
		
		/*
		 * Set Cooldown Periods
		 */
		if(args[0].equalsIgnoreCase("cooldown") || args[0].equalsIgnoreCase("cd")){
			if(args.length != 3){
				ZChat.error(s, "Usage: /zpset cooldown [warp/tele/home/back] (seconds)");
				return true;
			}
			int newTime = 0, oldTime = 0;
			try {
				newTime = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				ZChat.error(s, "Use an Integer. ('1' instead of '1.0' or 'one')");
				return true;
			}
			
			if(args[1].equalsIgnoreCase("warp") || args[1].equalsIgnoreCase("w")){
				if(!s.hasPermission("zp.set.cooldown.warp")){
					ZChat.error(s, "You don't have permission to set Warp Cooldowns.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Cooldown.Warp", 0);
				ZPort.config.set("Cooldown.Warp", newTime);
				ZChat.message(s, "Changed Warp Cooldown period from " + oldTime + "s to " + newTime + "s");
			}else if(args[1].equalsIgnoreCase("tele") || args[1].equalsIgnoreCase("t")){
				if(!s.hasPermission("zp.set.cooldown.tele")){
					ZChat.error(s, "You don't have permission to set Teleport Cooldowns.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Cooldown.Teleport", 0);
				ZPort.config.set("Cooldown.Teleport", newTime);
				ZChat.message(s, "Changed Teleport Cooldown period from " + oldTime + "s to " + newTime + "s");
			}else if(args[1].equalsIgnoreCase("home") || args[1].equalsIgnoreCase("h")){
				if(!s.hasPermission("zp.set.cooldown.home")){
					ZChat.error(s, "You don't have permission to set Home Cooldowns.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Cooldown.Home", 0);
				ZPort.config.set("Cooldown.Home", newTime);
				ZChat.message(s, "Changed Home Cooldown period from " + oldTime + "s to " + newTime + "s");
			}else if(args[1].equalsIgnoreCase("back") || args[1].equalsIgnoreCase("b")){
				if(!s.hasPermission("zp.set.cooldown.back")){
					ZChat.error(s, "You don't have permission to set Back Cooldowns.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Cooldown.Back", 0);
				ZPort.config.set("Cooldown.Back", newTime);
				ZChat.message(s, "Changed Back Cooldown period from " + oldTime + "s to " + newTime + "s");
			}else{
				ZChat.error(s, "Usage: /zpset cooldown [warp/tele/home/back] (seconds)");
				return true;
			}
			return true;
		}
		
		/*
		 * Set Warmup Periods
		 */
		if(args[0].equalsIgnoreCase("warmup") || args[0].equalsIgnoreCase("wu")){
			if(args.length != 3){
				ZChat.error(s, "Usage: /zpset warmup [warp/tele/home/back] (value)");
				return true;
			}
			int newTime = 0, oldTime = 0;
			try{
				newTime = Integer.parseInt(args[2]);
			}catch(NumberFormatException e){
				ZChat.error(s, "Use an Integer. ('1' instead of '1.0' or 'one')");
				return true;
			}
			
			if(args[1].equalsIgnoreCase("warp") || args[1].equalsIgnoreCase("w")){
				if(!s.hasPermission("zp.set.warmup.warp")){
					ZChat.error(s, "You don't have permission to set Warp Warmups.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Warmup.Warp", 0);
				ZPort.config.set("Warmup.Warp", newTime);
				ZChat.message(s, "Changed Warp Warmup from " + oldTime + "s to " + newTime + "s");
			}else if(args[1].equalsIgnoreCase("home") || args[1].equalsIgnoreCase("h")){
				if(!s.hasPermission("zp.set.warmup.home")){
					ZChat.error(s, "You don't have permission to set Home Warmups.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Warmup.Home", 0);
				ZPort.config.set("Warmup.Home", newTime);
				ZChat.message(s, "Changed Home Warmup from " + oldTime + "s to " + newTime + "s");
			}else if(args[1].equalsIgnoreCase("tele") || args[1].equalsIgnoreCase("t")){
				if(!s.hasPermission("zp.set.warmup.tele")){
					ZChat.error(s, "You don't have permission to set Teleport Warmups.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Warmup.Teleport", 0);
				ZPort.config.set("Warmup.Teleport", newTime);
				ZChat.message(s, "Changed Teleport Warmup from " + oldTime + "s to " + newTime + "s");
			}else if(args[1].equalsIgnoreCase("back") || args[1].equalsIgnoreCase("b")){
				if(!s.hasPermission("zp.set.warmup.back")){
					ZChat.error(s, "You don't have permission to set Back Warmups.");
					return true;
				}
				
				oldTime = ZPort.config.getInt("Warmup.Back", 0);
				ZPort.config.set("Warmup.Back", newTime);
				ZChat.message(s, "Changed Back Warmup from " + oldTime + "s to " + newTime + "s");
			}else{
				ZChat.error(s, "Usage: /zpset warmup [warp/tele/home/back] (value)");
				return true;
			}
			return true;
		}
		
		return true;
	}

}
