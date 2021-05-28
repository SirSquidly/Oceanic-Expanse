package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockTopKelp;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
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
	public int attemptsPerChunk;
	public int chancePerAttempt;
	public int patchAmount;
	/** Spread in positive and negative directions from origin to try and place at.*/
	private int placeSpreadXZ = 6;
	/** See placeSpreadXZ. The same, but on the Y axis.*/
	private int placeSpreadY = 4;
	public Biome[] biomes;

	public WorldGenKelp(int perChunk, int perAttempt, int amount, Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.patchAmount = amount;
		this.biomes = biomes;
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
					{ spawnKelp(world, rand, pos); }
				}
			}
		}
	}
	
    public boolean spawnKelp(World worldIn, Random rand, BlockPos pos)
    {
        for (int i = 0; i < patchAmount; ++i)
        {	
        	int rX = rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
        	int rZ = rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
        	
            BlockPos blockpos = pos.add(rX, rand.nextInt(placeSpreadY) - rand.nextInt(placeSpreadY), rZ);
            Block blockHere = worldIn.getBlockState(blockpos).getBlock();
            Block blockDown = worldIn.getBlockState(blockpos.down()).getBlock();
            
            if (blockHere == Blocks.WATER && OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, blockpos) && blockDown != OEBlocks.KELP_TOP && blockDown != OEBlocks.KELP)
            {
            	worldIn.setBlockState(blockpos, OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(rand.nextInt(10))));
            	growKelpStalk(worldIn, rand, blockpos);
            }
        }
        return true;
    }
    
    /** Grows a piece of kelp to it's max height.*/
    public boolean growKelpStalk(World worldIn, Random rand, BlockPos pos)
    {
    	IBlockState state = worldIn.getBlockState(pos);
        
        if (worldIn.getBlockState(pos).getBlock() == OEBlocks.KELP_TOP)
        {
        	for (int i = ((Integer)state.getValue(BlockTopKelp.AGE)).intValue(); i != BlockTopKelp.maxHeight; ++i)
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