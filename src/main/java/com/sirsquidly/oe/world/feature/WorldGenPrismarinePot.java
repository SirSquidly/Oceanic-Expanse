package com.sirsquidly.oe.world.feature;

import java.util.Random;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.sirsquidly.oe.blocks.BlockPrismarinePot;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.tileentity.TilePrismarinePot;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

/**
 * Generates patches of Prismarine Pots.
 */
public class WorldGenPrismarinePot implements IWorldGenerator
{
	private int patchAmount;
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
	public WorldGenPrismarinePot(int perChunk, int perAttempt, int amount, boolean rising, Biome... biomes)
	{
		this(perChunk, perAttempt, amount, 8, 4, rising, biomes);
	}
	
	public WorldGenPrismarinePot(int perChunk, int perAttempt, int amount, int spreadXZ, int spreadY, boolean rising, Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.patchAmount = amount;
		this.placeSpreadXZ = spreadXZ;
		this.placeSpreadY = spreadY;
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
							
							if(world.getBlockState(pos.down()).getBlock() == Blocks.WATER && world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP))
							{ beginPatchPlacing(world, rand, pos); break;}
						}
					}
					else
					{ 
						for ( IBlockState state = world.getBlockState(pos); (state.getBlock().isReplaceable(world, pos) && pos.getY() > 0); state = world.getBlockState(pos) )
			        	{ pos = pos.down(); }
						
						if(world.getBlockState(pos.up()).getBlock() == Blocks.WATER && world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP))
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
            
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP) && !worldIn.canBlockSeeSky(blockpos))
            {
            	placeBlock(worldIn, rand, blockpos);
            }
        }
        return true;
    }
    
    /** Used for placing the individual blocks because many blocks require specific checks/randomizing. **/
    public void placeBlock(World worldIn, Random rand, BlockPos pos)
    {
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPrismarine)
		{
			BlockPrismarinePot.EnumAxis randPotRotation = rand.nextInt(2) == 0 ? BlockPrismarinePot.EnumAxis.X : BlockPrismarinePot.EnumAxis.Z;
    		
    		worldIn.setBlockState(pos, OEBlocks.PRISMARINE_POT.getDefaultState().withProperty(BlockPrismarinePot.FACING, randPotRotation).withProperty(BlockPrismarinePot.SEALED, rand.nextInt(2) == 0), 3);
    		
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TilePrismarinePot)
            {
                ((TilePrismarinePot)tileentity).setLootTable(LootTableHandler.MONUMENT_MYSTIC, rand.nextLong());
            }
		}
	}
}