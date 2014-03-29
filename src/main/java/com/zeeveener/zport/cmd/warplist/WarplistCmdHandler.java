package com.zeeveener.zport.cmd.warplist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WarplistCmdHandler implements CommandExecutor{

	/**
	 * Ensures the arguments conform to the proper format.
	 * This method then sends the good command information to a new WarpList which handles sending the data to the CommandSender
	 */
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		
		return true;
	}
}
