package com.mcmarket.profile;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.craftbukkit.v1_7_R4.generator.InternalChunkGenerator;

import com.mcmarket.SQLStats;

public class SQLStatsProfile {

	public static boolean playerExits(String uuid) {
		try {

			ResultSet st = SQLStats.mysql.query("SELECT * FROM Stats WHERE UUID= '" + uuid + "'");

			if (st.next()) {
				return st.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void createPlayer(String uuid) {
		if (!(playerExits(uuid))) {
			SQLStats.mysql.update("INSERT INTO Stats(UUID, KILLS, DEATHS) VALUES(" + uuid + "), 0, 0");
		}
	}

	public static Integer getKills(String uuid) {
		Integer i = 0;

		if (playerExits(uuid)) {

			try {
				ResultSet st = SQLStats.mysql.query("SELECT * FROM Stats WHERE UUID= '" + uuid + "'");
				if ((!st.next()) || (Integer.valueOf(st.getInt("KILLS")) == null))
					;

				i = st.getInt("KILLS");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			createPlayer(uuid);
			getKills(uuid);
		}

		return i;
	}

	public static Integer getDeaths(String uuid) {
		Integer i = 0;

		if (playerExits(uuid)) {

			try {
				ResultSet rs = SQLStats.mysql.query("SELECT * FROM Stats WHERE UUID= '" + uuid + "'");
				if ((!rs.next()) || (Integer.valueOf(rs.getInt("DEATHS")) == null))
					;

				i = rs.getInt("DEATHS");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			createPlayer(uuid);
			getKills(uuid);
		}

		return i;
	}

	public static void setKills(String uuid, Integer kills) {
		if (playerExits(uuid)) {
			SQLStats.mysql.update("UPDATE Stats SET KILLS= '" + kills + "' WHERE= UUID= '" + uuid + "';");
		} else {
			createPlayer(uuid);
			setKills(uuid, kills);
		}
	}

	public static void setDeaths(String uuid, Integer deaths) {
		if (playerExits(uuid)) {
			SQLStats.mysql.update("UPDATE Stats SET KILLS= '" + deaths + "' WHERE= UUID= '" + uuid + "';");
		} else {
			createPlayer(uuid);
			setDeaths(uuid, deaths);
		}
	}

	public static void addKills(String uuid, Integer kills) {
		if (playerExits(uuid)) {
			setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + kills.intValue()));
		} else {
			createPlayer(uuid);
			addKills(uuid, kills);
		}
	}

	public static void addDeaths(String uuid, Integer deaths) {
		if (playerExits(uuid)) {
			setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + deaths.intValue()));
		} else {
			createPlayer(uuid);
			addKills(uuid, deaths);
		}
	}

	public static void removeKills(String uuid, Integer kills) {
		if (playerExits(uuid)) {
			setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + kills.intValue()));
		} else {
			createPlayer(uuid);
			removeKills(uuid, kills);
		}
	}

	public static void removeDeaths(String uuid, Integer deaths) {
		if (playerExits(uuid)) {
			setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + deaths.intValue()));
		} else {
			createPlayer(uuid);
			removeDeaths(uuid, deaths);
		}
	}
}
