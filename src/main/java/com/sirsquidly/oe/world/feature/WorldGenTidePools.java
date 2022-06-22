package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenTidePools  implements IWorldGenerator
{
	public int attemptsPerChunk;
	public int chancePerAttempt;
	public Biome[] biomes;
	public int poolDepth = 5;
	public int poolDepthRand = 2;
	
	public WorldGenTidePools(int perChunk, int perAttempt, Biome... biomes)
    {
    	this.attemptsPerChunk = perChunk;
		this.chancePerAttempt = perAttempt;
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
				int yPos = Math.min(world.getSeaLevel() + 1, world.getHeight());;
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					for ( IBlockState state = world.getBlockState(pos); ((state.getBlock().isReplaceable(world, pos) || state.getBlock() instanceof BlockLeaves) && pos.getY() > world.getSeaLevel() - 1); state = world.getBlockState(pos) )
		        	{ pos = pos.down(); }
					
					
					/** What an absolute mess. Basically, grabbing 4 positions around the center point, and checking for too much height-variation.*/
					BlockPos posChk1 = new BlockPos(pos.getX() + 5, world.getHeight(), pos.getZ() + 5);
					BlockPos posChk2 = new BlockPos(pos.getX() + 5, world.getHeight(), pos.getZ() - 5);
					BlockPos posChk3 = new BlockPos(pos.getX() - 5, world.getHeight(), pos.getZ() - 5);
					BlockPos posChk4 = new BlockPos(pos.getX() - 5, world.getHeight(), pos.getZ() + 5);
					
					for ( IBlockState state = world.getBlockState(posChk1); ((state.getBlock().isReplaceable(world, posChk1) || state.getBlock() instanceof BlockLeaves) && posChk1.getY() > world.getSeaLevel() - 1); state = world.getBlockState(posChk1) )
		        	{ posChk1 = posChk1.down(); }
					for ( IBlockState state = world.getBlockState(posChk2); ((state.getBlock().isReplaceable(world, posChk2) || state.getBlock() instanceof BlockLeaves) && posChk2.getY() > world.getSeaLevel() - 1); state = world.getBlockState(posChk2) )
		        	{ posChk2 = posChk2.down(); }
					for ( IBlockState state = world.getBlockState(posChk3); ((state.getBlock().isReplaceable(world, posChk3) || state.getBlock() instanceof BlockLeaves) && posChk3.getY() > world.getSeaLevel() - 1); state = world.getBlockState(posChk3) )
		        	{ posChk3 = posChk3.down(); }
					for ( IBlockState state = world.getBlockState(posChk4); ((state.getBlock().isReplaceable(world, posChk4) || state.getBlock() instanceof BlockLeaves) && posChk4.getY() > world.getSeaLevel() - 1); state = world.getBlockState(posChk4) )
		        	{ posChk4 = posChk4.down(); }
					
					int posChk1Diff = pos.getY() - posChk1.getY();
					int posChk2Diff = pos.getY() - posChk2.getY();
					int posChk3Diff = pos.getY() - posChk3.getY();
					int posChk4Diff = pos.getY() - posChk4.getY();
					
					if (world.getBlockState(pos).getMaterial() != Material.WATER && posChk1Diff >= -1 && posChk1Diff <= 1 && posChk2Diff >= -1 && posChk2Diff <= 1 && posChk3Diff >= -1 && posChk3Diff <= 1 && posChk4Diff >= -1 && posChk4Diff <= 1)
					{ spawnTidePool(world, rand, pos.up(3)); }
				}
			}
		}
	}
	
	public boolean spawnTidePool(World worldIn, Random rand, BlockPos position)
    {
        for (int pools = 1 + Math.min(rand.nextInt(4),1); pools >= 0; pools--)
        {
        	position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
        	
        	/** The height between shrinking.*/
        	int j = 30 / 5 + rand.nextInt(4);
            
            float topSize = 0.05F + rand.nextInt(80)/1000;
            
            /** Climbs the generator up, and sued to identify positioning*/
             for (int k = poolDepth + rand.nextInt(poolDepthRand+1); k > 0; --k)
             {
            	 /** Calculates the area covered per layer */
                 float poolSize = (topSize + ((float)9 - (float)k) / (float)30) * (float)j;
                 float poolRock = (topSize + (float)10 / (float)30) * (float)j;
                 
                 int psc = MathHelper.ceil(poolSize);
                 int prc = MathHelper.ceil(poolRock);

                 /** Generates the Stone */
                 for (int i1 = -prc; i1 <= prc; ++i1)
                 {
                     float f1 = (float)MathHelper.abs(i1) - 0.25F;

                     for (int j1 = -prc; j1 <= prc; ++j1)
                     {
                         float f2 = (float)MathHelper.abs(j1) - 0.25F;

                         if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= poolRock * poolRock) && (i1 != -prc && i1 != prc && j1 != -prc && j1 != prc || rand.nextFloat() <= 0.5F))
                         {
                             if (k == 3)
                             { placeStoneAt(rand, worldIn, position.add(i1, -k, j1)); }
                         }
                     }
                 }
                 
                 /** Generates the Water and space above */
                 for (int ij = -psc; ij <= psc; ++ij)
                 {
                     float f1 = (float)MathHelper.abs(ij) - 0.25F;

                     for (int j1 = -psc; j1 <= psc; ++j1)
                     {
                         float f2 = (float)MathHelper.abs(j1) - 0.25F;

                         if ((ij == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= poolSize * poolSize) && (ij != -psc && ij != psc && j1 != -psc && j1 != psc || rand.nextFloat() <= 0.5F))
                         {
                        	 if (k < 3)
                             { worldIn.setBlockToAir(position.add(ij, -k, j1)); }
                        	 else if (k >= 3)
                             { worldIn.setBlockState(position.add(ij, -k, j1), Blocks.FLOWING_WATER.getDefaultState(), 16 | 2); }
                         }
                     }
                 }
             }
        }
        return true;
    }
	
	private void placeStoneAt(Random rand, World world, BlockPos pos)
    {
		if (world.getBlockState(pos.down()).getMaterial() == Material.AIR)
    	{ world.setBlockState(pos.down(), Blocks.SAND.getDefaultState(), 16 | 2); }
		if (world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.DOWN))
		{
			if (world.getBlockState(pos.up()).getBlock() == Blocks.AIR && rand.nextInt(10) == 0)
			{ world.setBlockState(pos.up(), Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE), 16 | 2); }
			
			if (rand.nextInt(5) == 0)
	    	{
	    		if (rand.nextInt(2) == 0) { world.setBlockState(pos, OEBlocks.BLUE_CORAL_BLOCK_DEAD.getDefaultState(), 16 | 2); }
	    		if (rand.nextInt(2) == 0) { world.setBlockState(pos, OEBlocks.PINK_CORAL_BLOCK_DEAD.getDefaultState(), 16 | 2); }
	    		if (rand.nextInt(2) == 0) { world.setBlockState(pos, OEBlocks.PURPLE_CORAL_BLOCK_DEAD.getDefaultState(), 16 | 2); }
	    		if (rand.nextInt(2) == 0) { world.setBlockState(pos, OEBlocks.RED_CORAL_BLOCK_DEAD.getDefaultState(), 16 | 2); }
	    		else { world.setBlockState(pos, OEBlocks.YELLOW_CORAL_BLOCK_DEAD.getDefaultState(), 16 | 2); }
	    	}
	    	else if (rand.nextInt(5) == 0)
	    	{ world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 16 | 2); }
	    	else if (rand.nextInt(5) == 0)
	    	{ world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 16 | 2); }
	    	else if (rand.nextInt(3) == 0)
	    	{ world.setBlockState(pos, Blocks.STONE.getDefaultState(), 16 | 2); }
	    	else if (world.getBlockState(pos).getMaterial() != Material.WATER)
	    	{ world.setBlockState(pos, Blocks.SAND.getDefaultState(), 16 | 2); }
		}
    }
	
	
}