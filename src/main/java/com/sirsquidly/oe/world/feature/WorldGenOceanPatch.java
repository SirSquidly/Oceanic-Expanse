package com.sirsquidly.oe.world.feature;

import java.util.Random;

import com.sirsquidly.oe.blocks.BlockCoral;
import com.sirsquidly.oe.blocks.BlockCoralFan;
import com.sirsquidly.oe.blocks.BlockCoralFull;
import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.blocks.BlockDulse;
import com.sirsquidly.oe.blocks.BlockPrismarinePot;
import com.sirsquidly.oe.blocks.BlockSeaPickle;
import com.sirsquidly.oe.blocks.BlockTubeSponge;
import com.sirsquidly.oe.blocks.IChecksWater;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.tileentity.TilePrismarinePot;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
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
	private final Block block;
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

	/** Simplified because not every use needs to specify the spread or seagrass height chance.*/
	public WorldGenOceanPatch(Block blockIn, int perChunk, int perAttempt, int amount, boolean rising, Biome... biomes)
	{
		this(blockIn, perChunk, perAttempt, amount, 8, 4, 0.0, rising, biomes);
	}
	
	public WorldGenOceanPatch(Block blockIn, int perChunk, int perAttempt, int amount, int spreadXZ, int spreadY, double tall, boolean rising, Biome... biomes)
	{
		this.block = blockIn;
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
							
							if(this.block.canPlaceBlockAt(world, pos.down()))
							{ beginPatchPlacing(world, rand, pos); break;}
						}
					}
					else
					{ 
						for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        	{ pos = pos.down(); }
						
						if(this.block.canPlaceBlockAt(world, pos.up()))
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
            
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && this.block.canPlaceBlockAt(worldIn, blockpos))
            {
            	placeBlock(worldIn, rand, blockpos);
            }
        }
        return true;
    }
    
    /** Used for placing the individual blocks because many blocks require specific checks/randomizing. **/
    public void placeBlock(World worldIn, Random rand, BlockPos pos)
    {
    	if (this.block == OEBlocks.SEAGRASS && ConfigHandler.block.seagrass.enableTallSeagrass && rand.nextDouble() < tallChance && OEBlocks.TALL_SEAGRASS.canPlaceBlockAt(worldIn, pos))
    	{
    		((BlockDoubleUnderwater) OEBlocks.TALL_SEAGRASS).placeAt(worldIn, pos, 16 | 2);
    	}
    	else if (this.block == OEBlocks.SEA_PICKLE)
    	{
    		if (pos.getY() < worldIn.getSeaLevel() - 1) worldIn.setBlockState(pos, this.block.getDefaultState().withProperty(BlockSeaPickle.AMOUNT, rand.nextInt(4) + 1), 2);
    	}
    	else if (this.block == OEBlocks.DULSE)
    	{
    		if (pos.getY() < worldIn.getSeaLevel() - 1)
    		{
    			int dulseAge = rand.nextInt(4);
    			
    			if (dulseAge >= 3 && ((IChecksWater) OEBlocks.DULSE).checkWater(worldIn, pos.up()))
    			{
    				((BlockDulse) OEBlocks.DULSE).placeAt(worldIn, pos, 16 | 2);
    			}
    			if (dulseAge < 3)
    			{
    				worldIn.setBlockState(pos, this.block.getDefaultState().withProperty(BlockDulse.AGE, dulseAge), 2);
    			}
    		}
    	}
    	else if (this.block == OEBlocks.TUBE_SPONGE)
    	{
    		if (pos.getY() < worldIn.getSeaLevel() - 10) worldIn.setBlockState(pos, this.block.getDefaultState().withProperty(BlockTubeSponge.AGE, rand.nextInt(3)), 2);
    	}
    	else if (this.block instanceof BlockCoralFan)
    	{
    		if (pos.getY() < worldIn.getSeaLevel() - 10) ((BlockCoralFan) OEBlocks.BLUE_CORAL_FAN).placeAt(worldIn, pos, rand, this.block, true);
    	}
    	else if (this.block instanceof BlockCoral)
    	{
    		if (pos.getY() < worldIn.getSeaLevel() - 10 && worldIn.getBlockState(pos.down()).getBlock() instanceof BlockCoralFull) 
    		{ worldIn.setBlockState(pos, this.block.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2); }
    	}
    	else if (this.block == OEBlocks.PRISMARINE_POT)
    	{
    		if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPrismarine)
    		{
    			BlockPrismarinePot.EnumAxis randPotRotation = rand.nextInt(2) == 0 ? BlockPrismarinePot.EnumAxis.X : BlockPrismarinePot.EnumAxis.Z;
        		
        		worldIn.setBlockState(pos, this.block.getDefaultState().withProperty(BlockPrismarinePot.FACING, randPotRotation).withProperty(BlockPrismarinePot.SEALED, rand.nextInt(10) == 0), 3);
        		
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TilePrismarinePot)
                {
                    ((TilePrismarinePot)tileentity).setLootTable(LootTableHandler.MONUMENT_MYSTIC, rand.nextLong());
                }
    		}
    	}
    	else
    	{ worldIn.setBlockState(pos, this.block.getDefaultState(), 16 | 2); }
    }
}