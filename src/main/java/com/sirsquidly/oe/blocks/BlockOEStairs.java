package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOEStairs extends BlockStairs
{
	private int flamability;
	private int fireSpread;
	
	public BlockOEStairs(IBlockState modelState, int flamabilityIn, int fireSpreadIn)
	{
		super(modelState);
		this.flamability = flamabilityIn;
		this.fireSpread = fireSpreadIn;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    { return this.flamability; }

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{ return this.fireSpread; }
}