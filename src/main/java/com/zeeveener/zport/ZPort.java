package com.zeeveener.zport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.zeeveener.zcore.bukkit.ZChat;
import com.zeeveener.zport.backend.Backend;
import com.zeeveener.zport.backend.Config;
import com.zeeveener.zport.cmd.home.SetHome;
import com.zeeveener.zport.cmd.home.UseHome;
import com.zeeveener.zport.cmd.requests.Tpa;
import com.zeeveener.zport.cmd.requests.Tpaccept;
import com.zeeveener.zport.cmd.requests.Tpahere;
import com.zeeveener.zport.cmd.requests.Tpdeny;
import com.zeeveener.zport.cmd.requests.Tphaccept;
import com.zeeveener.zport.cmd.requests.Tphdeny;
import com.zeeveener.zport.cmd.settings.Set;
import com.zeeveener.zport.cmd.tp.Tp;
import com.zeeveener.zport.cmd.tp.TpHere;
import com.zeeveener.zport.cmd.tp.TpTo;
import com.zeeveener.zport.cmd.tp.related.Back;
import com.zeeveener.zport.cmd.warp.RemoveWarp;
import com.zeeveener.zport.cmd.warp.SetWarp;
import com.zeeveener.zport.cmd.warp.UseWarp;
import com.zeeveener.zport.cmd.warplist.WarplistCmdHandler;
import com.zeeveener.zport.events.PlayerLogin;
import com.zeeveener.zport.events.PlayerLogout;
import com.zeeveener.zport.events.PlayerTeleport;
import com.zeeveener.zport.objects.Warp;
import com.zeeveener.zport.objects.WarpSign;

public class ZPort extends JavaPlugin{

	/*
	 * TODO After those are done, release v1.0!
	 */

	public static Config	config;

	public void onEnable(){
		config = new Config(this);
		new Backend(this);
		new ZChat(this);

		Warp.startCacheThread();
		WarpSign.startCacheThread();

		this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLogin(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLogout(), this);

		this.getCommand("ztpa").setExecutor(new Tpa());
		this.getCommand("ztpahere").setExecutor(new Tpahere());
		this.getCommand("ztpaccept").setExecutor(new Tpaccept());
		this.getCommand("ztphaccept").setExecutor(new Tphaccept());
		this.getCommand("ztpdeny").setExecutor(new Tpdeny());
		this.getCommand("ztphdeny").setExecutor(new Tphdeny());
		this.getCommand("zwarp").setExecutor(new UseWarp());
		this.getCommand("zsetwarp").setExecutor(new SetWarp());
		this.getCommand("zremwarp").setExecutor(new RemoveWarp());
		this.getCommand("zlistwarp").setExecutor(new WarplistCmdHandler());
		this.getCommand("zhome").setExecutor(new UseHome());
		this.getCommand("zsethome").setExecutor(new SetHome());
		this.getCommand("ztp").setExecutor(new Tp());
		this.getCommand("ztphere").setExecutor(new TpHere());
		this.getCommand("ztpto").setExecutor(new TpTo());
		this.getCommand("zpset").setExecutor(new Set());
		this.getCommand("zback").setExecutor(new Back());
	}

	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
	}
}
