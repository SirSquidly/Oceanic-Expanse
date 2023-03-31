package com.sirsquidly.oe.world;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.world.feature.WorldGenBlueIce;
import com.sirsquidly.oe.world.feature.WorldGenOceanPatch;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockStone;
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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GeneratorFrozenOcean implements IWorldGenerator
{
    private double[] sandNoiseGen = new double[256];
    private NoiseGeneratorOctaves warmOceanNoiseGenOctaves;
    private double[] frozenOceanNoiseGen = new double[256];
    private NoiseGeneratorOctaves frozenOceanNoiseGenOctaves;
    private double[] iceSheetNoiseGen = new double[256];
    private NoiseGeneratorOctaves iceSheetNoiseGenOctaves;
    private double[] icebergIceNoiseGen = new double[256];
    private double[] icebergSnowNoiseGen = new double[256];
    //private double[] icebergSnowLayerNoiseGen = new double[256];
    private NoiseGeneratorOctaves icebergNoiseGen;
    public Biome[] biomes;
    
    public GeneratorFrozenOcean(Biome... biomes)
	{
    	this.biomes = biomes;
    	this.warmOceanNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(2560), 4);
    	this.frozenOceanNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(5120), 4);
    	this.iceSheetNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(1280), 4);
    	this.icebergNoiseGen = new NoiseGeneratorOctaves(new Random(3840), 6);
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{ 
		spawnFrozenOcean(world, random, chunkX, chunkZ, chunkGenerator, chunkProvider);
	}

    private void spawnFrozenOcean(World world, Random rand, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    	this.sandNoiseGen = warmOceanNoiseGenOctaves.generateNoiseOctaves(this.sandNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	this.frozenOceanNoiseGen = frozenOceanNoiseGenOctaves.generateNoiseOctaves(this.frozenOceanNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	this.iceSheetNoiseGen = iceSheetNoiseGenOctaves.generateNoiseOctaves(this.iceSheetNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.225D, 1.0, 0.225D);
    	
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++)
            {   
                BlockPos pos = getSeaFloor(world, chunkX * 16 + 8 + x, chunkZ * 16 + 8 + z);
                BlockPos posSurface = new BlockPos(chunkX * 16 + 8 + x, Math.max(world.getSeaLevel() - 1, 1), chunkZ * 16 + 8 + z);
                
                Biome biome = world.getBiomeForCoordsBody(pos);
                
                boolean isValidBiome = false;
                boolean isBeachBiome = false;	
                
                for(int i = 0; i < biomes.length; i++)
        		{
        			if(biome == biomes[i])
        			{
        				isValidBiome = true;
        				if (BiomeDictionary.hasType(biome, Type.BEACH)) isBeachBiome = true;
        				
        				break;
        			}
        		}
                
                if (isValidBiome && ConfigHandler.worldGen.frozenOcean.enableFrozenOcean && this.frozenOceanNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6 && !(this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6)) 
                { 
                	if (world.getBlockState(pos).getBlock() == Blocks.SAND)
                    { world.setBlockState(pos, Blocks.GRAVEL.getDefaultState(), 16 | 2); }
                	
                	if (ConfigHandler.worldGen.frozenOcean.enableIcebergs && !isBeachBiome) spawnIceBerg(world, rand, pos, chunkX, chunkZ, x, z);
                	
                	floatingIceCleaner(world, posSurface);
                	
                	if (ConfigHandler.worldGen.frozenOcean.enableIceSheet && this.iceSheetNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.225 > ConfigHandler.worldGen.frozenOcean.iceSheetSpread) 
                    { 
                    	if (world.getBlockState(posSurface).getBlock() == Blocks.WATER)
                        { world.setBlockState(posSurface, Blocks.ICE.getDefaultState(), 16 | 2); }
                    }
                	
                	/** Doesn't use new coordinates because it doesn't seem to be causing any issues as is.*/
                	if (x == 15 && z == 15)
                	{
                		if (ConfigHandler.worldGen.frozenOcean.frozenSeafloor.enableRockDecor) spawnRockDecor(world, rand, pos, chunkX, chunkZ, x, z);
                		
                		new WorldGenOceanPatch(OEBlocks.SEASTAR, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.seastarTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.seastarChancePerChunk, 16, false, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                		new WorldGenOceanPatch(OEBlocks.TUBE_SPONGE, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.tubeSpongeTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.tubeSpongeChancePerChunk, 8, 4, 4, 0.0, false, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                    	new WorldGenOceanPatch(OEBlocks.DULSE, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.dusleTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.dulseChancePerChunk, 8, 4, 4, 0.0, false, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                    	
                    	new WorldGenBlueIce(ConfigHandler.worldGen.frozenOcean.frozenSeafloor.blueIceTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.blueIceChancePerChunk, 50, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);;
                	}
                }
            }
        }
    }
    
    /** Generates the Icebergs. I'm sorry for this absolute mess. */
    private void spawnIceBerg(World world, Random rand, BlockPos pos, int chunkX, int chunkZ, int x, int z)
    {
    	this.icebergIceNoiseGen = icebergNoiseGen.generateNoiseOctaves(this.icebergIceNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.825D, 1.0, 0.825D);
    	this.icebergSnowNoiseGen = icebergNoiseGen.generateNoiseOctaves(this.icebergSnowNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 1.0D, 1.0, 1.0D);
    	
    	for (int y = 150; y >= world.getSeaLevel() - 20; y--)
    	{
    		BlockPos Icepos = new BlockPos(chunkX * 16 + 8 + x, y, chunkZ * 16 + 8 + z);
		
    		// Comment Hell    V Seperate gen used for lower part of iceberg                                   V Smoothens Noise            /Scales berg-gen into 'tiers'   \
    		// Top Layer, Spiked
    		if (y >= 80 && this.icebergIceNoiseGen[x * 16 + z] / 6 - rand.nextDouble() * 0.01 > 1.8 + ((y - 30) * 0.03 + ((y - 60) * y * .0003))    )
        	{
    			if (world.getBlockState(Icepos).getMaterial() == Material.WATER || world.getBlockState(Icepos).getBlock() == Blocks.ICE || world.getBlockState(Icepos).getMaterial() == Material.AIR)
    			{ 
    				world.setBlockState(Icepos, Blocks.PACKED_ICE.getDefaultState(), 16 | 2);
    	    	}
        	}
    		// Mid layer, smoothens out
    		if (y < 80 && y >= world.getSeaLevel() + 1 && this.icebergIceNoiseGen[x * 16 + z] / 6 - rand.nextDouble() * 0.01 > 0.0004 + ((y - 30) * 0.03 + ((y - 60) * y * .0015))    )
        	{
    			if (world.getBlockState(Icepos).getMaterial() == Material.WATER || world.getBlockState(Icepos).getBlock() == Blocks.ICE || world.getBlockState(Icepos).getMaterial() == Material.AIR)
    			{ 
    				world.setBlockState(Icepos, Blocks.PACKED_ICE.getDefaultState(), 16 | 2);
    	    	}
        	}
    		// Lower, sharp edges
    		if (y < world.getSeaLevel() + 1 && y > world.getSeaLevel() - 35) 
        	{ 
    			if (this.icebergIceNoiseGen[x * 16 + z] / 6 - rand.nextDouble() * 0.25 > 0.8 + (Math.round(1000 / Math.max(y - 45,1) * 1000)*.00001))
    			{
    				if (world.getBlockState(Icepos).getMaterial() == Material.WATER || world.getBlockState(Icepos).getBlock() == Blocks.ICE || world.getBlockState(Icepos).getMaterial() == Material.AIR)
        			{ 
        				world.setBlockState(Icepos, Blocks.PACKED_ICE.getDefaultState(), 16 | 2);
        			}
    			}
        	}
    		
    		 /** This generates the snow blocks, passing good positions to 'placeSnowDown' */
    		if (y > world.getSeaLevel() + 1 && world.getBlockState(Icepos).getBlock() == Blocks.PACKED_ICE)
    		{
    			if (this.icebergSnowNoiseGen[x * 16 + z] / 8 - rand.nextDouble() * 0.4 > 0.6 - ((pos.getY() - 30) * 0.009))
        		{
    				if (world.getBlockState(Icepos.up()).getMaterial() == Material.AIR)
        			{ placeSnowDown(world, Icepos); }
        		}
    		}
    	}
    	
    }
    
    /** Places the snow pillars in the ice */
    private void placeSnowDown(World worldIn, BlockPos pos)
    {
    	int innnnn = (pos.getY() / 5) - 12;
    	
    	for (int i = 0; i < Math.min(Math.max(innnnn + 1, 2), 5); i++)
    	{
    		worldIn.setBlockState(pos.down(i), Blocks.SNOW.getDefaultState(), 16 | 2);
    	}
    }
    
    /** Removes bits of Ice clearly over the water */
    public void floatingIceCleaner(World world, BlockPos pos)
    {
    	//pos = new BlockPos(pos.getX(), Math.min(world.getSeaLevel() - 2, 256), pos.getZ());
        for (; pos.getY() < world.getSeaLevel() + 3; pos = pos.up())
        {
        	if (world.getBlockState(pos).getBlock() == Blocks.PACKED_ICE && ((world.getBlockState(pos.up()).getBlock() != Blocks.PACKED_ICE && world.getBlockState(pos.up()).getBlock() != Blocks.SNOW) || world.getBlockState(pos.down()).getBlock() != Blocks.PACKED_ICE))
    		{ 
        		if (pos.getY() <= world.getSeaLevel() - 1)
            	{ world.setBlockState(pos, Blocks.WATER.getDefaultState(), 16 | 2); }
        		else
        		{ world.setBlockState(pos, Blocks.AIR.getDefaultState(), 16 | 2); }
        	}
        }
        
        
    }
    
    /** Generates small random bits of Granite in the sea floor, and chunks of Andesite up. */
    private void spawnRockDecor(World worldIn, Random rand, BlockPos pos, int chunkX, int chunkZ, int x, int z)
    {
    	int xPos = rand.nextInt(16) + 8;
		int zPos = rand.nextInt(16) + 8;
		ChunkPos chunkPos = worldIn.getChunkFromChunkCoords(chunkX, chunkZ).getPos();
		BlockPos floorPos = chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos);
		floorPos = getSeaFloor(worldIn, floorPos.getX(), floorPos.getZ()).up();
		
		int rX = rand.nextInt(8) - rand.nextInt(8);
    	int rZ = rand.nextInt(8) - rand.nextInt(8);
    	
        BlockPos randBlockPos = floorPos.add(rX, 0, rZ);
        
        
        if (rand.nextInt(6) == 0 && worldIn.getBlockState(randBlockPos.down()).isFullBlock())
        {
        	worldIn.setBlockState(randBlockPos.down(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	worldIn.setBlockState(randBlockPos.down().north(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	worldIn.setBlockState(randBlockPos.down().north().west(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	worldIn.setBlockState(randBlockPos.down().west(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	
        	if (rand.nextInt(2) == 0) worldIn.setBlockState(randBlockPos, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	if (rand.nextInt(2) == 0) worldIn.setBlockState(randBlockPos.north(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	if (rand.nextInt(2) == 0) worldIn.setBlockState(randBlockPos.north().west(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
        	if (rand.nextInt(2) == 0) worldIn.setBlockState(randBlockPos.west(), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 16 | 2);
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