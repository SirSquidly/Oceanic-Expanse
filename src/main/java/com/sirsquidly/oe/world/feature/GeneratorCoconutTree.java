package com.sirsquidly.oe.world.feature;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

/**
 * This builds the Coconut Tree
 */
public class GeneratorCoconutTree extends WorldGenAbstractTree
{
	public static final IBlockState LEAF = OEBlocks.PALM_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
	public static final IBlockState LEAF_FLOWERING = OEBlocks.PALM_LEAVES_FLOWERING.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
	
	/** The minimum height of the trunk*/
	public int minHeight = 5;
	/** A random number between this and zero get added to the minimum height*/
	public int plusRanHeight = 2;
	/** The max amount of block the trunk can bend out, like Acacia tree*/
	public int plusRanBend = 2;
	
	public GeneratorCoconutTree() 
	{
		super(false);
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        int i = rand.nextInt(plusRanHeight + 1) + minHeight;

        boolean flag = true;

        if (pos.getY() >= 1 && pos.getY() + i + 1 <= 256)
        {
            for (int j = pos.getY(); j <= pos.getY() + 1 + i; ++j)
            {
                int k = 1;

                if (j == pos.getY())
                { k = 0; }

                if (j >= pos.getY() + 1 + i - 2)
                { k = 2; }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l)
                {
                    for (int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < worldIn.getHeight())
                        {
                            if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                BlockPos down = pos.down();
                IBlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, EnumFacing.UP, (IPlantable)OEBlocks.PALM_SAPLING);

                
                if (isSoil && pos.getY() < worldIn.getHeight() - i - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, down, pos);
                	
                    EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                    int k2 = i/2 - rand.nextInt(2);
                    int l2 = (plusRanBend) - rand.nextInt(plusRanBend + 1);
                    int i3 = pos.getX();
                    int j1 = pos.getZ();
                    int k1 = 0;

                    for (int l1 = 0; l1 < i; ++l1)
                    {
                    	List<EnumFacing> list = Lists.newArrayList(EnumFacing.Plane.HORIZONTAL);
                        Collections.shuffle(list, rand);
                        
                        int stump = rand.nextInt(4);
                        int i2 = pos.getY() + l1;

                        if (l1 >= k2 && l2 > 0)
                        {
                            i3 += enumfacing.getXOffset();
                            j1 += enumfacing.getZOffset();
                            --l2;
                        }
                        
                        BlockPos blockpos = new BlockPos(i3, i2, j1);
                        
                        state = worldIn.getBlockState(blockpos);

                        if (state.getBlock().isAir(state, worldIn, blockpos) || state.getBlock().isLeaves(state, worldIn, blockpos))
                        {
                            this.placeLogAt(worldIn, blockpos);
                            
                            //** Used for generating an area around the 'stump' of the tree.*/
                            if (l1 == 0)
                            {
                            	for (EnumFacing facing : list.subList(0, stump))
                                {
                            		state = worldIn.getBlockState(blockpos.offset(facing));
                            		if (state.getBlock().isAir(state, worldIn, blockpos.offset(facing)) || state.getBlock().isLeaves(state, worldIn, blockpos.offset(facing))) this.placeStumpAt(worldIn, blockpos.offset(facing));
                                }
                            }
                            
                            k1 = i2;
                        }
                    }
                    
                    BlockPos blockpos2 = new BlockPos(i3, k1, j1);
                    
                    this.placeCrownLeaves(worldIn, blockpos2.up());
                	
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
	
	/** Used for placing the 'stump' around the base of the tree, basocally just runs checks and pushes along to placeLogAt.*/
	private void placeStumpAt(World worldIn, BlockPos pos)
    { 
		if (!worldIn.getBlockState(pos.down()).isFullBlock())
		{
			if (worldIn.getBlockState(pos.down(2)).isFullBlock())
			{
				placeLogAt(worldIn, pos.down());
				placeLogAt(worldIn, pos);
			}
		}
		else placeLogAt(worldIn, pos);
	}
	
	private void placeLogAt(World worldIn, BlockPos pos)
    { 
		EnumAxis getAxis = ConfigHandler.worldGen.palmTree.palmTreeFullBark ? BlockLog.EnumAxis.NONE : BlockLog.EnumAxis.Y;
		if (ConfigHandler.block.palmBlocks.enablePalmWoods)
		{ this.setBlockAndNotifyAdequately(worldIn, pos, OEBlocks.PALM_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, getAxis)); }
		else
		{ this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, getAxis).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE)); }
	}
	
	private void placeCrownLeaves(World worldIn, BlockPos pos)
    {
		//this.placeLeafAt(worldIn, pos);
		this.placeLeafAt(worldIn, pos);
		
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
        {
			this.placeFloweringLeafAt(worldIn, pos.offset(facing));
			this.placeLeafAt(worldIn, pos.offset(facing, 2));
			this.placeLeafAt(worldIn, pos.offset(facing, 2).down());
			this.placeLeafAt(worldIn, pos.offset(facing, 3).down());
			this.placeLeafAt(worldIn, pos.offset(facing, 3).down(2));
			
			this.placeLeafAt(worldIn, pos.offset(facing).offset(facing.rotateY()));
			this.placeLeafAt(worldIn, pos.offset(facing).offset(facing.rotateY()).down());
			this.placeLeafAt(worldIn, pos.offset(facing, 2).offset(facing.rotateY(), 2).down());
			this.placeLeafAt(worldIn, pos.offset(facing, 2).offset(facing.rotateY(), 2).down(2));
        }
    }
	
	private void placeLeafAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (state.getBlock().isAir(state, worldIn, pos) || state.getBlock().isLeaves(state, worldIn, pos))
        { this.setBlockAndNotifyAdequately(worldIn, pos, LEAF); }
    }
	
	private void placeFloweringLeafAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        /* Only a 75% chance to place a Flowering Leaf at the spot, so some Coconut Palms have more or less than others */
        IBlockState GET_LEAF = worldIn.rand.nextDouble() < 0.75D ? LEAF_FLOWERING : LEAF;

        if (state.getBlock().isAir(state, worldIn, pos) || state.getBlock().isLeaves(state, worldIn, pos))
        { this.setBlockAndNotifyAdequately(worldIn, pos, GET_LEAF); }
    }
	
	@Override
	protected boolean canGrowInto(Block blockType)
	{
		Material material = blockType.getDefaultState().getMaterial();
        return material == Material.AIR || material == Material.LEAVES || material == Material.GROUND || blockType == Blocks.GRASS || blockType == Blocks.DIRT || blockType == Blocks.LOG || blockType == Blocks.LOG2 || blockType == Blocks.SAPLING || blockType == Blocks.VINE;
	}
}
