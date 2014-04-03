package com.zeeveener.zport.cmd.warplist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zcore.bukkit.ZUtils;
import com.zeeveener.zport.ZPort;

public class WarplistCmdHandler implements CommandExecutor{

	/**
	 * Ensures the arguments conform to the proper format.
	 * This method then sends the good command information to a new WarpList which handles sending the data to the CommandSender
	 */
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!ZPort.config.getBoolean("Feature.Warp", false)){
			ZChat.error(s, "Warps are not Enabled.");
			return true;
		}
		
		if(args.length <= 1){
			List<String> m = new ArrayList<String>();
			String title = "WarpList Command Help";
			m.add("Command: " + ZChat.m + "/list (Arg) (Value) (Arg) (Value)...");
			m.add("--Args--");
			m.add("-w (World)\t List warps from one world.");
			m.add("-p (Player)\t List an ONLINE Players Warps.");
			m.add("-a (A/D)\t Sort by age.");
			m.add("-u (A/D)\t Sort by uses.");
			m.add("-n (A/D)\t Sort alphabetically.");
			ZChat.message(s, title, m.toArray(new String[0]));
			return true;
		}
		
		if(args.length >= 2){
			if(args.length % 2 != 0){
				ZChat.error(s, "Ensure your arguments are formatted properly.");
				return false;
			}
			for(int i = 0; i < args.length-1; i+=2){
				String op = args[i];
				String value = args[i+1];
				if(!op.equalsIgnoreCase("-a") && !op.equalsIgnoreCase("-u") && !op.equalsIgnoreCase("-n") && !op.equalsIgnoreCase("-w") && !op.equalsIgnoreCase("-p")){
					ZChat.error(s, "I'm not sure what '" + op + "' means.");
					return false;
				}
				
				if(op.equalsIgnoreCase("-a") || op.equalsIgnoreCase("-u") || op.equalsIgnoreCase("-n") ||
						op.equalsIgnoreCase("-age") || op.equalsIgnoreCase("-uses") || op.equalsIgnoreCase("-name")){
					if(!value.equalsIgnoreCase("a") && !value.equalsIgnoreCase("asc") && !value.equalsIgnoreCase("ascending")
							&& !value.equalsIgnoreCase("d") && !value.equalsIgnoreCase("des") && !value.equalsIgnoreCase("descending")){
						ZChat.error(s, "To use '" + op + "', you must type either 'A' or 'D' afterwards.");
						ZChat.error(s, "Example: /list -a A");
						return false;
					}
					args[i] = args[i].substring(0, 2);
					args[i+1] = args[i+1].substring(0, 1);
				}
				
				if(op.equalsIgnoreCase("-w") || op.equalsIgnoreCase("-world")){
					if(s instanceof Player){
						if(!((Player)s).hasPermission("zp.warp.list.byWorld")){
							ZChat.error(s, "You don't have permission to list warps per world.");
							return true;
						}
					}
					if(Bukkit.getWorld(value) == null){
						ZChat.error(s, "The requested World doesn't exist.");
						return false;
					}
					args[i] = "-w";
				}
				
				if(op.equalsIgnoreCase("-p") || op.equalsIgnoreCase("-player")){
					if(s instanceof Player){
						if(!((Player)s).hasPermission("zp.warp.list.others.private") && !((Player)s).hasPermission("zp.warp.list.others.public")
								&& !((Player)s).hasPermission("zp.warp.list.own.private") && !((Player)s).hasPermission("zp.warp.list.own.public")){
							ZChat.error(s, "You don't have permission to list warps per player.");
							return true;
						}
					}
					if(ZUtils.getPlayerByName(value) == null){
						ZChat.error(s, "The requested player must be online.");
						return false;
					}
					args[i] = "-p";
				}
			}
			
			new WarpList(s, args);
		}
		
		
		return true;
	}
}
