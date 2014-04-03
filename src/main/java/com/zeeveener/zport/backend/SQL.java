package com.zeeveener.zport.backend;

import com.zeeveener.zcore.bukkit.ZSQL;
import com.zeeveener.zport.ZPort;

public class SQL extends ZSQL{

	public SQL(ZPort instance, String h, String d, String u, String p, int prt){
		super(instance, h, d, u, p, prt);
		createDefaultTables();
	}

	public SQL(ZPort instance, String dbFile){
		super(instance, dbFile);
		createDefaultTables();
	}

	@Override
	protected void createDefaultTables(){
		this.preparedUpdate("CREATE TABLE IF NOT EXISTS zp_warps(" + "id INT(5) PRIMARY KEY AUTO_INCREMENT, " + "date_created BIGINT NOT NULL, "
				+ "last_used BIGINT NOT NULL, " + "total_uses INT(6) NOT NULL DEFAULT 0, " + "name VARCHAR(16) UNIQUE KEY NOT NULL DEFAULT '', "
				+ "owner VARCHAR(36) NOT NULL DEFAULT '', " + "private BOOLEAN NOT NULL DEFAULT 0," + "x DOUBLE NOT NULL DEFAULT 0.0, "
				+ "y DOUBLE NOT NULL DEFAULT 0.0, " + "z DOUBLE NOT NULL DEFAULT 0.0, " + "yaw FLOAT NOT NULL DEFAULT 0.0, "
				+ "pitch FLOAT NOT NULL DEFAULT 0.0, " + "world VARCHAR(16) NOT NULL DEFAULT '') ENGINE=InnoDB;", new Object[] {});
		this.preparedUpdate("CREATE TABLE IF NOT EXISTS zp_homes(" + "id INT(5) PRIMARY KEY AUTO_INCREMENT, " + "owner VARCHAR(36) NOT NULL DEFAULT '', "
				+ "x DOUBLE NOT NULL DEFAULT 0.0, " + "y DOUBLE NOT NULL DEFAULT 0.0, " + "z DOUBLE NOT NULL DEFAULT 0.0, "
				+ "yaw FLOAT NOT NULL DEFAULT 0.0, " + "pitch FLOAT NOT NULL DEFAULT 0.0, " + "world VARCHAR(16) NOT NULL DEFAULT '') ENGINE=InnoDB;",
				new Object[] {});
		this.preparedUpdate("CREATE TABLE IF NOT EXISTS zp_signs(" + "id INT(5) PRIMARY KEY AUTO_INCREMENT, " + "date_created BIGINT NOT NULL, "
				+ "last_used BIGINT NOT NULL, " + "total_uses INT(6) NOT NULL DEFAULT 0, " + "name VARCHAR(16) UNIQUE KEY NOT NULL DEFAULT '', "
				+ "target VARCHAR(16) NOT NULL DEFAULT '', " + "owner VARCHAR(36) NOT NULL DEFAULT '', " + "private BOOLEAN NOT NULL DEFAULT 0,"
				+ "x DOUBLE NOT NULL DEFAULT 0.0, " + "y DOUBLE NOT NULL DEFAULT 0.0, " + "z DOUBLE NOT NULL DEFAULT 0.0, "
				+ "yaw FLOAT NOT NULL DEFAULT 0.0, " + "pitch FLOAT NOT NULL DEFAULT 0.0, " + "world VARCHAR(16) NOT NULL DEFAULT '') ENGINE=InnoDB;",
				new Object[] {});
	}

}
