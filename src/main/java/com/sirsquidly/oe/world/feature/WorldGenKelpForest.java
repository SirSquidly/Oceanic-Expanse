package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockTopKelp;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
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

public class WorldGenKelpForest implements IWorldGenerator
{
    private double[] kelpNoiseGen = new double[256];
    private NoiseGeneratorOctaves kelpForestNoiseGen;
    public Biome[] biomes;
    private double placeChance = 0.2;
    
    public WorldGenKelpForest(Biome... biomes)
	{
    	this.biomes = biomes;
    	this.kelpForestNoiseGen = new NoiseGeneratorOctaves(new Random(1244), 4);
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{ 
		Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
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
		{ spawnKelpForest(world, random, chunkX, chunkZ); }
	}

    private void spawnKelpForest(World world, Random rand, int chunkX, int chunkZ) {
    	this.kelpNoiseGen = kelpForestNoiseGen.generateNoiseOctaves(this.kelpNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.2D, 1.0, 0.2D);
    	
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++)
            {   
                BlockPos pos = getSeaFloor(world, chunkX * 16 + 8 + x, chunkZ * 16 + 8 + z);
                
                if (this.kelpNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.07 > 0.1) 
                { 
                	Block blockHere = world.getBlockState(pos.up()).getBlock();
                	Block blockDown = world.getBlockState(pos).getBlock();

                	if (rand.nextDouble() < placeChance && blockHere == Blocks.WATER && OEBlocks.KELP_TOP.canPlaceBlockAt(world, pos.up()) && blockDown != OEBlocks.KELP_TOP && blockDown != OEBlocks.KELP)
                    {
                    	world.setBlockState(pos.up(), OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(rand.nextInt(BlockTopKelp.randomAge + 1))), 16 | 2);
                    	growKelpStalk(world, rand, pos.up());
                    }
                }
            }
        }
    }
    
    public boolean growKelpStalk(World worldIn, Random rand, BlockPos pos)
    {
    	IBlockState state = worldIn.getBlockState(pos);
        
        if (worldIn.getBlockState(pos).getBlock() == OEBlocks.KELP_TOP)
        {
        	for (int i = ((Integer)state.getValue(BlockTopKelp.AGE)).intValue(); i != BlockTopKelp.maxHeight; ++i)
            {	
            	if(OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, pos.up()))
            	{ 
            		worldIn.setBlockState(pos.up(), OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(i)), 16 | 2);
            		worldIn.setBlockState(pos, OEBlocks.KELP.getDefaultState(), 16 | 2);
            		
            		pos = pos.up();
            		state = worldIn.getBlockState(pos);
            	}
            	else { break; }
            }
        }
        return true;
    }
    
    public static BlockPos getSeaFloor(World world, int x, int z)
    {
    	int yPos = Math.max(world.getSeaLevel() - 2, 1);;
    	BlockPos pos = new BlockPos(x, yPos, z);
    	
        for (; pos.getY() > 0; pos = pos.down())
        {
        	IBlockState state = world.getBlockState(pos);
        	if (!(state.getBlock().isReplaceable(world, pos)) && state.getMaterial() != Material.LEAVES && state.getMaterial() != Material.ICE)
        	{ break;}
        }
        return pos;
    }
}