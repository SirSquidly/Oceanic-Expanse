package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockDoubleSeaOats;
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

public class WorldGenSeaOats implements IWorldGenerator
{
	private int patchAmount;
	private int attemptsPerChunk;
	private int chancePerAttempt;
	private Biome[] biomes;
	/** Spread in positive and negative directions from origin to try and place at.*/
	private int placeSpread = 3;
	/** Chance /1 to retry a failed placement.*/
	private int placeRetryChance = 2;
	/** Chance /1 to place short sea oats when tall sea oats cannot be placed.*/

	public WorldGenSeaOats(int perChunk, int perAttempt, int amount, Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.patchAmount = amount;
		this.biomes = biomes;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
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

		if (isValidBiome)
		{
			for(int i = 0; i < attemptsPerChunk; i++)
			{
				int xPos = rand.nextInt(16) + 8;
				int zPos = rand.nextInt(16) + 8;
				int yPos = world.getHeight();
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > world.getSeaLevel() - 1); state = world.getBlockState(pos) )
		        	{ pos = pos.down(); }
				
					if(OEBlocks.SEA_OATS.canPlaceBlockAt(world, pos.up()))
					{ spawnSeaOats(world, rand, pos); }
				}
			}
		}
	}
	
    public boolean spawnSeaOats(World worldIn, Random rand, BlockPos position)
    {
        for (int i = 0; i < patchAmount; ++i)
        {	
        	int rX = rand.nextInt(placeSpread) - rand.nextInt(placeSpread);
        	int rZ = rand.nextInt(placeSpread) - rand.nextInt(placeSpread);
        	
            BlockPos blockpos = position.add(rX, rand.nextInt(placeSpread) - rand.nextInt(placeSpread), rZ);
            
            if (worldIn.isAirBlock(blockpos) && OEBlocks.SEA_OATS.canPlaceBlockAt(worldIn, blockpos))
            { 
            	if (((BlockDoubleSeaOats) OEBlocks.SEA_OATS).checkTouching(worldIn, blockpos, false) >= ConfigHandler.worldGen.seaOatsPatch.seaOatsPatchMinTallSides)
            	{ 
            		((BlockDoubleSeaOats) OEBlocks.SEA_OATS).placeAt(worldIn, blockpos, (16 | 2));
            	}
            	else if (ConfigHandler.worldGen.seaOatsPatch.seaOatsPatchShortChance == 1 || ConfigHandler.worldGen.seaOatsPatch.seaOatsPatchShortChance != 0 && rand.nextInt(ConfigHandler.worldGen.seaOatsPatch.seaOatsPatchShortChance) == 0)
            	{
            		worldIn.setBlockState(blockpos, OEBlocks.SEA_OATS.getDefaultState().withProperty(BlockDoubleSeaOats.HALF, BlockDoubleSeaOats.EnumBlockHalf.UPPER).withProperty(BlockDoubleSeaOats.SANDY, true), 2);
            	}
            }
            else { if (rand.nextInt(placeRetryChance) == 0) { --i; } }
        }
        return true;
    }
}