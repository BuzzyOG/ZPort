package com.zeeveener.zport.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.zeeveener.zport.ZPort;
import com.zeeveener.zport.objects.WarpSign;

public class WarpSigns implements Listener{

	@EventHandler
	public void onSignPlace(SignChangeEvent e){
		if(!isWarpSign(e.getBlock())) return;
		if(ZPort.config.getBoolean("Feature.WarpSign", false)) return;

		Player p = e.getPlayer();
		if(!p.hasPermission("zp.warpsign.create.public")){
			e.setLine(0, "--Error--");
			e.setLine(1, "No");
			e.setLine(2, "Perms");
			e.setLine(3, "--Error--");
			return;
		}
		if(e.getLine(3).equalsIgnoreCase("private") && !p.hasPermission("zp.warpsign.create.private")){
			e.setLine(0, "--Error--");
			e.setLine(1, "No Perms");
			e.setLine(2, "For Private");
			e.setLine(3, "WarpSigns");
			return;
		}
		if(WarpSign.exists(e.getLine(1))){
			e.setLine(0, "--Error--");
			e.setLine(1, "Warp Name");
			e.setLine(2, "Already Taken");
			e.setLine(3, "--Error--");
			return;
		}

		WarpSign sign = WarpSign.create(p, e.getLine(1), e.getLine(3).equalsIgnoreCase("private"));
		;
		WarpSign target = WarpSign.getWarpSign(e.getLine(2));
		String targetMessage = ZPort.chat.e + " No Target";

		if(target == null){
			e.setLine(2, "-NoTarget-");
		}else if(target.getPrivate() && !p.hasPermission("zp.warpsign.target.private")){
			ZPort.chat.error(p, "You don't have permission to target private warpsigns.");
			e.setLine(2, "-NoTarget-");
		}else{
			sign.setTarget(target);
			targetMessage = " Target " + ZPort.chat.m + target.getName();
		}
		ZPort.chat.message(p, "Created WarpSign " + ZPort.chat.m + sign.getName() + "with" + targetMessage);
		ZPort.chat.toConsole(p.getName() + " created WarpSign: " + sign.getName());
	}

	@EventHandler
	public void onSignBreakByPlayer(BlockBreakEvent e){
		if(!isWarpSign(e.getBlock())) return;
		if(ZPort.config.getBoolean("Feature.WarpSign", false)) return;

		Player p = e.getPlayer();

		Sign s = (Sign) e.getBlock().getState();
		if(!WarpSign.exists(s.getLine(1))) return;
		WarpSign sign = WarpSign.getWarpSign(s.getLine(1));

		if(!sign.getOwner().toString().equalsIgnoreCase(p.getUniqueId().toString())){
			if(sign.getPrivate() && !p.hasPermission("zp.warpsign.destroy.others.private")){
				ZPort.chat.error(p, "You don't have permission to destroy other players' Private WarpSigns.");
				e.setCancelled(true);
				return;
			}else if(!sign.getPrivate() && !p.hasPermission("zp.warpsign.destroy.others.public")){
				ZPort.chat.error(p, "You don't have permission to destroy other players' Public WarpSigns.");
				e.setCancelled(true);
				return;
			}else{
				sign.delete();
			}
		}else if(sign.getPrivate()){
			if(!p.hasPermission("zp.warpsign.destroy.own.private")){
				ZPort.chat.error(p, "You don't have permission to destroy your Private WarpSigns.");
				e.setCancelled(true);
				return;
			}
			sign.delete();
		}else{
			if(!p.hasPermission("zp.warpsign.destroy.own.public")){
				ZPort.chat.error(p, "You don't have permission to destroy your own Public WarpSigns.");
				e.setCancelled(true);
				return;
			}
			sign.delete();
		}

		if(!WarpSign.exists(s.getLine(1))){
			ZPort.chat.message(p, "Destroyed WarpSign: " + s.getLine(1));
			ZPort.chat.toConsole(p.getName() + " Destroyed WarpSign: " + s.getLine(1));
		}else{
			ZPort.chat.error(p, "An Error Occurred... Unable to Destroy Warpsign.");
		}
	}

	@EventHandler
	public void onSignBreakByNotPlayer(BlockPhysicsEvent e){
		if(!isWarpSign(e.getBlock())) return;

		Sign s = (Sign) e.getBlock().getState();
		WarpSign sign = WarpSign.getWarpSign(s.getLine(1));
		sign.delete();
		ZPort.chat.toConsole("WarpSign " + s.getLine(1) + " was broken by natural forces.");
	}

	@EventHandler
	public void onSignClick(PlayerInteractEvent e){
		if(!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if(!isWarpSign(e.getClickedBlock())) return;
		if(ZPort.config.getBoolean("Feature.WarpSign", false)){
			ZPort.chat.error(e.getPlayer(), "WarpSigns have been disabled.");
			return;
		}

		Player p = e.getPlayer();
		Sign s = (Sign) e.getClickedBlock().getState();

		if(s.getLine(1).equalsIgnoreCase("{home}")){
			Bukkit.getServer().dispatchCommand(p, "zhome" + p.getLocation().getWorld().getName());
			return;
		}

		WarpSign sign = WarpSign.getWarpSign(s.getLine(1));
		if(sign.getTarget() == null){
			ZPort.chat.error(p, "The Target WarpSign doesn't exist.");
			return;
		}

		if(!sign.getOwner().toString().equalsIgnoreCase(p.getUniqueId().toString())){
			if(sign.getPrivate() && !p.hasPermission("zp.warpsign.use.others.private")){
				ZPort.chat.error(p, "You don't have permission to use other players' Private WarpSigns.");
				return;
			}else if(!sign.getPrivate() && !p.hasPermission("zp.warpsign.use.others.public")){
				ZPort.chat.error(p, "You don't have permission to use other players' Public WarpSigns.");
				return;
			}
		}else if(sign.getPrivate() && !p.hasPermission("zp.warpsign.use.own.private")){
			ZPort.chat.error(p, "You don't have permission to use your own Private WarpSigns.");
			return;
		}else if(!p.hasPermission("zp.warpsign.use.own.public")){
			ZPort.chat.error(p, "You don't have permission to use your own Public WarpSigns.");
			return;
		}

		sign.goTo(p);
		ZPort.chat.message(p, "You have arrived at " + sign.getTarget().getName());
	}

	private boolean isWarpSign(Block b){
		if(b.getType() != Material.SIGN && b.getType() != Material.SIGN_POST && b.getType() != Material.WALL_SIGN) return false;
		Sign e = (Sign) b.getState();
		if(e.getLines().length < 4) return false;
		if(!e.getLine(0).equalsIgnoreCase("{warp}")) return false;
		return true;
	}
}
