package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSeagrass implements IWorldGenerator
{
	private int patchAmount;
	private double tallChance;
	private Biome[] biomes;

	public WorldGenSeagrass(int amount, double tall, Biome... biomes)
	{
		this.patchAmount = amount;
		this.tallChance = tall;
		this.biomes = biomes;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		int seaLevel = Math.max(world.getSeaLevel() - 1, 1);
        BlockPos pos = new BlockPos(rand.nextInt(16) + chunkX * 16 + 8, seaLevel, rand.nextInt(16) + chunkZ * 16 + 8);
        
        Biome biome = world.getBiomeForCoordsBody(pos);
        boolean isValidBiome = false;

		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				isValidBiome = true;
				break;
			}
		}
		
		if (rand.nextInt(1) == 0 && (isValidBiome)) 
		{
            spawnSeagrass(world, rand, pos);
        }
	}
	
    public boolean spawnSeagrass(World worldIn, Random rand, BlockPos position)
    {
        for ( IBlockState iblockstate = worldIn.getBlockState(position); 
        		(iblockstate.getBlock() == Blocks.WATER || 
        				iblockstate.getBlock().isLeaves(iblockstate, worldIn, position)) && 
        				position.getY() > 0; iblockstate = worldIn.getBlockState(position)
        		)
        {
            position = position.down();
        }

        for (int i = 0; i < patchAmount; ++i)
        {	
        	int rX = rand.nextInt(8) - rand.nextInt(8);
        	int rZ = rand.nextInt(8) - rand.nextInt(8);
        	
            BlockPos blockpos = position.add(rX, rand.nextInt(4) - rand.nextInt(4), rZ);
            
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && OEBlocks.SEAGRASS.canPlaceBlockAt(worldIn, blockpos))
            {
            	if (rand.nextDouble() < tallChance && OEBlocks.TALL_SEAGRASS.canPlaceBlockAt(worldIn, blockpos))
            	{ ((BlockDoubleUnderwater) OEBlocks.TALL_SEAGRASS).placeAt(worldIn, blockpos, 2); }
            	else
            	{ worldIn.setBlockState(blockpos, OEBlocks.SEAGRASS.getDefaultState()); }
            }
        }
        return true;
    }
}