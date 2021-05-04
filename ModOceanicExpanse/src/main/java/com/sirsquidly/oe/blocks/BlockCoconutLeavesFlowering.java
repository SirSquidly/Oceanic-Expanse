package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCoconutLeavesFlowering extends BlockCoconutLeaves implements IGrowable
{
	public BlockCoconutLeavesFlowering() 
	{
		super();
		this.setTickRandomly(true);
		
		setLightOpacity(2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(false)).withProperty(DECAYABLE, Boolean.valueOf(true)));
		this.setCreativeTab(Main.OCEANEXPTAB);
	}
	
	protected ItemStack getSilkTouchDrop(IBlockState state)
    { return new ItemStack(OEBlocks.COCONUT_LEAVES); }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(OEBlocks.COCONUT_LEAVES));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
    	if (!worldIn.isAreaLoaded(pos, 1)) return;
        BlockPos blockpos = pos.up();
        
        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, blockpos, state, rand.nextInt(1) == 0))
        {
        	this.grow(worldIn, rand, pos, state);
        }
    }
    
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) 
	{ return worldIn.isAirBlock(pos.down()); }

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{ return worldIn.isAirBlock(pos.down()); }

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) 
	{
		if(rand.nextInt(10) < 1 && worldIn.isAirBlock(pos.down()))
    	{
			worldIn.setBlockState(pos.down(), OEBlocks.COCONUT.getDefaultState().withProperty(BlockCoconut.AGE, Integer.valueOf(0)).withProperty(BlockCoconut.HANGING, true));
    	}
	}
}