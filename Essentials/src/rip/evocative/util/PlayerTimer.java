package rip.evocative.util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerTimer
{

	public static HashMap<String, HashMap<UUID, Long>> cooldown;

	static
	{
		cooldown = new HashMap<String, HashMap<UUID, Long>>();
	}

	public static HashMap<UUID, Long> getCooldownMap(String cooldown1)
	{
		if (cooldown.containsKey(cooldown1)) { return cooldown.get(cooldown1); }
		return null;
	}

	public static void addCooldown(String cooldown1, Player p, int seconds)
	{
		if (!PlayerTimer.cooldown.containsKey(cooldown1)) { throw new IllegalArgumentException(String.valueOf(cooldown1) + " does not exist"); }
		long next = System.currentTimeMillis() + seconds * 1000L;
		cooldown.get(cooldown1).put(p.getUniqueId(), next);
	}

	public static boolean isOnCooldown(String cooldown1, Player p)
	{
		return cooldown.containsKey(cooldown1) && PlayerTimer.cooldown.get(cooldown1).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= PlayerTimer.cooldown.get(cooldown1).get(p.getUniqueId());
	}

	public static boolean isOnCooldown(String cooldown1, UUID uuid)
	{
		return cooldown.containsKey(cooldown1) && PlayerTimer.cooldown.get(cooldown1).containsKey(uuid) && System.currentTimeMillis() <= PlayerTimer.cooldown.get(cooldown1).get(uuid);
	}

	public static long getCooldownForPlayerLong(String cooldown1, Player p)
	{
		return cooldown.get(cooldown1).get(p.getUniqueId()) - System.currentTimeMillis();
	}

	public static void removeCooldown(String cooldown1, Player p)
	{
		if (!PlayerTimer.cooldown.containsKey(cooldown1)) { throw new IllegalArgumentException(String.valueOf(cooldown1) + " does not exist"); }
		cooldown.get(cooldown1).remove(p.getUniqueId());
	}

	public static void removeCooldown(String cooldown1, UUID uuid)
	{
		if (!PlayerTimer.cooldown.containsKey(cooldown1)) { throw new IllegalArgumentException(String.valueOf(cooldown1) + " does not exist"); }
		cooldown.get(cooldown1).remove(uuid);
	}
}
