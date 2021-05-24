package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GeneratorCoconutTree implements IWorldGenerator
{
	public WorldGenCoconutTree coconutTreeGen = new WorldGenCoconutTree();
	private Biome[] biomes;
	private int attemptsPerChunk;
	private int chancePerAttempt;

	public GeneratorCoconutTree(int perChunk,int perAttempt ,Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.biomes = biomes; 
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider)
	{
		int x = chunkX * 16 + 8;
		int z = chunkZ * 16 + 8;
		Biome biome = world.getBiomeForCoordsBody(new BlockPos(x, 0, z));	
        boolean isValidBiome = false;

		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				isValidBiome = true;
				break;
			}
		}

		if(isValidBiome)
		{
			for(int i = 0; i < attemptsPerChunk; i++)
			{
				int xPos = x + random.nextInt(4) - random.nextInt(4);
				int zPos = z + random.nextInt(4) - random.nextInt(4);
				int yPos = Math.max(world.getSeaLevel() + 6, 1);
				
				if(random.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = new BlockPos(xPos, yPos, zPos);
					
					for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        { pos = pos.down(); }
					
					if(OEBlocks.COCONUT_SAPLING.canPlaceBlockAt(world, pos.up()))
					{
						if(coconutTreeGen.generate(world, random, pos.up()))
						{
							break;
						}
					}
				}
			}
		}
	}
}