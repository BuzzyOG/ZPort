package com.zeeveener.zport.cmd.tp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZUtils;
import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.checks.Cooldown;
import com.zeeveener.zport.checks.TpBlocks;
import com.zeeveener.zport.checks.Warmup;

public class TpHere implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){

		if(!(s instanceof Player)){
			ZPort.chat.error(s, "Console cannot summon players to itself.");
			return true;
		}

		if(args.length != 1){
			ZPort.chat.error(s, "Invalid Number of Arguments.");
			return false;
		}

		Player p = (Player) s;

		if(!ZPort.config.getBoolean("Feature.Teleportation", false) && !s.hasPermission("zp.exempt.toggles.tele")){
			ZPort.chat.error(s, "Teleportation features have been disabled.");
			return true;
		}

		Player get;

		if((get = ZUtils.getPlayerByName(args[0])) == null || !get.isOnline()){
			ZPort.chat.error(s, "That player is offline.");
			return true;
		}

		if(!p.hasPermission("zp.tp.here.player")){
			ZPort.chat.error(s, "You don't have permission to TP Players to You.");
			return true;
		}
		if(get.isOp() && !p.hasPermission("zp.tp.here.op")){
			ZPort.chat.error(s, "You don't have permission to TP Ops to You.");
			return true;
		}

		if(TpBlocks.isBlocking(get) && !p.hasPermission("zp.exempt.tpblocks")){
			ZPort.chat.error(s, get.getName() + " has Teleportation Blocked.");
			return true;
		}

		if(!Cooldown.doneCooldown(p, "tele")){
			ZPort.chat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}

		Warmup.warmup(get, ZPort.config.getInt("Warmup.Teleport", 0), p.getLocation());
		new Cooldown(p, "tele", ZPort.config.getInt("Cooldown.Teleport", 0));
		ZPort.chat.message(s, "You have Teleported " + ZPort.chat.m + get.getName() + ZPort.chat.g + " to yourself.");
		ZPort.chat.message(get, ZPort.chat.m + p.getName() + ZPort.chat.g + " has teleported you to themselves.");

		return true;
	}
}
