package rip.cobalt.database;

import java.net.UnknownHostException;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDB {

	private static MongoDB instance;

	public MongoDB() {
		instance = this;
	}

	private DBCollection players;
	private DBCollection staffplayers;
	private DBCollection security;
	private DB mc;
	private MongoClient client;

	public String ip;
	public int kills;
	public int deaths;
	public int diamond;
	public int redstone;
	public int coal;
	public int gold;
	public int lapis;
	public int iron;
	public int emerald;

	public String pwd;

	public boolean connect(String ip, int port) {
		// Default is localhost, 27017
		try {
			client = new MongoClient(ip, port);
		} catch (UnknownHostException e) {
			System.out.println("Ketamine - Could not connect to database!");
			e.printStackTrace();
			return false;
		}
		mc = client.getDB("mc");
		players = mc.getCollection("players");
		staffplayers = mc.getCollection("staff");
		security = mc.getCollection("security");
		return true;
	}

	public void addPlayer(Player p) {
		DBObject obj = new BasicDBObject("uuid", p.getUniqueId());
		obj.put("ip", p.getAddress().getAddress().getHostAddress());
		obj.put("kills", p.getStatistic(Statistic.PLAYER_KILLS));
		obj.put("deaths", p.getStatistic(Statistic.DEATHS));
		obj.put("diamond", p.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
		obj.put("redstone", p.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
		obj.put("coal", p.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
		obj.put("gold", p.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
		obj.put("lapis", p.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
		obj.put("iron", p.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
		obj.put("emerald", p.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
		players.insert(obj);

	}

	public void readPlayer(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		// Use findOne to only get one object!
		DBObject found = players.findOne(r);
		ip = (String) found.get("ip");
		kills = (int) found.get("kills");
		diamond = (int) found.get("diamond");
		redstone = (int) found.get("redstone");
		coal = (int) found.get("coal");
		gold = (int) found.get("gold");
		lapis = (int) found.get("lapis");
		iron = (int) found.get("iron");
		emerald = (int) found.get("emerald");

	}
	public void updatesPlayer(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		DBObject found = players.findOne(r);
		DBObject obj = new BasicDBObject("uuid", p.getUniqueId());
		obj.put("ip", p.getAddress().getAddress().getHostAddress());
		obj.put("kills", p.getStatistic(Statistic.PLAYER_KILLS));
		obj.put("deaths", p.getStatistic(Statistic.DEATHS));
		obj.put("diamond", p.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
		obj.put("redstone", p.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
		obj.put("coal", p.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
		obj.put("gold", p.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
		obj.put("lapis", p.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
		obj.put("iron", p.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
		obj.put("emerald", p.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
		players.update(found, obj);
	}

	public static MongoDB getInstance() {
		return instance;
	}

	public void AddStaffPassword(Player p, String args) {
		DBObject obj = new BasicDBObject("uuid", p.getUniqueId());
		obj.put("pin", args);
		staffplayers.insert(obj);
	}

	public void StaffPinRefresh(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		DBObject found = staffplayers.findOne(r);
		pwd = (String) found.get("pin");
	}

	public boolean ifStaff(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		DBObject found = staffplayers.findOne(r);
		if (found == null) {
			return false;
		}
		return true;
	}

	public void ChangeStaffPassword(Player p, String pwd) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		DBObject found = staffplayers.findOne(r);
		DBObject obj = new BasicDBObject("uuid", p.getUniqueId());
		obj.put("pin", pwd);
		staffplayers.update(found, obj);

	}

	public void RemoveStaffPin(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		staffplayers.remove(r);
	}

	public void AddPlayerToSecurity(Player p) {
		DBObject obj = new BasicDBObject("uuid", p.getUniqueId());
		obj.put("name", p.getName());
		security.insert(obj);
	}

	public void RemovePlayerFromSecurity(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		security.remove(r);
	}

	public boolean ifSecurity(Player p) {
		DBObject r = new BasicDBObject("uuid", p.getUniqueId());
		DBObject found = security.findOne(r);
		if (found == null) {
			return false;
		}
		return true;
	}
}
