package com.sirsquidly.oe.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOEWall extends BlockWall 
{
	public BlockOEWall(Block modelBlock)
	{
		super(modelBlock);
	}

	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    { items.add(new ItemStack(this, 1)); }
	
	@Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    { return true; }
}
