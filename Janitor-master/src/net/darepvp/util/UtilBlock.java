/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.darepvp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import net.darepvp.util.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UtilBlock
{
	public static HashSet<Byte> blockPassSet = new HashSet();
	public static HashSet<Byte> blockAirFoliageSet = new HashSet();
	public static HashSet<Byte> fullSolid = new HashSet();
	public static HashSet<Byte> blockUseSet = new HashSet();

	public static Block getLowestBlockAt(Location Location2)
	{
		Block Block2 = Location2.getWorld().getBlockAt((int) Location2.getX(), 0, (int) Location2.getZ());
		if (Block2 == null || Block2.getType().equals((Object) Material.AIR))
		{
			Block2 = Location2.getBlock();
			int y = (int) Location2.getY();
			while (y > 0)
			{
				Block Current = Location2.getWorld().getBlockAt((int) Location2.getX(), y, (int) Location2.getZ());
				Block Below = Current.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
				if (Below == null || Below.getType().equals((Object) Material.AIR))
				{
					Block2 = Current;
				}
				--y;
			}
		}
		return Block2;
	}

	public static boolean containsBlock(Location Location2, Material Material2)
	{
		int y = 0;
		while (y < 256)
		{
			Block Current = Location2.getWorld().getBlockAt((int) Location2.getX(), y, (int) Location2.getZ());
			if (Current != null && Current.getType().equals((Object) Material2)) { return true; }
			++y;
		}
		return false;
	}

	public static boolean containsBlock(Location Location2)
	{
		int y = 0;
		while (y < 256)
		{
			Block Current = Location2.getWorld().getBlockAt((int) Location2.getX(), y, (int) Location2.getZ());
			if (Current != null && !Current.getType().equals((Object) Material.AIR)) { return true; }
			++y;
		}
		return false;
	}

	public static boolean containsBlockBelow(Location Location2)
	{
		int y = 0;
		while (y < (int) Location2.getY())
		{
			Block Current = Location2.getWorld().getBlockAt((int) Location2.getX(), y, (int) Location2.getZ());
			if (Current != null && !Current.getType().equals((Object) Material.AIR)) { return true; }
			++y;
		}
		return false;
	}

	public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		int x = loc.getBlockX() - radius;
		while (x <= loc.getBlockX() + radius)
		{
			int y = loc.getBlockY() - radius;
			while (y <= loc.getBlockY() + radius)
			{
				int z = loc.getBlockZ() - radius;
				while (z <= loc.getBlockZ() + radius)
				{
					Location l = new Location(loc.getWorld(), (double) x, (double) y, (double) z);
					if (l.distance(loc) <= (double) radius)
					{
						blocks.add(l.getBlock());
					}
					++z;
				}
				++y;
			}
			++x;
		}
		return blocks;
	}

	public static Location StringToLocation(String Key)
	{
		String[] Args = Key.split(",");
		World World2 = Bukkit.getWorld((String) Args[0]);
		double X = Double.parseDouble(Args[1]);
		double Y = Double.parseDouble(Args[2]);
		double Z = Double.parseDouble(Args[3]);
		float Pitch = Float.parseFloat(Args[4]);
		float Yaw = Float.parseFloat(Args[5]);
		return new Location(World2, X, Y, Z, Pitch, Yaw);
	}

	public static String LocationToString(Location Location2)
	{
		return String.valueOf(Location2.getWorld().getName()) + "," + Location2.getX() + "," + Location2.getY() + "," + Location2.getZ() + "," + Location2.getPitch() + "," + Location2.getYaw();
	}

	public static boolean solid(Block block)
	{
		if (block == null) { return false; }
		return UtilBlock.solid(block.getTypeId());
	}

	public static boolean solid(int block)
	{
		return UtilBlock.solid((byte) block);
	}

	public static boolean solid(byte block)
	{
		if (blockPassSet.isEmpty())
		{
			blockPassSet.add(Byte.valueOf(String.valueOf(0)));
			blockPassSet.add(Byte.valueOf(String.valueOf(6)));
			blockPassSet.add(Byte.valueOf(String.valueOf(8)));
			blockPassSet.add(Byte.valueOf(String.valueOf(9)));
			blockPassSet.add(Byte.valueOf(String.valueOf(10)));
			blockPassSet.add(Byte.valueOf(String.valueOf(11)));
			blockPassSet.add(Byte.valueOf(String.valueOf(26)));
			blockPassSet.add(Byte.valueOf(String.valueOf(27)));
			blockPassSet.add(Byte.valueOf(String.valueOf(28)));
			blockPassSet.add(Byte.valueOf(String.valueOf(30)));
			blockPassSet.add(Byte.valueOf((byte) 31));
			blockPassSet.add(Byte.valueOf((byte) 32));
			blockPassSet.add(Byte.valueOf((byte) 37));
			blockPassSet.add(Byte.valueOf((byte) 38));
			blockPassSet.add(Byte.valueOf((byte) 39));
			blockPassSet.add(Byte.valueOf((byte) 40));
			blockPassSet.add(Byte.valueOf((byte) 50));
			blockPassSet.add(Byte.valueOf((byte) 51));
			blockPassSet.add(Byte.valueOf((byte) 55));
			blockPassSet.add(Byte.valueOf((byte) 59));
			blockPassSet.add(Byte.valueOf((byte) 63));
			blockPassSet.add(Byte.valueOf((byte) 64));
			blockPassSet.add(Byte.valueOf((byte) 65));
			blockPassSet.add(Byte.valueOf((byte) 66));
			blockPassSet.add(Byte.valueOf((byte) 68));
			blockPassSet.add(Byte.valueOf((byte) 69));
			blockPassSet.add(Byte.valueOf((byte) 70));
			blockPassSet.add(Byte.valueOf(String.valueOf(71)));
			blockPassSet.add(Byte.valueOf((byte) 72));
			blockPassSet.add(Byte.valueOf(String.valueOf(75)));
			blockPassSet.add(Byte.valueOf(String.valueOf(76)));
			blockPassSet.add(Byte.valueOf(String.valueOf(77)));
			blockPassSet.add(Byte.valueOf(String.valueOf(78)));
			blockPassSet.add(Byte.valueOf(String.valueOf(83)));
			blockPassSet.add(Byte.valueOf((byte) 90));
			blockPassSet.add(Byte.valueOf((byte) 92));
			blockPassSet.add(Byte.valueOf((byte) 93));
			blockPassSet.add(Byte.valueOf((byte) 94));
			blockPassSet.add(Byte.valueOf((byte) 96));
			blockPassSet.add(Byte.valueOf((byte) 101));
			blockPassSet.add(Byte.valueOf((byte) 102));
			blockPassSet.add(Byte.valueOf((byte) 104));
			blockPassSet.add(Byte.valueOf((byte) 105));
			blockPassSet.add(Byte.valueOf((byte) 106));
			blockPassSet.add(Byte.valueOf((byte) 107));
			blockPassSet.add(Byte.valueOf((byte) 111));
			blockPassSet.add(Byte.valueOf((byte) 115));
			blockPassSet.add(Byte.valueOf((byte) 116));
			blockPassSet.add(Byte.valueOf((byte) 117));
			blockPassSet.add(Byte.valueOf((byte) 118));
			blockPassSet.add(Byte.valueOf(String.valueOf(119)));
			blockPassSet.add(Byte.valueOf(String.valueOf(120)));
			blockPassSet.add(Byte.valueOf(String.valueOf(-85)));
		}
		return !blockPassSet.contains(Byte.valueOf(block));
	}

	public static boolean airFoliage(Block block)
	{
		if (block == null) { return false; }
		return UtilBlock.airFoliage(block.getTypeId());
	}

	public static boolean airFoliage(int block)
	{
		return UtilBlock.airFoliage((byte) block);
	}

	public static boolean airFoliage(byte block)
	{
		if (blockAirFoliageSet.isEmpty())
		{
		}
		return blockAirFoliageSet.contains(Byte.valueOf(block));
	}

	public static boolean fullSolid(Block block)
	{
		if (block == null) { return false; }
		return UtilBlock.fullSolid(block.getTypeId());
	}

	public static boolean fullSolid(int block)
	{
		return UtilBlock.fullSolid((byte) block);
	}

	public static boolean fullSolid(byte block)
	{
		if (fullSolid.isEmpty())
		{
			fullSolid.add(Byte.valueOf(String.valueOf(1)));
			fullSolid.add(Byte.valueOf(String.valueOf(2)));
			fullSolid.add(Byte.valueOf(String.valueOf(3)));
			fullSolid.add(Byte.valueOf(String.valueOf(4)));
		}
		return fullSolid.contains(Byte.valueOf(block));
	}

	public static boolean usable(Block block)
	{
		if (block == null) { return false; }
		return UtilBlock.usable(block.getTypeId());
	}

	public static boolean usable(int block)
	{
		return UtilBlock.usable((byte) block);
	}

	public static boolean usable(byte block)
	{
		if (blockUseSet.isEmpty())
		{
		}
		return blockUseSet.contains(Byte.valueOf(block));
	}

	public static HashMap<Block, Double> getInRadius(Location loc, double dR)
	{
		return UtilBlock.getInRadius(loc, dR, 999.0);
	}

	public static HashMap<Block, Double> getInRadius(Location loc, double dR, double heightLimit)
	{
		HashMap<Block, Double> blockList = new HashMap<Block, Double>();
		int iR = (int) dR + 1;
		int x = -iR;
		while (x <= iR)
		{
			int z = -iR;
			while (z <= iR)
			{
				int y = -iR;
				while (y <= iR)
				{
					Block curBlock;
					double offset;
					if ((double) Math.abs(y) <= heightLimit && (offset = UtilMath.offset(loc, (curBlock = loc.getWorld().getBlockAt((int) (loc.getX() + (double) x), (int) (loc.getY() + (double) y), (int) (loc.getZ() + (double) z))).getLocation().add(0.5, 0.5, 0.5))) <= dR)
					{
						blockList.put(curBlock, 1.0 - offset / dR);
					}
					++y;
				}
				++z;
			}
			++x;
		}
		return blockList;
	}

	public static HashMap<Block, Double> getInRadius(Block block, double dR)
	{
		HashMap<Block, Double> blockList = new HashMap<Block, Double>();
		int iR = (int) dR + 1;
		int x = -iR;
		while (x <= iR)
		{
			int z = -iR;
			while (z <= iR)
			{
				int y = -iR;
				while (y <= iR)
				{
					Block curBlock = block.getRelative(x, y, z);
					double offset = UtilMath.offset(block.getLocation(), curBlock.getLocation());
					if (offset <= dR)
					{
						blockList.put(curBlock, 1.0 - offset / dR);
					}
					++y;
				}
				++z;
			}
			++x;
		}
		return blockList;
	}

	public static boolean isBlock(ItemStack item)
	{
		if (item == null) { return false; }
		if (item.getTypeId() > 0 && item.getTypeId() < 256) { return true; }
		return false;
	}

	public static Block getHighest(Location locaton)
	{
		return UtilBlock.getHighest(locaton, null);
	}

	public static Block getHighest(Location location, HashSet<Material> ignore)
	{
		Location loc = location;
		loc.setY(0.0);
		int i = 0;
		while (i < 256)
		{
			loc.setY((double) (256 - i));
			if (UtilBlock.solid(loc.getBlock()))
				break;
			++i;
		}
		return loc.getBlock().getRelative(BlockFace.UP);
	}

	public static boolean isInAir(Player player)
	{
		boolean nearBlocks = false;
		for (Block block : UtilBlock.getSurrounding(player.getLocation().getBlock(), true))
		{
			if (block.getType() == Material.AIR)
				continue;
			nearBlocks = true;
			break;
		}
		return nearBlocks;
	}

	public static ArrayList<Block> getSurrounding(Block block, boolean diagonals)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		if (diagonals)
		{
			int x = -1;
			while (x <= 1)
			{
				int y = -1;
				while (y <= 1)
				{
					int z = -1;
					while (z <= 1)
					{
						if (x != 0 || y != 0 || z != 0)
						{
							blocks.add(block.getRelative(x, y, z));
						}
						++z;
					}
					++y;
				}
				++x;
			}
		}
		else
		{
			blocks.add(block.getRelative(BlockFace.UP));
			blocks.add(block.getRelative(BlockFace.DOWN));
			blocks.add(block.getRelative(BlockFace.NORTH));
			blocks.add(block.getRelative(BlockFace.SOUTH));
			blocks.add(block.getRelative(BlockFace.EAST));
			blocks.add(block.getRelative(BlockFace.WEST));
		}
		return blocks;
	}

	public static ArrayList<Block> getSurroundingXZ(Block block)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		blocks.add(block.getRelative(BlockFace.NORTH));
		blocks.add(block.getRelative(BlockFace.NORTH_EAST));
		blocks.add(block.getRelative(BlockFace.NORTH_WEST));
		blocks.add(block.getRelative(BlockFace.SOUTH));
		blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
		blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
		blocks.add(block.getRelative(BlockFace.EAST));
		blocks.add(block.getRelative(BlockFace.WEST));
		return blocks;
	}

	public static String serializeLocation(Location location)
	{
		int X = (int) location.getX();
		int Y = (int) location.getY();
		int Z = (int) location.getZ();
		int P = (int) location.getPitch();
		int Yaw = (int) location.getYaw();
		return new String(String.valueOf(location.getWorld().getName()) + "," + X + "," + Y + "," + Z + "," + P + "," + Yaw);
	}

	public static Location deserializeLocation(String string)
	{
		if (string == null) { return null; }
		String[] parts = string.split(",");
		World world = Bukkit.getServer().getWorld(parts[0]);
		Double LX = Double.parseDouble(parts[1]);
		Double LY = Double.parseDouble(parts[2]);
		Double LZ = Double.parseDouble(parts[3]);
		Float P = Float.valueOf(Float.parseFloat(parts[4]));
		Float Y = Float.valueOf(Float.parseFloat(parts[5]));
		Location result = new Location(world, LX.doubleValue(), LY.doubleValue(), LZ.doubleValue());
		result.setPitch(P.floatValue());
		result.setYaw(Y.floatValue());
		return result;
	}

	public static boolean isVisible(Block block)
	{
		for (Block other : UtilBlock.getSurrounding(block, false))
		{
			if (other.getType().isOccluding())
				continue;
			return true;
		}
		return false;
	}
}
