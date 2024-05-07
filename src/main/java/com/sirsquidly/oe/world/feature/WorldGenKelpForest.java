package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockKelp;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
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

public class WorldGenKelpForest implements IWorldGenerator
{
    private double[] frozenOceanNoiseGen = new double[256];
    private NoiseGeneratorOctaves frozenOceanNoiseGenOctaves;
    
    private double[] sandNoiseGen = new double[256];
    private NoiseGeneratorOctaves warmOceanNoiseGenOctaves;
	
    private double[] kelpNoiseGen = new double[256];
    private NoiseGeneratorOctaves kelpForestNoiseGen;
    public Biome[] biomes;
    //private double placeChance = 0.2;
    
    public WorldGenKelpForest(Biome... biomes)
	{
    	this.biomes = biomes;
    	this.kelpForestNoiseGen = new NoiseGeneratorOctaves(new Random(1244), 4);
    	
    	this.warmOceanNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(2560), 4);
    	this.frozenOceanNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(5120), 4);
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{ 
		//Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
		ChunkPos chunkPos = world.getChunk(chunkX, chunkZ).getPos();
        Biome biome = world.getBiomeForCoordsBody(chunkPos.getBlock(0, 0, 0));
        
        boolean isValidBiome = false;
        
		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				isValidBiome = true;
				break;
			}
		}
		
		if (isValidBiome && ConfigHandler.worldGen.kelpForest.enableKelpForest)
		{ spawnKelpForest(world, random, chunkX, chunkZ); }
	}

    private void spawnKelpForest(World world, Random rand, int chunkX, int chunkZ)
    {
    	this.frozenOceanNoiseGen = frozenOceanNoiseGenOctaves.generateNoiseOctaves(this.frozenOceanNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	this.sandNoiseGen = warmOceanNoiseGenOctaves.generateNoiseOctaves(this.sandNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	this.kelpNoiseGen = kelpForestNoiseGen.generateNoiseOctaves(this.kelpNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, ConfigHandler.worldGen.kelpForest.kelpConnective, 1.0, ConfigHandler.worldGen.kelpForest.kelpConnective);
    	
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++)
            {   
                BlockPos pos = getSeaFloor(world, chunkX * 16 + 8 + x, chunkZ * 16 + 8 + z);
                
                if (this.kelpNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.07 > ConfigHandler.worldGen.kelpForest.kelpSpread && !(this.frozenOceanNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6) && !(this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.95)) 
                { 
                	Block blockHere = world.getBlockState(pos.up()).getBlock();
                	Block blockDown = world.getBlockState(pos).getBlock();

                	if (rand.nextDouble() < ConfigHandler.worldGen.kelpForest.kelpDensity && blockHere == Blocks.WATER && OEBlocks.KELP.canPlaceBlockAt(world, pos.up()) && blockDown != OEBlocks.KELP)
                    {
                    	growKelpStalk(world, rand, pos.up());
                    }
                }
            }
        }
    }
    
    public void growKelpStalk(World worldIn, Random rand, BlockPos pos)
    {
    	int max = BlockKelp.maxHeight;
    	for (int i = rand.nextInt(BlockKelp.randomAge + 1); i != max; ++i)
        {	
        	if(OEBlocks.KELP.canPlaceBlockAt(worldIn, pos))
        	{ 
        		worldIn.setBlockState(pos, OEBlocks.KELP.getDefaultState(), 16 | 2);
        		
        		pos = pos.up();
        	}
        	else { break; }
        }
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