package com.sirsquidly.oe.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * This exists for any blocks that require specialized rules for placement (such as double-height like Dulse)
 * */
public interface ISpecialWorldGen
{
	public default void placeGeneration(World worldIn, BlockPos pos, Random rand, IBlockState state)
	{}
}