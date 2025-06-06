package com.sirsquidly.oe.world.feature.coral;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CoralGen extends WorldGenerator
{
	public boolean generate(World worldIn, Random rand, BlockPos position) 
	{
		return false;
	}

	public void placeCoralBlockAt(World worldIn, BlockPos pos, IBlockState blockState)
	{ if(pos.getY() < worldIn.getSeaLevel() - 1) this.setBlockAndNotifyAdequately(worldIn, pos, blockState); }
}