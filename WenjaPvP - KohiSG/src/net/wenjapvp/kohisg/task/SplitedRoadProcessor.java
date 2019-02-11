package net.wenjapvp.kohisg.task;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SplitedRoadProcessor extends BukkitRunnable
{
	private JavaPlugin plugin;

	private Location center;
	private Processor processor;

	private int delay;

	public SplitedRoadProcessor(JavaPlugin plugin, Location center, int maxPerTick, int delay)
	{
		this.center = center;
		this.processor = new Processor(center, maxPerTick);
		this.delay = delay;
		this.plugin = plugin;
	}

	private SplitedRoadProcessor(JavaPlugin plugin, Location center, int delay, Processor procesor)
	{
		this.center = center;
		this.processor = procesor;
		this.delay = delay;
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		if (processor.run()) { return; }
		new SplitedRoadProcessor(this.plugin, this.center, this.delay, this.processor).runTaskLater(this.plugin, this.delay);
	}

	public static enum Type
	{
		X, Z;

		private Type()
		{}
	}

	public static class Processor
	{
		private World world;
		private Location center;
		private int phase = 0;
		private int processedBlockThisTick;
		private int processingZ;
		private int processingX;
		private int maxPerTick;
		private int length = 600;
		private int y = 64;

		public Processor(Location center, int maxPerTick)
		{
			this.world = center.getWorld();
			this.center = center;
			this.maxPerTick = maxPerTick;
		}

		public boolean run()
		{
			Chunk centerChunk = center.getChunk();
			processedBlockThisTick = 0;
			if (phase == 0)
			{
				processingZ = (-1 * length);
				processingX = 0;
				phase = 1;
			}
			if (phase == 1)
			{
				Chunk chunk = centerChunk;
				for (int x = 0; x < 16; x++)
				{
					Block block1 = world.getBlockAt(x, chunk.getX() + 1, chunk.getZ() + -1);
					Block block2 = world.getBlockAt(x, chunk.getX() + 1, chunk.getZ() + 16);
					if (!block1.getChunk().isLoaded())
					{
						block1.getChunk().load(true);
					}
					if (!block2.getChunk().isLoaded())
					{
						block2.getChunk().load(true);
					}
				}

				for (int z = 0; z < 16; z++)
				{
					Block block1 = world.getBlockAt(chunk.getX() + -1, 1, chunk.getZ() + z);
					Block block2 = world.getBlockAt(chunk.getX() + 16, 1, chunk.getZ() + z);
					if (!block1.getChunk().isLoaded())
					{
						block1.getChunk().load(true);
					}
					if (!block2.getChunk().isLoaded())
					{
						block2.getChunk().load(true);
					}
				}
				for (int z = 0; z < 16; z++)
				{
					for (int x = 0; x < 16; x++)
					{
						clearAbove(chunk.getWorld(), chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
						processedBlockThisTick += 1;
						setRoad(chunk.getBlock(chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z));
					}
				}
				phase = 2;
			}
			if (phase == 2)
			{
				for (; processingZ < length; processingZ += 1)
				{
					Chunk chunk = world.getChunkAt(new Location(world, center.getX(), 100.0D, processingZ + center.getZ()));
					if (chunk.getZ() != centerChunk.getZ())
					{
						fillRow(y, SplitedRoadProcessor.Type.Z, processingZ);
						if (processedBlockThisTick > maxPerTick) { return false; }
					}
				}
				phase = 3;
				processingZ = 0;
				processingX = (-1 * length);
			}
			if (phase == 3)
			{
				for (; processingX < length; processingX += 1)
				{
					Chunk chunk = world.getChunkAt(new Location(world, center.getX() + processingX, 100.0D, center.getZ()));
					if (chunk.getX() != centerChunk.getX())
					{
						fillRow(y, SplitedRoadProcessor.Type.X, processingX);
						if (processedBlockThisTick > maxPerTick) { return false; }
					}
				}
				this.phase = 4;
			}
			for (Entity entity : centerChunk.getWorld().getEntities())
			{
				if (entity instanceof Item)
				{
					entity.remove();
				}
			}
			return true;
		}

		public void fillRow(int y, SplitedRoadProcessor.Type modifierType, int modifier)
		{
			if (modifierType == SplitedRoadProcessor.Type.X)
			{
				for (int z = 0; z < 16; z++)
				{
					processedBlockThisTick += 1;
					clearAbove(world, modifier + center.getBlockX(), y, center.getBlockZ() + z);
					setRoad(world.getBlockAt(modifier + center.getBlockX(), y, center.getBlockZ() + z));
				}
				for (int i = 1; i < 6; i++)
				{
					Block one = this.world.getBlockAt(modifier + this.center.getBlockX(), y + i - 1, this.center.getBlockZ() + 15 + i);
					Block two = this.world.getBlockAt(modifier + this.center.getBlockX(), y + i - 1, this.center.getBlockZ() - i);
					if ((!one.getRelative(BlockFace.UP).isEmpty()) && (one.getRelative(BlockFace.UP).getType().isOccluding()))
					{
						one.setType(getBiomeMaterial(one.getBiome()));
					}
					if ((!two.getRelative(BlockFace.UP).isEmpty()) && (two.getRelative(BlockFace.UP).getType().isOccluding()))
					{
						two.setType(getBiomeMaterial(two.getBiome()));
					}
					clearAbove(this.world, modifier + this.center.getBlockX(), y + i - 1, this.center.getBlockZ() + 15 + i);
					clearAbove(this.world, modifier + this.center.getBlockX(), y + i - 1, this.center.getBlockZ() - i);
				}
			}
			else
			{
				for (int x = 0; x < 16; x++)
				{
					this.processedBlockThisTick += 1;
					clearAbove(this.world, this.center.getBlockX() + x, y, modifier + this.center.getBlockZ());
					setRoad(this.world.getBlockAt(this.center.getBlockX() + x, y, modifier + this.center.getBlockZ()));
				}
				for (int i = 1; i < 6; i++)
				{
					Block one = this.world.getBlockAt(this.center.getBlockX() + 15 + i, y + i - 1, modifier + this.center.getBlockZ());
					Block two = this.world.getBlockAt(this.center.getBlockX() - i, y + i - 1, modifier + this.center.getBlockZ());
					if ((!one.getRelative(BlockFace.UP).isEmpty()) && (one.getRelative(BlockFace.UP).getType().isOccluding()))
					{
						one.setType(getBiomeMaterial(one.getBiome()));
					}
					if ((!two.getRelative(BlockFace.UP).isEmpty()) && (two.getRelative(BlockFace.UP).getType().isOccluding()))
					{
						two.setType(getBiomeMaterial(two.getBiome()));
					}
					clearAbove(this.world, this.center.getBlockX() + 15 + i, y + i - 1, modifier + this.center.getBlockZ());
					clearAbove(this.world, this.center.getBlockX() - i, y + i - 1, modifier + this.center.getBlockZ());
				}
			}
		}

		public void setRoad(Block block)
		{
			block.setType(getRoadMaterial());
			block.getRelative(BlockFace.DOWN).setType(Material.BEDROCK);
		}

		public void clearAbove(World world, int bx, int by, int bz)
		{
			for (int y = by + 1; y < 256; y++)
			{
				this.processedBlockThisTick += 1;
				world.getBlockAt(bx, y, bz).setType(Material.AIR);
			}
		}

		public Material getBiomeMaterial(final Biome biome)
		{
			switch (biome)
			{
				case DESERT:
				{
					return Material.SAND;
				}
				default:
				{
					return Material.GRASS;
				}
			}
		}

		public Material getRoadMaterial()
		{
			int rand = RandomUtils.nextInt(3);
			if (rand == 0) { return Material.COBBLESTONE; }
			if (rand == 1) { return Material.GRAVEL; }
			return Material.SMOOTH_BRICK;
		}
	}
}