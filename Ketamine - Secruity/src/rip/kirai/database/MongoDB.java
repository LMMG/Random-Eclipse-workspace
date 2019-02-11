package rip.kirai.database;

import java.net.UnknownHostException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import net.md_5.bungee.api.ChatColor;

public class MongoDB
{

	private static MongoDB instance;
	private DBCollection staffpins;
	private DB katamine;
	public String ip;
	public String pwd;
	private MongoClient client;

	public MongoDB()
	{
		instance = this;
	}

	public boolean connect(String ip, int port)
	{
		try
		{
			client = new MongoClient(ip, port);
		}
		catch (UnknownHostException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Secruity] Wont load!");
			e.printStackTrace();
			return false;
		}
		katamine = client.getDB("mc");
		staffpins = katamine.getCollection("pins");
		return true;
	}

	public void AddStaffPassword(Player player, String args)
	{
		DBObject obj = new BasicDBObject("uuid", player.getUniqueId());
		obj.put("pin", args);
		staffpins.insert(obj);
	}

	public void StaffPinRefresh(Player player)
	{
		DBObject r = new BasicDBObject("uuid", player.getUniqueId());
		DBObject found = staffpins.findOne(r);
		pwd = (String) found.get("pin");
	}

	public boolean isStaff(Player player)
	{
		DBObject r = new BasicDBObject("uuid", player.getUniqueId());
		DBObject found = staffpins.findOne(r);
		if (found == null) { return false; }
		return true;
	}

	public void ChangeStaffPassword(Player player, String pwd)
	{
		DBObject r = new BasicDBObject("uuid", player.getUniqueId());
		DBObject found = staffpins.findOne(r);
		DBObject obj = new BasicDBObject("uuid", player.getUniqueId());
		obj.put("pin", pwd);
		staffpins.update(found, obj);
	}

	public void RemoveStaffPin(Player player)
	{
		DBObject r = new BasicDBObject("uuid", player.getUniqueId());
		staffpins.remove(r);
	}

	public static MongoDB getInstance()
	{
		return instance;
	}
}
