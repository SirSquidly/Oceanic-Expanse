package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCoconutTree implements IWorldGenerator
{
	public GeneratorCoconutTree coconutTreeGen = new GeneratorCoconutTree();
	private Biome[] biomes;
	private int attemptsPerChunk;
	private int chancePerAttempt;

	public WorldGenCoconutTree(int perChunk,int perAttempt ,Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.biomes = biomes; 
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider)
	{
        boolean isValidBiome = false;
        ChunkPos chunkPos = world.getChunk(chunkX, chunkZ).getPos();
        Biome biome = world.getBiomeForCoordsBody(chunkPos.getBlock(0, 0, 0));

		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				isValidBiome = true;
				break;
			}
		}

		if(isValidBiome && ConfigHandler.worldGen.palmTree.enablePalmTrees)
		{
			for(int i = 0; i < attemptsPerChunk; i++)
			{
				int xPos = rand.nextInt(16) + 8;
				int zPos = rand.nextInt(16) + 8;
				int yPos = Math.max(world.getSeaLevel() + 6, 1);
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					//BlockPos pos = new BlockPos(xPos, yPos, zPos);
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        { pos = pos.down(); }
					
					if(OEBlocks.PALM_SAPLING.canPlaceBlockAt(world, pos.up()))
					{
						if(coconutTreeGen.generate(world, rand, pos.up()))
						{
							break;
						}
					}
				}
			}
		}
	}
}