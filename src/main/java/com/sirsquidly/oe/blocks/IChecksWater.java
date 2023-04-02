package com.sirsquidly.oe.blocks;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
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
	
	/** Checks if surrounding blocks are either solid or are Material.WATER. **/
	default boolean checkWater(World worldIn, BlockPos pos)
    {
    	if (ConfigHandler.block.allowAirAbove || (worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER || worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN)))
    	{
    		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
    			BlockPos blockpos = pos.offset(enumfacing);
            	
            	if (worldIn.getBlockState(blockpos).getMaterial() != Material.WATER && !worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, enumfacing.getOpposite()) && worldIn.getBlockState(blockpos.up()).getMaterial() != Material.WATER && !worldIn.getBlockState(blockpos.up()).isSideSolid(worldIn, blockpos.up(), EnumFacing.DOWN))
            	{ return false; }
            }
    		return true;
    	}
    	return false;
    }
		
	/** Checks if water is touching the side of this block. Skips the check if the block is underwater. **/
	default boolean checkSurfaceWater(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && !ConfigHandler.block.allowAirAbove)
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