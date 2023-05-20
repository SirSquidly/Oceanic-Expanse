package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenBlueIce implements IWorldGenerator
{
	private int patchAmount;
	private int attemptsPerChunk;
	private int chancePerAttempt;
	private Biome[] biomes;
	/** Spread in positive and negative directions from origin to try and place at.*/
	private int placeSpread = 3;
	/** Chance /1 to retry a failed placement.*/
	private int placeRetryChance = 2;
	
	public WorldGenBlueIce(int perChunk, int perAttempt, int amount, Biome... biomes)
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
		ChunkPos chunkPos = world.getChunkFromChunkCoords(chunkX, chunkZ).getPos();
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
				int yRandOffset = rand.nextInt(20);
				int yPos = Math.max(world.getSeaLevel() - 1 - yRandOffset, 1);
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					for ( IBlockState state = world.getBlockState(pos); state.getBlock().isReplaceable(world, pos) && pos.getY() > 0; state = world.getBlockState(pos) )
		        	{ pos = pos.down(); }
					
					if(world.getBlockState(pos).getBlock() == Blocks.PACKED_ICE)
					{ spawnBlueIce(world, rand, pos); }
				}
			}
		}
	}
	
    public boolean spawnBlueIce(World worldIn, Random rand, BlockPos position)
    {
    	int getMaxY = Math.max(worldIn.getSeaLevel() + ConfigHandler.worldGen.shellPatch.shellPatchAboveSeaLevel, 1);
    	
        for (int i = 0; i < patchAmount; ++i)
        {	
        	int rX = rand.nextInt(placeSpread) - rand.nextInt(placeSpread);
        	int rZ = rand.nextInt(placeSpread) - rand.nextInt(placeSpread);
        	
            BlockPos blockpos = position.add(rX, rand.nextInt(placeSpread) - rand.nextInt(placeSpread), rZ);
            
        	if (blockpos.getY() > getMaxY) blockpos = new BlockPos(blockpos.getX(), getMaxY, blockpos.getZ());
        	
            if(worldIn.getBlockState(blockpos).getBlock() == Blocks.ICE || worldIn.getBlockState(blockpos).getBlock() == Blocks.PACKED_ICE || worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
			{ 
            	worldIn.setBlockState(blockpos, OEBlocks.BLUE_ICE.getDefaultState());
            }
            else { if (rand.nextInt(placeRetryChance) == 0) { --i; } }
        }
        return true;
    }
}