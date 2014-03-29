package com.zeeveener.zport.cmd.warplist;

import org.bukkit.command.CommandSender;

/**
 * Takes good command information from WarplistCmdHandler and sends the relevant information to the CommandSender
 * <br>We know the data is good because WarplistCmdHandler ensures the arguments are properly formatted.
 */
public class WarpList {

	
	public WarpList(CommandSender s, String[] args){ 
		//Should we create multiple constructors which contain individual arguments instead of parsing the array twice? (Once in WLCH, once in here.)
	}
}
