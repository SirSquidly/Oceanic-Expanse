package com.sirsquidly.oe.world;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockTubeSponge;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.world.feature.WorldGenBlueIce;
import com.sirsquidly.oe.world.feature.WorldGenOceanPatch;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
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
	private double[] icebergCircleNoiseGen = new double[256];
	private double[] icebergSnowNoiseGen = new double[256];
	//private double[] icebergSnowLayerNoiseGen = new double[256];
	private NoiseGeneratorOctaves icebergNoiseGen;
	public Biome[] biomes;
	public ChunkGeneratorSettings chunkProviderSettings;
    
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
	{ spawnFrozenOcean(world, random, chunkX, chunkZ, chunkGenerator, chunkProvider); }

    private void spawnFrozenOcean(World world, Random rand, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		/* Stored so we don't need to redo math. */
		int chunkPosX = chunkX * 16 + 8;
		int chunkPosZ = chunkZ * 16 + 8;
		int groundReplaceLowest = world.getSeaLevel() - 3;

    	this.sandNoiseGen = warmOceanNoiseGenOctaves.generateNoiseOctaves(this.sandNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	this.frozenOceanNoiseGen = frozenOceanNoiseGenOctaves.generateNoiseOctaves(this.frozenOceanNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.00764D, 1.0, 0.00764D);
    	this.iceSheetNoiseGen = iceSheetNoiseGenOctaves.generateNoiseOctaves(this.iceSheetNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.225D, 1.0, 0.225D);
		this.icebergIceNoiseGen = icebergNoiseGen.generateNoiseOctaves(this.icebergIceNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 1.0D, 1.0, 1.0D);
		this.icebergCircleNoiseGen = icebergNoiseGen.generateNoiseOctaves(this.icebergCircleNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 1.0D, 1.0, 1.0D);
		this.icebergSnowNoiseGen = icebergNoiseGen.generateNoiseOctaves(this.icebergSnowNoiseGen, chunkX * 16, 0, chunkZ * 16, 16, 1, 16, 0.825, 1.0, 0.825);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++)
            {
				int posX = chunkPosX + x;
				int posZ = chunkPosZ + z;

				/** Gets the lowest non-water block, usually for the Sea Floor */
				BlockPos posWaterFloor = world.getTopSolidOrLiquidBlock(new BlockPos(posX, 0, posZ));
                Biome biome = world.getBiomeForCoordsBody(posWaterFloor);
                
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

				/** The scale of Frozen Oceans. Noise filter, so smaller number = larger results. 4 is default! */
				double frozenOceanScale = 4;

				if (isValidBiome && ConfigHandler.worldGen.frozenOcean.enableFrozenOcean && this.frozenOceanNoiseGen[x * 16 + z] / frozenOceanScale - rand.nextDouble() * 0.01 > 0.6 && !(this.sandNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.01 > 0.6))
				{
					/** Used by Features that are placed at Sea Level exactly */
					BlockPos posSeaLevel = new BlockPos(posX, world.getSeaLevel(), posZ);

					if (world.getBlockState(posWaterFloor.down()).getBlock() == Blocks.SAND && posWaterFloor.getY() < groundReplaceLowest)
					{ world.setBlockState(posWaterFloor.down(), Blocks.GRAVEL.getDefaultState(), 16 | 2); }

					if (ConfigHandler.worldGen.frozenOcean.enableIcebergs && !isBeachBiome)
					{
						spawnIceBerg(world, rand, posSeaLevel, chunkX, chunkZ, x, z, false, 0.3, 0.01, 3, 1);
						spawnIceBerg(world, rand, posSeaLevel, chunkX, chunkZ, x, z, true, 0.3, 0.01, 10, 0.5);
						icebergToppings(world, rand, posSeaLevel, chunkX, chunkZ, x, z);
					}

					//floatingIceCleaner(world, posSurface);

					if (ConfigHandler.worldGen.frozenOcean.enableIceSheet && this.iceSheetNoiseGen[x * 16 + z] / 4 - rand.nextDouble() * 0.225 > ConfigHandler.worldGen.frozenOcean.iceSheetSpread)
					{
						if (world.getBlockState(posSeaLevel.down()).getBlock().isReplaceable(world, posSeaLevel.down()))
						{ world.setBlockState(posSeaLevel.down(), Blocks.ICE.getDefaultState(), 16 | 2); }
					}

					if (x == 0 && z == 0)
					{
						if (ConfigHandler.worldGen.frozenOcean.frozenSeafloor.enableRockDecor) spawnRockDecor(world, rand, posWaterFloor, chunkX, chunkZ, x, z);
						if (ConfigHandler.block.enableSeastar) new WorldGenOceanPatch(OEBlocks.SEASTAR.getDefaultState(), ConfigHandler.worldGen.frozenOcean.frozenSeafloor.seastarTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.seastarChancePerChunk, 16, false, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
						if (ConfigHandler.block.tubeSponge.enableTubeSponge) new WorldGenOceanPatch(OEBlocks.TUBE_SPONGE.getDefaultState(), ConfigHandler.worldGen.frozenOcean.frozenSeafloor.tubeSpongeTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.tubeSpongeChancePerChunk, 8, 4, 4, 0.0, false, biomes).setSeaLevelMinRequirement(10).setIntStatePropertyRange(BlockTubeSponge.AGE, 0,3).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
						if (ConfigHandler.block.dulse.enableDulse) new WorldGenOceanPatch(OEBlocks.DULSE.getDefaultState(), ConfigHandler.worldGen.frozenOcean.frozenSeafloor.dusleTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.dulseChancePerChunk, 8, 4, 4, 0.0, false, biomes).setSeaLevelMinRequirement(1).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
					}
					else if (x == 15 && z == 15)
					{
						if (ConfigHandler.block.blueIce.enableBlueIce) new WorldGenBlueIce(ConfigHandler.worldGen.frozenOcean.frozenSeafloor.blueIceTriesPerChunk, ConfigHandler.worldGen.frozenOcean.frozenSeafloor.blueIceChancePerChunk, 50, biomes).generate(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
					}
				}
			}
        }
    }

	/** Generates the Icebergs. I'm sorry for this absolute mess.
	 *
	 *  `scale` affects the scale of the entire Iceberg. Noise filter, so smaller number = larger results. 1.8 is Default.
	 *	`vertScale` affects the verticle scale of the TOP of the iceberg.
	 * */
	private void spawnIceBerg(World world, Random rand, BlockPos pos, int chunkX, int chunkZ, int x, int z, boolean stack, double scale, double vertScale, double heightSmoothening, double slopeFactor)
	{
		final int seaLevel = world.getSeaLevel();
		final int maxDepth = seaLevel - 18;
		final int maxHeight = Math.min(seaLevel + 40, 256);

		double noiseGen = this.icebergIceNoiseGen[x * 16 + z] / 2;
		double noiseCircleGen = this.icebergCircleNoiseGen[x * 16 + z];

		for (int y = maxHeight; y >= maxDepth; y--)
		{
			BlockPos icePos = new BlockPos(chunkX * 16 + 8 + x, y, chunkZ * 16 + 8 + z);
			/* Block height above or below Sea Level. */
			int seaLevelDifference = y - seaLevel;

			/* Used to round off the tips of the Icebergs. */
			double topHeightRatio = Math.pow((double) seaLevelDifference / heightSmoothening, slopeFactor);

			/* Used for scaling the bottom of the Iceberg, smaller = larger changes. */
			double bottomVertScale = vertScale / 1.5;
			double bottomHeightRatio = Math.pow(((double) -seaLevelDifference * 2 / heightSmoothening) / 4, slopeFactor);
			double bottomHeightDropoff = scale - seaLevelDifference * bottomVertScale + bottomHeightRatio;

			/* Sets the SeaLevel and one block above to use the same result, giving Icebergs the sharp edged where they touch the water. */
			if (seaLevelDifference <= 0)
			{
				seaLevelDifference = 1;
				topHeightRatio = Math.pow((double) seaLevelDifference / heightSmoothening, slopeFactor);
			}

			double topHeightDropoff = scale + seaLevelDifference * vertScale + topHeightRatio;

			if (y > seaLevel - 2)
			{
				if (noiseGen / 6 > topHeightDropoff && (!stack || noiseCircleGen > topHeightDropoff))
				{
					if (world.getBlockState(icePos).getMaterial() == Material.WATER || world.getBlockState(icePos).getBlock() == Blocks.ICE || world.getBlockState(icePos).getMaterial() == Material.AIR)
					{ placeIcebergBlock(world, icePos); }
				}
			}
			else
			{
				/* Block noise on the edges of the bottom half of the iceberg. Large = more noise.*/
				double bottomEdgeNoise = 0.2;

				if (noiseGen / 6 - rand.nextDouble() * bottomEdgeNoise > bottomHeightDropoff && (!stack || noiseCircleGen / 1 > bottomHeightDropoff))
				{
					if (world.getBlockState(icePos).getMaterial() == Material.WATER || world.getBlockState(icePos).getBlock() == Blocks.ICE || world.getBlockState(icePos).getMaterial() == Material.AIR)
					{ placeIcebergBlock(world, icePos); }
				}
			}
		}
	}


	/** Generates the now atop Icebergs. */
	private void icebergToppings(World world, Random rand, BlockPos pos, int chunkX, int chunkZ, int x, int z)
	{
		for (int y = Math.min(world.getSeaLevel() + 42, 256); y >= world.getSeaLevel() - 18; y--)
		{
			BlockPos Icepos = new BlockPos(chunkX * 16 + 8 + x, y, chunkZ * 16 + 8 + z);
			/** Block height above or below Sea Level. */
			int seaLevelDifference = y - world.getSeaLevel();

			/** Block noise on the edges of the snow patches. Large = more noise.*/
			double edgeNoise = 0.8;

			double snowNoiseGen = this.icebergSnowNoiseGen[x * 16 + z];

			/** Should expand the Snow Patches as height increases.*/
			double heightScaling = seaLevelDifference * 0.2 - 3;

			if (y > world.getSeaLevel() - 2)
			{
				if (snowNoiseGen / 8 - rand.nextDouble() * edgeNoise < heightScaling && world.getBlockState(Icepos).getBlock() == Blocks.PACKED_ICE && world.getBlockState(Icepos.up(seaLevelDifference/3)).getMaterial() == Material.AIR)
				{
					placeIcebergTopBlock(world, Icepos);
				}
			}
		}
	}

	/** Places blocks that make up whole Icebergs. */
	private void placeIcebergBlock(World worldIn, BlockPos pos)
	{ worldIn.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState(), 16 | 2); }

	/** Places blocks that are part of the top of Icebergs. */
	private void placeIcebergTopBlock(World worldIn, BlockPos pos)
	{ worldIn.setBlockState(pos, Blocks.SNOW.getDefaultState(), 16 | 2); }
    
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
		ChunkPos chunkPos = worldIn.getChunk(chunkX, chunkZ).getPos();
		BlockPos floorPos = chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos);
		floorPos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(floorPos.getX(), 0, floorPos.getZ()));

		IBlockState graniteState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);
		
		int rX = rand.nextInt(8) - rand.nextInt(8);
    	int rZ = rand.nextInt(8) - rand.nextInt(8);
    	
        BlockPos randBlockPos = floorPos.add(rX, 1, rZ);
        
        
        if (rand.nextInt(6) == 0 && worldIn.getBlockState(randBlockPos.down()).isFullBlock())
        {
			BlockPos[] basePositions = { randBlockPos.down(), randBlockPos.down().north(), randBlockPos.down().west(), randBlockPos.down().north().west() };

			for (BlockPos posBase : basePositions)
			{
				worldIn.setBlockState(posBase, graniteState, 16 | 2);
				if (rand.nextBoolean()) worldIn.setBlockState(posBase.up(), graniteState, 16 | 2);
			}
        }
    }
}