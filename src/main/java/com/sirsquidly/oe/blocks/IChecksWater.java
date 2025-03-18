package com.sirsquidly.oe.blocks;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
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
		if (!checkSurroundingUnderwaterPosition(worldIn, pos) && state.getValue(IN_WATER))
		{
			worldIn.setBlockState(pos, state.withProperty(IN_WATER, false));
		}
		else if (checkSurroundingUnderwaterPosition(worldIn, pos) && !state.getValue(IN_WATER))
		{
			worldIn.setBlockState(pos, state.withProperty(IN_WATER, true));
		}
	}

	default boolean isPositionUnderwater(World worldIn, BlockPos pos)
	{ return this.isPositionUnderwater(worldIn, pos, false); }

	/**
	 * If the given position is `underwater`. If Fluidlogged API is installed, it just checks itself. Otherwise it will check all neighboring blocks
	 * 	boolean 'onlyTrueWater' makes this return Water blocks, else it will return for any Material.WATER block
	 * */
	default boolean isPositionUnderwater(World worldIn, BlockPos pos, @Nullable boolean onlyTrueWater)
    { 
		if (onlyTrueWater)
		{
			boolean waterBlocks = worldIn.getBlockState(pos).getBlock() == Blocks.WATER || worldIn.getBlockState(pos).getBlock() == Blocks.FLOWING_WATER;
			return waterBlocks && (Main.proxy.fluidlogged_enable || checkSurroundingUnderwaterPosition(worldIn, pos) && !Main.proxy.fluidlogged_enable);
		}
		return Main.proxy.fluidlogged_enable ? isWaterHere(worldIn, pos): checkSurroundingUnderwaterPosition(worldIn, pos);
	}
	
	/** Checks if any of the surrounding blocks are NOT an acceptable neighbor. **/
	default boolean checkSurroundingUnderwaterPosition(World worldIn, BlockPos pos)
    {
		for (EnumFacing enumfacing : EnumFacing.VALUES)
		{
			if (!isAcceptableNeighbor(worldIn, pos.offset(enumfacing), enumfacing)) return false;
		}
		return true;
    }

	/** Checks if the given block is an acceptable neighbor for an `underwater-type` block. */
	default boolean isAcceptableNeighbor(World worldIn, BlockPos pos, EnumFacing facing)
	{ return isWaterHere(worldIn, pos) || worldIn.getBlockState(pos).isSideSolid(worldIn, pos, facing); }

	/** If the given block is Water, or Fluidlogged. */
	default boolean isWaterHere(World worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos).getMaterial() == Material.WATER) return true;
		if (Main.proxy.fluidlogged_enable)
		{
			final FluidState fluidState = FluidloggedUtils.getFluidState(worldIn, pos);
			return !fluidState.isEmpty() && fluidState.getMaterial() == Material.WATER;
		}

		return false;
	}
}