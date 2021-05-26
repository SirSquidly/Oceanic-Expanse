package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldGenKelpForest extends WorldGenKelp
{
	private int branchAmount;
	private int branchRetryChance = 2;
	private int branchSpread = 40;

	public WorldGenKelpForest(int perChunk, int perAttempt, int branches, int amount, Biome... biomes)
	{
		super(perChunk, perAttempt, amount, biomes);
		this.branchAmount = branches * 10; 
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
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
		
		if (isValidBiome)
		{
			for(int i = 0; i < attemptsPerChunk; i++)
			{
				int xPos = x + rand.nextInt(4) - rand.nextInt(4);
				int zPos = z + rand.nextInt(4) - rand.nextInt(4);
				int yPos = Math.max(world.getSeaLevel() - 1, 1);;
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = new BlockPos(xPos, yPos, zPos);
					
					for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        { pos = pos.down(); }
					
					if(OEBlocks.KELP_TOP.canPlaceBlockAt(world, pos.up()))
					{
						spawnKelpForest(world, rand, pos);
					}
				}
			}
		}
	}
	
	public boolean spawnKelpForest(World worldIn, Random rand, BlockPos pos)
    {
        for (int i = 0; i < branchAmount; ++i)
        {	
        	int rX = pos.getX() + rand.nextInt(branchSpread) - rand.nextInt(branchSpread);
        	int rZ = pos.getZ() + rand.nextInt(branchSpread) - rand.nextInt(branchSpread);
        	
        	BlockPos blockpos = new BlockPos(rX, worldIn.getSeaLevel(), rZ);
            
            for ( IBlockState state = worldIn.getBlockState(blockpos); (state.getBlock().isReplaceable(worldIn, blockpos) && blockpos.getY() > 0); state = worldIn.getBlockState(blockpos) )
	        { blockpos = blockpos.down(); }
            
            if (OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, blockpos))
            { spawnKelp(worldIn, rand, blockpos); }
            else { if (rand.nextInt(branchRetryChance) == 0) { --i; } }
        }
        return true;
    }
}
