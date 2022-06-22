package com.sirsquidly.oe.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenShoreRock implements IWorldGenerator
{
	public int attemptsPerChunk;
	public int chancePerAttempt;
	/** Spread in positive and negative directions from origin to try and place at.*/
	public int groupAmount;
	/** Spread in positive and negative directions from origin to try and place at. Only used when groupAmount is over 1.*/
	public int placeSpreadXZ = 8;
	/** If the rock placement shouldn't avoid the biome topBlock (sand on beaches, ect.).*/
	private boolean acceptTop;
	public Biome[] biomes;
	/** Chance /1 to boost the height by 20. No reason for this, I just think it's fun.*/
	public int chanceForHuge = 100;
	
    public WorldGenShoreRock(int perChunk, int perAttempt, int amount, boolean acceptTop, Biome... biomes)
    {
    	this.attemptsPerChunk = perChunk;
		this.chancePerAttempt = perAttempt;
		this.groupAmount = amount;
		this.acceptTop = acceptTop;
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
				int xPos = 8;
				int zPos = 8;
				int yPos = Math.max(world.getSeaLevel() - 1, 1);;
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					int posSeaFloor = yPos;
					//BlockPos posSeaFloor = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					for (int a = 0; a <= groupAmount; ++a)
			        {	
						if (groupAmount > 1)
						{
							int rX = pos.getX() + rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
				        	int rZ = pos.getZ() + rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
				        	
				        	pos = new BlockPos(rX, yPos, rZ);
				        	biome = world.getBiomeForCoordsBody(pos);
						}
						
			        	if (world.getBlockState(pos).getBlock() != biome.topBlock || acceptTop)
						{ 
			        		for ( IBlockState state = world.getBlockState(pos.down(posSeaFloor)); state.getBlock() != biome.fillerBlock && posSeaFloor > 0 ; state = world.getBlockState(pos.down(posSeaFloor)) )
				        	{ posSeaFloor -= 1; }
			        		
			        		spawnShoreRock(world, rand, pos, posSeaFloor - 3); 
						}
			        }
				}
			}
		}
	}
    
    public boolean spawnShoreRock(World worldIn, Random rand, BlockPos position, int floorY)
    {
    	position = position.add(0, rand.nextInt(6) - rand.nextInt(3), 0);
    	if (rand.nextInt(chanceForHuge + 1) == 0) { position = position.up(20); }
    	
    	int amountDown = position.getY() - floorY;
    	
        int i = 30;
        /** The height between shrinking.*/
        int j = i / 5 + rand.nextInt(4);
        
        float topSize = 0.05F + rand.nextInt(80)/1000;
        
        /** Climbs the generator up, and sued to identify positioning*/
         for (int k = amountDown; k > 0; --k)
         {
        	 /** Calculates the area covered per layer */
             float f = (topSize + (float)k / (float)i) * (float)j;
             
             int l = MathHelper.ceil(f);

             for (int i1 = -l; i1 <= l; ++i1)
             {
                 float f1 = (float)MathHelper.abs(i1) - 0.25F;

                 for (int j1 = -l; j1 <= l; ++j1)
                 {
                     float f2 = (float)MathHelper.abs(j1) - 0.25F;

                     if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f) && (i1 != -l && i1 != l && j1 != -l && j1 != l || rand.nextFloat() <= 0.5F))
                     {
                         IBlockState iblockstate = worldIn.getBlockState(position.add(i1, -k, j1));
                         Block block = iblockstate.getBlock();

                         /** Checks spot is air, and places blocks.*/
                         if (iblockstate.getBlock().isAir(iblockstate, worldIn, position.add(i1, -k, j1)) || block != Blocks.STONE)
                         { 
                        	 if (rand.nextInt(Math.max(10+k/2,2)) == 0)
                        	 { 
                        		 if (position.add(i1, -k, j1).getY() > worldIn.getSeaLevel() + 1 && worldIn.getBlockState(position.add(i1, -k + 1, j1)).getMaterial() == Material.AIR && rand.nextInt(2) == 0)
                            	 { worldIn.setBlockState(position.add(i1, -k, j1), Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE), 2); }
                        		 else { worldIn.setBlockState(position.add(i1, -k, j1), Blocks.COBBLESTONE.getDefaultState(), 2); }
                        	 }
                        	 else if (rand.nextInt(Math.max(10+k/2,2)) == 0)
                        	 { worldIn.setBlockState(position.add(i1, -k, j1), Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2); }
                        	 else { worldIn.setBlockState(position.add(i1, -k, j1), Blocks.STONE.getDefaultState(), 2);  }
                        	 
                         }
                     }
                 }
             }
         }
		return true;
    }
}