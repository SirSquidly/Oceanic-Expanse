package com.sirsquidly.oe.world.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sirsquidly.oe.blocks.*;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Used for generating patches of plants/blocks on the ocean floor(EX. Seagrass or Tube Sponge).
 */
public class WorldGenOceanPatch implements IWorldGenerator
{
	private IBlockState blockState;
	private int patchAmount;
	/** Currently exclusive for Seagrass. Float chance (0.0 - 1.0) for generating Double/Tall Seagrass. Higher is more likely.*/
	private double tallChance;
	private int attemptsPerChunk;
	private int chancePerAttempt;
	/** Flips the generator to run from the world bottom up, always failing if the placed Seagrass has sky access. For underground / beneath structure generation.*/
	private boolean bottomUp;
	/** Spread in positive and negative directions from origin to try and place at*/
	private int placeSpreadXZ = 8;
	/** See placeSpreadXZ. The same, but on the Y axis.*/
	private int placeSpreadY = 4;
	private Biome[] biomes;

	/** Blocks below Sea Level this block requires to be generated. -1 disables this check, and is the default.*/
	private int seaLevelMin = -1;
	/** Use if we want to not use a Block's hard defined worldgen settings, such as for Dulse. */
	private boolean overrideBlockGenInterface;
	/** Stores IProperties (Block Properties) and int arrays together. */
	private final Map<IProperty<Integer>, int[]> propertyRanges = new HashMap<>();
	/** Simplified because not every use needs to specify the spread or seagrass height chance.*/
	public WorldGenOceanPatch(IBlockState blockStateIn, int perChunk, int perAttempt, int amount, boolean rising, Biome... biomes)
	{
		this(blockStateIn, perChunk, perAttempt, amount, 8, 4, 0.0, rising, biomes);
	}
	
	public WorldGenOceanPatch(IBlockState blockStateIn, int perChunk, int perAttempt, int amount, int spreadXZ, int spreadY, double tall, boolean rising, Biome... biomes)
	{
		this.blockState = blockStateIn;
		this.attemptsPerChunk = perChunk;
		this.chancePerAttempt = perAttempt; 
		this.patchAmount = amount;
		this.placeSpreadXZ = spreadXZ;
		this.placeSpreadY = spreadY;
		this.tallChance = tall;
		this.bottomUp = rising;
		this.biomes = biomes;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
        boolean isValidBiome = false;

        ChunkPos chunkPos = world.getChunk(chunkX, chunkZ).getPos();
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
				int yPos = Math.max(world.getSeaLevel() - 1, 1);
				if (bottomUp) { yPos = 1; }
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
					
					if (bottomUp) 
					{ 
						for ( @SuppressWarnings("unused") IBlockState state = world.getBlockState(pos); !world.canBlockSeeSky(pos) && pos.getY() < world.getHeight(); state = world.getBlockState(pos) )
						{ 
							pos = pos.up();
							
							if(blockState.getBlock().canPlaceBlockAt(world, pos.down()))
							{ beginPatchPlacing(world, rand, pos); break;}
						}
					}
					else
					{ 
						for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        	{ pos = pos.down(); }
						
						if(blockState.getBlock().canPlaceBlockAt(world, pos.up()))
						{ beginPatchPlacing(world, rand, pos); }
					}
				}
			}
		}
	}
	
	/** Grabs random positions near the patch's starting pos to place the blocks at. **/
    public boolean beginPatchPlacing(World worldIn, Random rand, BlockPos position)
    {
        for (int i = 0; i < patchAmount; ++i)
        {	
        	int rX = rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
        	int rZ = rand.nextInt(placeSpreadXZ) - rand.nextInt(placeSpreadXZ);
        	
            BlockPos blockpos = position.add(rX, rand.nextInt(placeSpreadY) - rand.nextInt(placeSpreadY), rZ);
            
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && this.blockState.getBlock().canPlaceBlockAt(worldIn, blockpos))
            {
            	placeBlock(worldIn, rand, blockpos);
            }
        }
        return true;
    }

	/** Used for placing the individual blocks because many blocks require specific checks/randomizing. **/
	public void placeBlock(World worldIn, Random rand, BlockPos pos)
	{
		/* Some blocks (ex. Dulse) require specialized placement, so they handle their own placement within their own classes. */
		if (!this.overrideBlockGenInterface && blockState.getBlock() instanceof ISpecialWorldGen)
		{
			((ISpecialWorldGen)blockState.getBlock()).placeGeneration(worldIn, pos, rand, this.blockState);
			return;
		}

		if (seaLevelMin > -1 && pos.getY() >= worldIn.getSeaLevel() - seaLevelMin) return;
		if (!propertyRanges.isEmpty()) applyIntStatePropertiesRange(this.blockState, rand);
		/** Seagrass remains hardcoded due to biomes using different chances of double height, a better method should be devised later. */
		if (blockState.getBlock() == OEBlocks.SEAGRASS && ConfigHandler.block.seagrass.enableTallSeagrass && rand.nextDouble() < tallChance && OEBlocks.SEAGRASS.canPlaceBlockAt(worldIn, pos) && ((BlockSeagrasss) OEBlocks.SEAGRASS).isPositionUnderwater(worldIn, pos.up(), true))
		{
			((BlockSeagrasss) OEBlocks.SEAGRASS).placeAt(worldIn, pos, 2 | 64);
		}
		else
		{ worldIn.setBlockState(pos, this.blockState, 2 | 64); }
	}

	/**
	 * Below are methods specific for this generator!
	 * */

	/** Lets this generator apply this given Integer Property in a range. ONLY use for Integers! */
	public <T extends Comparable<T>> WorldGenOceanPatch setIntStatePropertyRange(IProperty<Integer> property, int min, int max)
	{
		this.propertyRanges.put(property, new int[]{min, max});
		return this;
	}

	/** This runs through the list of State Properties that need to be setup (as added using `setSpecialStateProperties`)*/
	public void applyIntStatePropertiesRange(IBlockState state, Random rand)
	{
		for (Map.Entry<IProperty<Integer>, int[]> entry : propertyRanges.entrySet())
		{
			IProperty<Integer> property = entry.getKey();
			int[] range = entry.getValue();

			if (property.getValueClass() != Integer.class) continue;

			state = state.withProperty(property, rand.nextInt(range[1] - range[0]) + range[0]);
		}

		this.blockState = state;
	}

	/** This is used if we want a feature to only generate below the Sea Level. */
	public WorldGenOceanPatch setSeaLevelMinRequirement(int seaLevelMinIn)
	{ this.seaLevelMin = seaLevelMinIn; return this; }

	/** This is used in case we ever want to override the usage of ISpecialWorldGen. */
	public WorldGenOceanPatch overrideISpecialWorldgen(boolean overrideIn)
	{ this.overrideBlockGenInterface = overrideIn; return this; }
}