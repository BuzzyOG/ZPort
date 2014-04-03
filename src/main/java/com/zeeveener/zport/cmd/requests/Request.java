package com.zeeveener.zport.cmd.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Request{

	private static List<Request> requests = new ArrayList<Request>();
	private static Object lock = new Object();
	
	private final UUID player;
	private UUID playerToMe = null;
	private UUID meToPlayer = null;
//	private static final long TIMEOUT = 60*20*1000L;
	
	public Request(UUID p){
		player = p;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(player);
	}
	public UUID getPlayerUUID(){
		return player;
	}
	
	private void cleanUp(){
		if(playerToMe == null && meToPlayer == null){
			Request.removeFromCache(player);
		}
	}
	
	public synchronized void summonRequestFrom(UUID p){
		meToPlayer = p;
		this.cleanUp();
	}
	public synchronized void teleportRequestFrom(UUID p){
		playerToMe = p;
		this.cleanUp();
	}
	
	public synchronized UUID getSummonRequester(){
		return meToPlayer;
	}
	public synchronized UUID getTeleportRequester(){
		return playerToMe;
	}
	
	public synchronized void fulfillSummonRequest(){
		Player to = Bukkit.getPlayer(meToPlayer);
		Player from = Bukkit.getPlayer(player);
		if(from == null || to == null) return;
		from.teleport(to);
		this.cleanUp();
	}
	public synchronized void fulfillTeleportRequest(){
		Player from = Bukkit.getPlayer(playerToMe);
		Player to = Bukkit.getPlayer(player);
		if(from == null || to == null) return;
		from.teleport(to);
		this.cleanUp();
	}
	
	public synchronized static boolean existsInCache(UUID to){
		synchronized(lock){
			for(Request r : requests){
				if(r.getPlayerUUID().equals(to)){
					return true;
				}
			}
			return false;
		}
	}
	public synchronized static void addToCache(Request r){
		synchronized(lock){
			requests.add(r);
		}
	}
	public synchronized static void removeFromCache(UUID to){
		synchronized(lock){
			for(Request r : requests){
				if(r.getPlayerUUID().equals(to)){
					requests.remove(r);
				}
			}
		}
	}
	public synchronized static Request getFromCache(UUID to){
		synchronized(lock){
			for(Request r : requests){
				if(r.getPlayerUUID().equals(to)){
					return r;
				}
			}
			return null;
		}
	}
}
