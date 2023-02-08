package com.sirsquidly.oe.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOESlabDouble extends BlockOESlab
{
	private Block block;
	
	public BlockOESlabDouble(Block block, Material materialIn, SoundType soundIn, float hardnessIn, float resistenceIn)
    {
        this(block, materialIn, soundIn, hardnessIn, resistenceIn, 0, 0);
    }
	
	public BlockOESlabDouble(Block block, Material materialIn, SoundType soundIn, float hardnessIn, float resistenceIn, int flamabilityIn, int fireSpreadIn)
    {
        super(materialIn, soundIn, hardnessIn, resistenceIn, flamabilityIn, fireSpreadIn);
        this.block = block;
    }
	
	/** Why, yes, this IS a short useless file, but GOD I suffered too much trying to condense this in the normal slab class.*/
	public boolean isDouble()
	{
		return true;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    { return this.block.getFlammability(world, pos, face); }

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{ return this.block.getFireSpreadSpeed(world, pos, face); }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(block);
    }
}