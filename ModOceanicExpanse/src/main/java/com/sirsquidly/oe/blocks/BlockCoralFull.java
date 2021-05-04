package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCoralFull extends Block
{
	public static final PropertyBool IS_DEAD = PropertyBool.create("is_dead");
	
	public BlockCoralFull(MapColor blockMapColor) 
	{
		super(Material.ROCK, blockMapColor);
		this.setDefaultState(this.blockState.getBaseState());		
		this.setTickRandomly(true);
	}
	
    
    public int getMetaFromState(IBlockState state)
    {
    	return 0;
    }
	
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	if (state.getBlock() == OEBlocks.BLUE_CORAL_BLOCK)
    	{ return Item.getItemFromBlock(OEBlocks.BLUE_CORAL_BLOCK_DEAD); }	
    	if (state.getBlock() == OEBlocks.PINK_CORAL_BLOCK)
    	{ return Item.getItemFromBlock(OEBlocks.PINK_CORAL_BLOCK_DEAD); }
    	if (state.getBlock() == OEBlocks.PURPLE_CORAL_BLOCK)
    	{ return Item.getItemFromBlock(OEBlocks.PURPLE_CORAL_BLOCK_DEAD); }
    	if (state.getBlock() == OEBlocks.RED_CORAL_BLOCK)
    	{ return Item.getItemFromBlock(OEBlocks.RED_CORAL_BLOCK_DEAD); }
    	if (state.getBlock() == OEBlocks.YELLOW_CORAL_BLOCK)
    	{ return Item.getItemFromBlock(OEBlocks.YELLOW_CORAL_BLOCK_DEAD); }
    	
    	return null;
    }
    
    protected boolean canSilkHarvest() { return true; }
    /**
     * Handles the Coral Death
     */
    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (!this.checkWater(worldIn, pos, state))
        {
    		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.checkWater(worldIn, pos, state))
        {
        	worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    protected boolean checkWater(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = false;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
        	BlockPos blockpos = pos.offset(enumfacing);
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
    
    /** Should be ~6 Seconds **/
    public int tickRate(World worldIn)
    {
        return 100;
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean flag = this.checkWater(worldIn, pos, state);

        if (!flag)
        {
        	if (state.getBlock() == OEBlocks.BLUE_CORAL_BLOCK)
        	{ worldIn.setBlockState(pos, OEBlocks.BLUE_CORAL_BLOCK_DEAD.getDefaultState(), 2); }
        	if (state.getBlock() == OEBlocks.PINK_CORAL_BLOCK)
        	{ worldIn.setBlockState(pos, OEBlocks.PINK_CORAL_BLOCK_DEAD.getDefaultState(), 2); }
        	if (state.getBlock() == OEBlocks.PURPLE_CORAL_BLOCK)
        	{ worldIn.setBlockState(pos, OEBlocks.PURPLE_CORAL_BLOCK_DEAD.getDefaultState(), 2); }
        	if (state.getBlock() == OEBlocks.RED_CORAL_BLOCK)
        	{ worldIn.setBlockState(pos, OEBlocks.RED_CORAL_BLOCK_DEAD.getDefaultState(), 2); }
        	if (state.getBlock() == OEBlocks.YELLOW_CORAL_BLOCK)
        	{ worldIn.setBlockState(pos, OEBlocks.YELLOW_CORAL_BLOCK_DEAD.getDefaultState(), 2); }
        }
    }
}