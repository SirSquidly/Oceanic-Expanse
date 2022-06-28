package com.sirsquidly.oe.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockPalmSlabDouble extends BlockPalmSlab
{
	private Block block;
	
	public BlockPalmSlabDouble(Block block)
    {
        super();
        this.block = block;
    }
	
	/** Why, yes, this IS a short useless file, but GOD I suffered too much trying to condense this in the normal slab class.*/
	public boolean isDouble()
	{
		return true;
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(block);
    }
}