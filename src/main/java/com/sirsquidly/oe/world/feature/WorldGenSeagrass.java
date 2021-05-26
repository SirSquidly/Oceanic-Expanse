package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.ChunkProviderClient;
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
	private int attemptsPerChunk;
	private int chancePerAttempt;
	private boolean bottomUp;
	private Biome[] biomes;

	public WorldGenSeagrass(int perChunk, int perAttempt, int amount, double tall, boolean rising, Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.patchAmount = amount;
		this.tallChance = tall;
		this.bottomUp = rising;
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
				int yPos = Math.max(world.getSeaLevel() - 1, 1);
				if (bottomUp) { yPos = 1; }
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = new BlockPos(xPos, yPos, zPos);
					
					if (bottomUp) 
					{ 
						for ( IBlockState state = world.getBlockState(pos); !world.canBlockSeeSky(pos) && pos.getY() < world.getHeight(); state = world.getBlockState(pos) )
						{ 
							pos = pos.up();
							
							if(OEBlocks.SEAGRASS.canPlaceBlockAt(world, pos.down()))
							{ spawnSeagrass(world, rand, pos); break;}
						}
					}
					else
					{ 
						for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        	{ pos = pos.down(); }
					
						if(OEBlocks.SEAGRASS.canPlaceBlockAt(world, pos.up()))
						{ spawnSeagrass(world, rand, pos); }
					}
				}
			}
		}
	}
	
    public boolean spawnSeagrass(World worldIn, Random rand, BlockPos position)
    {
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