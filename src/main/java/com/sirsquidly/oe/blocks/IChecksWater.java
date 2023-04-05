package com.sirsquidly.oe.blocks;

import javax.annotation.Nullable;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChecksWater 
{
	public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
	
	default void swapWaterProperty(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!checkWater(worldIn, pos) && state.getValue(IN_WATER))
		{
			worldIn.setBlockState(pos, state.withProperty(IN_WATER, false));
		}
		else if (checkWater(worldIn, pos) && !state.getValue(IN_WATER))
		{
			worldIn.setBlockState(pos, state.withProperty(IN_WATER, true));
		}
	}

	/** Checks if the exact block is water. Uses 'checkWater', or just directly checks the block if 'disableBlockWaterLogic'. 
	 * 	boolean 'onlyTrueWater' makes this return Water blocks, else it will return for any Material.WATER block
	 * */
	default boolean checkPlaceWater(World worldIn, BlockPos pos, @Nullable boolean onlyTrueWater)
    { 
		if (onlyTrueWater)
		{
			boolean waterBlocks = worldIn.getBlockState(pos).getBlock() == Blocks.WATER || worldIn.getBlockState(pos).getBlock() == Blocks.FLOWING_WATER;
			return waterBlocks && (ConfigHandler.block.disableBlockWaterLogic || checkWater(worldIn, pos) && !ConfigHandler.block.disableBlockWaterLogic);
		}
		return ConfigHandler.block.disableBlockWaterLogic ? worldIn.getBlockState(pos).getMaterial() == Material.WATER: checkWater(worldIn, pos);
	}
	
	/** Checks if surrounding blocks are either solid or are Material.WATER. **/
	default boolean checkWater(World worldIn, BlockPos pos)
    {
		/** First we check above this for Water, a Solid, or skip if config. **/
    	if ((ConfigHandler.block.disableBlockWaterLogic || worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER || worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN)))
    	{
    		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
    			BlockPos blockpos = pos.offset(enumfacing);
    			
    			/** Checking each direction if a block should make this fail by either not being solid or water **/
            	if (worldIn.getBlockState(blockpos).getMaterial() != Material.WATER && !worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, enumfacing.getOpposite()))
            	{ 
            		if (worldIn.getBlockState(blockpos.up()).getMaterial() != Material.WATER && !worldIn.getBlockState(blockpos.up()).isSideSolid(worldIn, blockpos.up(), EnumFacing.DOWN))
            		{
            			return false;
            		}
            	}
            }
    		return true;
    	}
    	return false;
    }
		
	/** Checks if water is touching the side of this block. Skips the check if the block is underwater. **/
	default boolean checkSurfaceWater(World worldIn, BlockPos pos, IBlockState state)
    {
		if (ConfigHandler.block.disableBlockWaterLogic) return false;
		
    	if (worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR)
    	{
    		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) 
            {
    			BlockPos blockpos = pos.offset(enumfacing);
            	
            	if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
            	{ return true; }
            }
    	}
    	return false;
    }
}