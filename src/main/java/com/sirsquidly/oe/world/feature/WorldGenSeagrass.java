package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.blocks.BlockSeaPickle;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSeagrass implements IWorldGenerator
{
	private final Block block;
	private int patchAmount;
	/** Float chance (0.0 - 1.0) for generating double Seagrass. Higher is more likely.*/
	private double tallChance;
	private int attemptsPerChunk;
	private int chancePerAttempt;
	/** Flips the generator to run from the world bottom up, always failing if the placed Seagrass has sky access. For underground / beneath structure generation.*/
	private boolean bottomUp;
	/** Spread in positive and negative directions from origin to try and place at*/
	private int placeSpreadXZ = 8;
	/** See placeSpreadXZ. The same, but on the Y axis.*/
	private int placeSpreadY = 4;
	private Biome[] biomes;

	public WorldGenSeagrass(Block blockIn, int perChunk, int perAttempt, int amount, double tall, boolean rising, Biome... biomes)
	{
		this.block = blockIn;
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
				int yPos = Math.max(world.getSeaLevel() - 1, 1);
				if (bottomUp) { yPos = 1; }
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					if (bottomUp) 
					{ 
						for ( @SuppressWarnings("unused") IBlockState state = world.getBlockState(pos); !world.canBlockSeeSky(pos) && pos.getY() < world.getHeight(); state = world.getBlockState(pos) )
						{ 
							pos = pos.up();
							
							if (this.block == OEBlocks.SEA_PICKLE && world.getBlockState(pos).getBlock() != Blocks.SAND)
							{ return; }
							if(this.block.canPlaceBlockAt(world, pos.down()))
							{ spawnSeagrass(world, rand, pos); break;}
						}
					}
					else
					{ 
						for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        	{ pos = pos.down(); }
					
						if (this.block == OEBlocks.SEA_PICKLE && world.getBlockState(pos).getBlock() != Blocks.SAND)
						{ return; }
						
						if(this.block.canPlaceBlockAt(world, pos.up()))
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
        	int rX = rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
        	int rZ = rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
        	
            BlockPos blockpos = position.add(rX, rand.nextInt(placeSpreadY) - rand.nextInt(placeSpreadY), rZ);
            
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && this.block.canPlaceBlockAt(worldIn, blockpos))
            {
            	if (this.block == OEBlocks.SEAGRASS && rand.nextDouble() < tallChance && OEBlocks.TALL_SEAGRASS.canPlaceBlockAt(worldIn, blockpos))
            	{ ((BlockDoubleUnderwater) OEBlocks.TALL_SEAGRASS).placeAt(worldIn, blockpos, 16 | 2); }
            	else if (this.block == OEBlocks.SEA_PICKLE)
            	{
            		if (blockpos.getY() < worldIn.getSeaLevel() - 1) worldIn.setBlockState(blockpos, this.block.getDefaultState().withProperty(BlockSeaPickle.AMOUNT, rand.nextInt(4) + 1), 2);
            	}
            	else
            	{ worldIn.setBlockState(blockpos, this.block.getDefaultState(), 16 | 2); }
            }
        }
        return true;
    }
}