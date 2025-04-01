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
    private double[] warmOceanNoiseGen = new double[256];
    private NoiseGeneratorOctaves warmOceanNoiseGenOctaves;
    public Biome[] biomes;

	/* Use a list of all Coral Fans, so we can just iterate and add generators for each instead of writing it all out. */
	List<Block> allCoral = Lists.newArrayList( OEBlocks.BLUE_CORAL, OEBlocks.PINK_CORAL, OEBlocks.PURPLE_CORAL, OEBlocks.RED_CORAL, OEBlocks.YELLOW_CORAL );
	List<Block> allCoralBlock = Lists.newArrayList( OEBlocks.BLUE_CORAL_BLOCK, OEBlocks.PINK_CORAL_BLOCK, OEBlocks.PURPLE_CORAL_BLOCK, OEBlocks.RED_CORAL_BLOCK, OEBlocks.YELLOW_CORAL_BLOCK );
	List<Block> allCoralFans = Lists.newArrayList( OEBlocks.BLUE_CORAL_FAN, OEBlocks.PINK_CORAL_FAN, OEBlocks.PURPLE_CORAL_FAN, OEBlocks.RED_CORAL_FAN, OEBlocks.YELLOW_CORAL_FAN );
    
    public GeneratorWarmOcean(Biome... biomes)
	{
    	this.biomes = biomes;
    	this.warmOceanNoiseGenOctaves = new NoiseGeneratorOctaves(new Random(2560), 4);
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{ 
		if (ConfigHandler.worldGen.warmOcean.enableWarmOcean) spawnWarmOcean(world, random, chunkX, chunkZ, chunkGenerator, chunkProvider);
	}

    private void spawnWarmOcean(World world, Random rand, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
    	this.warmOceanNoiseGen = warmOceanNoiseGenOctaves.generateNoiseOctaves(this.warmOceanNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
		/* Stored so we don't need to redo math. */
		int chunkPosX = chunkX * 16;
		int chunkPosZ = chunkZ * 16;

		boolean isValidBiome = false;
		double warmOceanNoiseGen = 0;

		/* Default is 0.6F */
		float warmOceanNoiseMin = 0.6F;
		/* Default is 0.96F */
		float coralReefNoiseMin = 0.96F;

		boolean shouldPlaceFloorDecor = false;
		boolean shouldPlaceCoralReef = false;

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++)
		{
            for (int z = 0; z < 16; z++)
			{
				int posX = chunkPosX + x;
				int posZ = chunkPosZ + z;
				mutablePos.setPos(posX, 0, posZ);
				BlockPos posWaterFloor = world.getTopSolidOrLiquidBlock(mutablePos);
				Biome biome = world.getBiomeForCoordsBody(posWaterFloor);

				for (int i = 0; i < biomes.length; i++)
				{
					if (biome == biomes[i])
					{
						isValidBiome = true;
						break;
					}
					isValidBiome = false;
				}

				warmOceanNoiseGen = this.warmOceanNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01;

				if (isValidBiome && warmOceanNoiseGen > warmOceanNoiseMin)
				{
					mutablePos.setPos(posWaterFloor.down());
					if (world.getBlockState(mutablePos).getBlock() == Blocks.GRAVEL)
					{ world.setBlockState(mutablePos, Blocks.SAND.getDefaultState(), 16 | 2); }
				}

				if (x == 15 && z == 15)
				{
					shouldPlaceFloorDecor = warmOceanNoiseGen > warmOceanNoiseMin;
					shouldPlaceCoralReef = warmOceanNoiseGen > coralReefNoiseMin;
				}
			}
		}

		if (isValidBiome)
		{
			if (shouldPlaceFloorDecor)
			{
				if (ConfigHandler.block.enableSeaPickle) new WorldGenOceanPatch(OEBlocks.SEA_PICKLE.getDefaultState(), ConfigHandler.worldGen.warmOcean.seaPickleTriesPerChunk, ConfigHandler.worldGen.warmOcean.seaPickleChancePerChunk, 16, false, biomes).setSeaLevelMinRequirement(1).setIntStatePropertyRange(BlockSeaPickle.AMOUNT, 1,4).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			}

			if (shouldPlaceCoralReef && ConfigHandler.worldGen.warmOcean.coralReef.enableCoralReef)
			{ placeCoralReef(world, rand, chunkX, chunkZ, chunkGenerator, chunkProvider); }
		}
    }

	/*
	* Places the Coral Reef, including the coral structures and scattered Fan and normal coral.
	**/
	private void placeCoralReef(World world, Random rand, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		int chunkPosX = chunkX * 16;
		int chunkPosZ = chunkZ * 16;

		for (int l = 0; l <= 7; l++)
		{
			IBlockState state = allCoralBlock.get(rand.nextInt(allCoralBlock.size())).getDefaultState();
			/** We make some new coordinates (coralPos) for generating Coral to avoid cascading world gen. Mostly the same code as my other world gen.*/
			BlockPos coralPos = world.getTopSolidOrLiquidBlock(new BlockPos(chunkPosX + 8, 0, chunkPosZ + 8).add(rand.nextInt(16), 0, rand.nextInt(16)));

			int k = rand.nextInt(11);

			if (ConfigHandler.block.coralBlocks.enableCoralBlock && coralPos.getY() <= world.getSeaLevel() - 5 && !(world.getBlockState(coralPos.down()).getBlock() instanceof BlockCoralFull))
			{
				if (k >= 8 && ConfigHandler.worldGen.warmOcean.coralReef.enableCoralBulb)
				{ new WorldGenCoralBulb(state).generate(world, rand, coralPos); }
				else if (k >= 4 && ConfigHandler.worldGen.warmOcean.coralReef.enableCoralBranch)
				{ new WorldGenCoralBranch(state).generate(world, rand, coralPos); }
				else if (ConfigHandler.worldGen.warmOcean.coralReef.enableCoralStalk)
				{ new WorldGenCoralStalk(state).generate(world, rand, coralPos); }
			}
		}

		if (ConfigHandler.block.coralBlocks.enableCoralFan)
		{
			for (Block coralFan: allCoralFans)
			{
				new WorldGenOceanPatch(coralFan.getDefaultState(), 8, 2, 48, 8, 16, 0.0, false, biomes).setSeaLevelMinRequirement(10).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			}
		}
		if (ConfigHandler.block.coralBlocks.enableCoral)
		{
			for (Block coral: allCoral)
			{
				new WorldGenOceanPatch(coral.getDefaultState(), 8, 2, 48, 8, 16, 0.0, false, biomes).setSeaLevelMinRequirement(10).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			}
		}
	}
}