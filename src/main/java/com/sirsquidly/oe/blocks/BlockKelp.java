package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockKelp extends BlockBush implements IGrowable
{
	protected static final AxisAlignedBB KELP_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	
	public BlockKelp() {
		super(Material.WATER);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(null);

		setDefaultState(blockState.getBaseState());
	}

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		return KELP_AABB;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (!this.trySwapKelp(worldIn, pos, state))
        {
            super.onBlockAdded(worldIn, pos, state);
        }
    }
    
    @Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	if (!this.trySwapKelp(worldIn, pos, state))
        {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        }
	}

    protected boolean trySwapKelp(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = false;
        
        if (!(worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER) && !(worldIn.getBlockState(pos.up(2)).getMaterial() == Material.WATER))
        {
        	OEBlocks.KELP_TOP.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
			worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
			if (worldIn instanceof WorldServer)
            {
                ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.WATER_BUBBLE, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.25D, (double)pos.getZ() + 0.5D, 8, 0.25D, 0.5D, 0.25D, 0.0D);
            }
        }
        if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER)
		{ flag = true; }
        if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.FLOWING_WATER)
		{ flag = true; }

        if (flag)
        {
        	Random rand = worldIn.rand;
        	worldIn.setBlockState(pos, OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(rand.nextInt(10))));
        }
        return flag;
    }
    
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || 
        		worldIn.getBlockState(pos.down()).getBlock() == this && worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER;
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if ((worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) || 
        	(worldIn.getBlockState(pos.down()).getBlock() == this) ||
        	(worldIn.getBlockState(pos.down()).getBlock() == OEBlocks.KELP_TOP)) return true;
        return false;
    }
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{
			OEBlocks.KELP_TOP.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) 
	{ return new ItemStack(OEBlocks.KELP_TOP); }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return Item.getItemFromBlock(OEBlocks.KELP_TOP); }
	
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
    
	@Override
	public int getMetaFromState(IBlockState state) 
	{ return 0; }
	
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockLiquid.LEVEL);
    }

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{ 
		BlockPos next = pos.up();
		while (worldIn.getBlockState(next).getBlock() == this)
		{
			next = next.up();
		}
		
		if(worldIn.getBlockState(next).getBlock() == OEBlocks.KELP_TOP)
    	{ 
			if (worldIn.getBlockState(next).getValue(BlockTopKelp.AGE).intValue() != BlockTopKelp.maxHeight && OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, next.up()))
			{
				return true;
			}
			else { return false ; }
    	}
		else { return false; }
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return this.canGrow(worldIn, pos, state, enableStats);
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		BlockPos next = pos.up();
		
		while (worldIn.getBlockState(next).getBlock() == this)
		{
			next = next.up();
		}
		
		if(worldIn.getBlockState(next).getBlock() == OEBlocks.KELP_TOP)
    	{ 
			int i = ((Integer)worldIn.getBlockState(next).getValue(BlockTopKelp.AGE)).intValue();
			
    		if(OEBlocks.KELP_TOP.canPlaceBlockAt(worldIn, next.up()) && i != BlockTopKelp.maxHeight)
        	{ 
    			worldIn.setBlockState(next.up(), OEBlocks.KELP_TOP.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(i + 1)), 16 | 2);
    			worldIn.setBlockState(next, OEBlocks.KELP.getDefaultState(), 16 | 2);
        	}
    	}
	}
}
