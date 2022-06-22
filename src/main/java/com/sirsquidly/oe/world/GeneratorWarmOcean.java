package com.sirsquidly.oe.world;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockCoralFull;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.world.feature.WorldGenSeagrass;
import com.sirsquidly.oe.world.feature.coral.*;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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
            	ChunkPos chunkPos = world.getChunkFromChunkCoords(chunkX, chunkZ).getPos();
                BlockPos pos = getSeaFloor(world, chunkPos.getXStart() + x, chunkPos.getZStart() + z);
                /** Does 2 SEPERATE check types to try and make the generator as clean as possible. Still isn't COMPLETELY GOOD..*/
                Biome biome = world.getBiomeForCoordsBody(chunkPos.getBlock(pos.getX(), pos.getY(), pos.getZ()));
                Biome biome1 = world.getBiomeForCoordsBody(chunkPos.getBlock(0, 0, 0));
                
                boolean isValidBiome = false;		
                for(int i = 0; i < biomes.length; i++)
        		{
        			if(biome == biomes[i] || biome1 == biomes[i])
        			{
        				isValidBiome = true;
        				break;
        			}
        		}
                
                if (isValidBiome && this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6) 
                { 
                	if (world.getBlockState(pos).getBlock() == Blocks.GRAVEL) world.setBlockState(pos, Blocks.SAND.getDefaultState(), 16 | 2);
                	
                	//** Doesn't use new coordinates like the coral because it doesn't seem to be causing any issues as is..*/
                	new WorldGenSeagrass(OEBlocks.SEA_PICKLE, 1, 6, 4, 0.4, false, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                }
                
                //** This chunk does all the work for Coral Reefs. */
                if (isValidBiome && this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.96) 
                { 
                	if(rand.nextInt(30) == 0)
    				{
                		//** We make some new coordinates (coralPos) for generating Coral to avoid cascading world gen. Mostly the same code as my other world gen.*/
                		int xPos = rand.nextInt(16) + 8;
        				int zPos = rand.nextInt(16) + 8;
        				BlockPos coralPos = chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos);
        				coralPos = getSeaFloor(world, coralPos.getX(), coralPos.getZ()).up();
        				
    					int k = rand.nextInt(11);
    					
    					if (k >= 8)
    					{
    						new WorldGenCoralBulb(0).generate(world, rand, coralPos);
    					}
    					else if (k >= 4)
    					{
    						new WorldGenCoralBranch(0).generate(world, rand, coralPos);
    					}
    					else
    					{
    						new WorldGenCoralStalk(0).generate(world, rand, coralPos);
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