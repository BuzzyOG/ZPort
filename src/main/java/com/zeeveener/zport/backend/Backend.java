package com.zeeveener.zport.backend;

import java.io.File;

import com.zeeveener.zport.ZPort;

public class Backend{

	private static SQL		sql;
	private static String	type;
	private static File		signFolder, warpFolder, homeFolder;

	public Backend(ZPort plugin){
		type = ZPort.config.getString("Backend.Type", "file");
		if(type.equalsIgnoreCase("mysql")){
			String host = ZPort.config.getString("Backend.MySQL.Host", "localhost");
			String db = ZPort.config.getString("Backend.MySQL.Database", "mcdb");
			String user = ZPort.config.getString("Backend.MySQL.User", "root");
			String pass = ZPort.config.getString("Backend.MySQL.Pass", "toor");
			int port = ZPort.config.getInt("Backend.MySQL.Port", 3306);
			sql = new SQL(plugin, host, db, user, pass, port);
		}else if(type.equalsIgnoreCase("sqlite")){
			sql = new SQL(plugin, "Data.db");
		}else{
			signFolder = new File(plugin.getDataFolder(), "Signs");
			if(!signFolder.exists()) signFolder.mkdirs();
			warpFolder = new File(plugin.getDataFolder(), "Warps");
			if(!warpFolder.exists()) warpFolder.mkdirs();
			homeFolder = new File(plugin.getDataFolder(), "Homes");
			if(!homeFolder.exists()) homeFolder.mkdirs();
		}
	}

	public static File getWarpFolder(){
		return warpFolder;
	}

	public static File getHomeFolder(){
		return homeFolder;
	}

	public static File getSignFolder(){
		return signFolder;
	}

	public static SQL getSQL(){
		return sql;
	}

	public static String getType(){
		return type;
	}

	public static void setType(String s){
		type = s;
	}

	public synchronized static boolean isSQL(){
		updateType();
		return (type.equalsIgnoreCase("mysql") || type.equalsIgnoreCase("sqlite"));
	}

	public synchronized static void updateType(){
		if(!ZPort.config.getString("Backend.Type", "file").equalsIgnoreCase(type)){
			String old = type;
			type = ZPort.config.getString("Backend.Type", "file");
			ZPort.chat.toOps("Backend has Changed from " + ZPort.chat.m + old + ZPort.chat.g + " to " + ZPort.chat.m + type);
			ZPort.chat.toConsole("Backend has Changed from " + old + " to " + type);
		}
	}
}
