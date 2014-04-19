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

public class Tp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args){

		if(!(s instanceof Player)){
			ZPort.chat.error(s, "Console cannot teleport to players.");
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

		Player to = ZUtils.getPlayerByName(args[0]);

		if(to == null || !to.isOnline()){
			ZPort.chat.error(s, "That player is offline.");
			return true;
		}

		if(!p.hasPermission("zp.tp.to.player")){
			ZPort.chat.error(s, "You don't have permission to TP to players.");
			return true;
		}
		if(to.isOp() && !p.hasPermission("zp.tp.to.op")){
			ZPort.chat.error(s, "You don't have permission to TP to ops.");
			return true;
		}

		if(TpBlocks.isBlocking(to) && !p.hasPermission("zp.exempt.tpblocks")){
			ZPort.chat.error(s, to.getName() + " has Teleportation Blocked.");
			return true;
		}

		if(!Cooldown.doneCooldown(p, "tele")){
			ZPort.chat.error(s, "You must wait for the Cooldown period to elapse first...");
			return true;
		}

		Warmup.warmup(p, ZPort.config.getInt("Warmup.Teleport", 0), to.getLocation());
		new Cooldown(p, "tele", ZPort.config.getInt("Cooldown.Teleport", 0));
		ZPort.chat.message(s, "You have Teleported to: " + ZPort.chat.m + to.getName());
		ZPort.chat.message(to, ZPort.chat.m + p.getName() + ZPort.chat.g + " has Teleported to you.");

		return true;
	}
}