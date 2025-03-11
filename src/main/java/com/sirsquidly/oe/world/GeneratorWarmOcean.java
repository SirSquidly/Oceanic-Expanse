package com.sirsquidly.oe.world;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.blocks.BlockCoralFull;
import com.sirsquidly.oe.blocks.BlockSeaPickle;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.world.feature.WorldGenOceanPatch;
import com.sirsquidly.oe.world.feature.coral.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GeneratorWarmOcean implements IWorldGenerator
{
    private double[] sandNoiseGen = new double[256];
    private NoiseGeneratorOctaves warmOceanNoiseGenOctaves;
    public Biome[] biomes;
    
    public GeneratorWarmOcean(Biome... biomes)
	{
    	this.biomes = biomes;
    	this.warmOceanNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(2560), 4);
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{ 
		spawnWarmOcean(world, random, chunkX, chunkZ, chunkGenerator, chunkProvider);
	}

    private void spawnWarmOcean(World world, Random rand, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
    	this.sandNoiseGen = warmOceanNoiseGenOctaves.generateNoiseOctaves(this.sandNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++)
            {   
            	
                BlockPos pos = getSeaFloor(world, (chunkX * 16) + x + 8, (chunkZ * 16) + z + 8);
                Biome biome = world.getBiome(pos);
                boolean isValidBiome = false;		
                for(int i = 0; i < biomes.length; i++)
        		{
        			if(biome == biomes[i])
        			{
        				isValidBiome = true;
        				break;
        			}
        		}
                
                if (isValidBiome && this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6 && ConfigHandler.worldGen.warmOcean.enableWarmOcean) 
                { 
                	if (world.getBlockState(pos).getBlock() == Blocks.GRAVEL) world.setBlockState(pos, Blocks.SAND.getDefaultState(), 16 | 2);
                	
                	/** Doesn't use new coordinates like the coral because it doesn't seem to be causing any issues as is..*/
                	if (x == 0 && z == 0)
                	{
                		if (ConfigHandler.block.enableSeaPickle) new WorldGenOceanPatch(OEBlocks.SEA_PICKLE.getDefaultState(), ConfigHandler.worldGen.warmOcean.seaPickleTriesPerChunk, ConfigHandler.worldGen.warmOcean.seaPickleChancePerChunk, 16, false, biomes).setSeaLevelMinRequirement(1).setIntStatePropertyRange(BlockSeaPickle.AMOUNT, 1,4).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                	}
                }
                
                /** This chunk does all the work for Coral Reefs. */
                if (isValidBiome && this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.96 && ConfigHandler.worldGen.warmOcean.coralReef.enableCoralReef && ConfigHandler.worldGen.warmOcean.enableWarmOcean) 
                { 
                	if (x == 0 && z == 0)
                	{
                		for (int l = 0; l < 8; l++)
        				{
                    		/** We make some new coordinates (coralPos) for generating Coral to avoid cascading world gen. Mostly the same code as my other world gen.*/
                    		int xPos = rand.nextInt(16);
            				int zPos = rand.nextInt(16);
            				BlockPos coralPos = pos.add(xPos, 0, zPos);
            				coralPos = getSeaFloor(world, coralPos.getX(), coralPos.getZ()).up();
            				
        					int k = rand.nextInt(11);
        					
        					if (ConfigHandler.block.coralBlocks.enableCoralBlock && coralPos.getY() <= world.getSeaLevel() - 5)
        					{
        						if (k >= 8 && ConfigHandler.worldGen.warmOcean.coralReef.enableCoralBulb)
            					{
            						new WorldGenCoralBulb(0).generate(world, rand, coralPos);
            					}
            					else if (k >= 4 && ConfigHandler.worldGen.warmOcean.coralReef.enableCoralBranch)
            					{
            						new WorldGenCoralBranch(0).generate(world, rand, coralPos);
            					}
            					else if (ConfigHandler.worldGen.warmOcean.coralReef.enableCoralStalk)
            					{
            						new WorldGenCoralStalk(0).generate(world, rand, coralPos);
            					}
        					}
        					
        					if (l == 7)
                    		{
        						if (ConfigHandler.block.coralBlocks.enableCoralFan)
        						{
									/* Use a list of all Coral Fans, so we can just iterate and add generators for each instead of writing it all out. */
									List<Block> allCoralFans = Lists.newArrayList( OEBlocks.BLUE_CORAL_FAN, OEBlocks.PINK_CORAL_FAN, OEBlocks.PURPLE_CORAL_FAN, OEBlocks.RED_CORAL_FAN, OEBlocks.YELLOW_CORAL_FAN );

									for (Block coralFan: allCoralFans)
									{
										new WorldGenOceanPatch(coralFan.getDefaultState(), 8, 2, 48, 8, 16, 0.0, false, biomes).setSeaLevelMinRequirement(10).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
									}
        						}
                        		if (ConfigHandler.block.coralBlocks.enableCoral)
                        		{
									/* Use a list of all Coral, exact same as above list for Coral Fans. */
									List<Block> allCoral = Lists.newArrayList( OEBlocks.BLUE_CORAL, OEBlocks.PINK_CORAL, OEBlocks.PURPLE_CORAL, OEBlocks.RED_CORAL, OEBlocks.YELLOW_CORAL );

									for (Block coral: allCoral)
									{
										new WorldGenOceanPatch(coral.getDefaultState(), 8, 2, 48, 8, 16, 0.0, false, biomes).setSeaLevelMinRequirement(10).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
									}
                        		}
                    		}
        				}
                	}
                }
            }
        }
    }
        
    public static BlockPos getSeaFloor(World world, int x, int z)
    {
    	int yPos = Math.max(world.getSeaLevel() - 2, 1);;
    	BlockPos pos = new BlockPos(x, yPos, z);
    	
        for (; pos.getY() > 0; pos = pos.down())
        {
        	IBlockState state = world.getBlockState(pos);
        	if (!(state.getBlock().isReplaceable(world, pos)) && state.getMaterial() != Material.LEAVES && state.getMaterial() != Material.ICE && state.getMaterial() != Material.WATER && !(state.getBlock() instanceof BlockPrismarine) && !(state.getBlock() instanceof BlockCoralFull))
        	{ break;}
        }
        return pos;
    }
}