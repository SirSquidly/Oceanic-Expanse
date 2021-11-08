package com.sirsquidly.oe.world;

import java.util.Random;

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
		spawnWarmOcean(world, random, chunkX, chunkZ);
	}

    private void spawnWarmOcean(World world, Random rand, int chunkX, int chunkZ) {
    	this.sandNoiseGen = warmOceanNoiseGenOctaves.generateNoiseOctaves(this.sandNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++)
            {   
                BlockPos pos = getSeaFloor(world, chunkX * 16 + 8 + x, chunkZ * 16 + 8 + z);
                BlockPos posSurface = new BlockPos(chunkX * 16 + 8 + x, Math.max(world.getSeaLevel() - 1, 1), chunkZ * 16 + 8 + z);
                
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
                
                if (isValidBiome && this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6) 
                { 
                	if (world.getBlockState(pos).getBlock() == Blocks.GRAVEL)
                    { world.setBlockState(pos, Blocks.SAND.getDefaultState(), 16 | 2); }
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
        	if (!(state.getBlock().isReplaceable(world, pos)) && state.getMaterial() != Material.LEAVES && state.getMaterial() != Material.ICE && state.getMaterial() != Material.WATER && !(state.getBlock() instanceof BlockPrismarine))
        	{ break;}
        }
        return pos;
    }
}