package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockTopKelp;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenKelp implements IWorldGenerator
{
	private int patchAmount;
	private Biome[] biomes;

	public WorldGenKelp(int amount, Biome... biomes)
	{
		this.patchAmount = amount;
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
			spawnKelp(world, rand, pos);
        }
	}
	
    public boolean spawnKelp(World worldIn, Random rand, BlockPos pos)
    {
        for ( IBlockState iblockstate = worldIn.getBlockState(pos); 
        		(iblockstate.getBlock() == Blocks.WATER || 
        				iblockstate.getBlock().isLeaves(iblockstate, worldIn, pos)) && 
        				pos.getY() > 0; iblockstate = worldIn.getBlockState(pos)
        		)
        { pos = pos.down(); }

        for (int i = 0; i < patchAmount; ++i)
        {	
        	int rX = rand.nextInt(8) - rand.nextInt(8);
        	int rZ = rand.nextInt(8) - rand.nextInt(8);
        	
            BlockPos blockpos = pos.add(rX, rand.nextInt(4) - rand.nextInt(4), rZ);
            
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, blockpos))
            {
            	worldIn.setBlockState(blockpos, OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(rand.nextInt(10))));
            	growKelpStalk(worldIn, rand, blockpos);
            	
            }
        }
        return true;
    }
    
    
    public boolean growKelpStalk(World worldIn, Random rand, BlockPos pos)
    {
    	IBlockState state = worldIn.getBlockState(pos);
        
        if (worldIn.getBlockState(pos).getBlock() == OEBlocks.KELP_TOP)
        {
        	for (int i = ((Integer)state.getValue(BlockTopKelp.AGE)).intValue(); i < 16; ++i)
            {	
            	if(OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, pos.up()))
            	{ 
            		worldIn.setBlockState(pos.up(), OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(i)), 2);
            		worldIn.setBlockState(pos, OEBlocks.KELP.getDefaultState(), 2);
            		
            		pos = pos.up();
            		state = worldIn.getBlockState(pos);
            	}
            	else { break; }
            }
        }
        return true;
    }
}