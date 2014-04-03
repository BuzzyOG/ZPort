package com.zeeveener.zport.cmd.warplist;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zcore.bukkit.ZUtils;
import com.zeeveener.zport.backend.Backend;
import com.zeeveener.zport.objects.Warp;

/**
 * Takes good command information from WarplistCmdHandler and sends the relevant
 * information to the CommandSender <br>
 * We know the data is good because WarplistCmdHandler ensures the arguments are
 * properly formatted.
 */
public class WarpList{
	public WarpList(final CommandSender s, final String[] args){
		Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getServer().getPluginManager().getPlugin("ZPort"), new Runnable(){
			@Override
			public void run(){
				List<Warp> warps = getAllWarps();
				String title = "List of Warps: " + ZChat.m;
				boolean a = false, n = false, u = false, byW = false, byP = false;
				for(int i = 0; i < args.length - 1; i += 2){
					String op = args[i];
					String value = args[i + 1];

					if(op.equalsIgnoreCase("-a")){
						if(a){
							continue;
						}else if(s instanceof Player && !((Player) s).hasPermission("zp.warp.list.byAge")){
							ZChat.error(s, "You don't have permission to sort by Warp Age.");
							return;
						}else if(value.equalsIgnoreCase("a")){
							Sort.sortByAgeAscending(warps);
							title = title + "Age-A ";
						}else if(value.equalsIgnoreCase("d")){
							Sort.sortByAgeDescending(warps);
							title = title + "Age-D ";
						}else{
							ZChat.error(s, "I had a problem sorting by age because I don't know what " + ZChat.m + value + " means.");
							return;
						}
						a = true;
					}

					if(op.equalsIgnoreCase("-n")){
						if(n){
							continue;
						}else if(s instanceof Player && !((Player) s).hasPermission("zp.warp.list.byName")){
							ZChat.error(s, "You don't have permission to sort by Warp Name.");
							return;
						}else if(value.equalsIgnoreCase("a")){
							Sort.sortByNameAscending(warps);
							title = title + "Name-A ";
						}else if(value.equalsIgnoreCase("d")){
							Sort.sortByNameDescending(warps);
							title = title + "Name-D ";
						}else{
							ZChat.error(s, "I had a problem sorting by name because I don't know what " + ZChat.m + value + " means.");
							return;
						}
						n = true;
					}

					if(op.equalsIgnoreCase("-u")){
						if(u){
							continue;
						}else if(s instanceof Player && !((Player) s).hasPermission("zp.warp.list.byUses")){
							ZChat.error(s, "You don't have permission to sort by Warp Uses.");
							return;
						}else if(value.equalsIgnoreCase("a")){
							Sort.sortByUsesAscending(warps);
							title = title + "Uses-A ";
						}else if(value.equalsIgnoreCase("d")){
							Sort.sortByUsesDescending(warps);
							title = title + "Uses-D ";
						}else{
							ZChat.error(s, "I had a problem sorting by uses because I don't know what " + ZChat.m + value + " means.");
							return;
						}
						u = true;
					}

					if(op.equalsIgnoreCase("-w")){
						if(byW){
							continue;
						}
						for(Warp w : warps){
							if(!w.getLocation().getWorld().getName().equalsIgnoreCase(value)){
								warps.remove(w);
							}
						}
						title = title + "World-" + value + " ";
						byW = true;
					}

					if(op.equalsIgnoreCase("-p")){
						if(byP){
							continue;
						}
						Player p = ZUtils.getPlayerByName(value);
						for(Warp w : warps){
							if(!w.getOwner().toString().equals(p.getUniqueId().toString())){
								warps.remove(w);
							}else if(s instanceof Player){
								Player sp = (Player) p;
								if(p.getName().equals(sp.getName())){
									if(w.getPrivate()){
										if(!sp.hasPermission("zp.warp.list.own.private")){
											warps.remove(w);
										}
									}else{
										if(!sp.hasPermission("zp.warp.list.own.public")){
											warps.remove(w);
										}
									}
								}else{
									if(w.getPrivate()){
										if(!sp.hasPermission("zp.warp.list.others.private")){
											warps.remove(w);
										}
									}else{
										if(!sp.hasPermission("zp.warp.list.others.public")){
											warps.remove(w);
										}
									}
								}
							}
						}
						title = title + "Player-" + value + " ";
						byP = true;
					}
				}

				String msg = "";
				for(Warp w : warps){
					if(w.getPrivate()){
						msg = msg + ZChat.e + w.getName() + " ";
					}else{
						msg = msg + ZChat.g + w.getName() + " ";
					}
				}
				ZChat.message(s, title);
				ZChat.message(s, msg);
			}
		});
	}

	private synchronized List<Warp> getAllWarps(){
		List<Warp> list = new ArrayList<Warp>();
		if(Backend.isSQL()){
			ResultSet set = Backend.getSQL().preparedQuery("SELECT name FROM zp_warps;", null);
			try{
				while(set.next()){
					list.add(Warp.getWarp(set.getString("name")));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}else{
			File folder = Backend.getWarpFolder();
			for(File f : folder.listFiles()){
				FileConfiguration c = YamlConfiguration.loadConfiguration(f);
				list.add(Warp.loadFromString(c.getString("Object")));
			}
		}
		return list;
	}
}
