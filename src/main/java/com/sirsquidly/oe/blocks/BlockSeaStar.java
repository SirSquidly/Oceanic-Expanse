package com.sirsquidly.oe.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSeaStar extends BlockBush implements IChecksWater
{
	protected static final AxisAlignedBB SEASTAR_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.0625D, 0.875D);
	
	public BlockSeaStar() {
		super(Material.WATER);
		this.setSoundType(SoundType.PLANT);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(IN_WATER, Boolean.valueOf(true)));
	}

	@Override
	@Deprecated
	public Material getMaterial(IBlockState state)
	{
		if(state.getValue(IN_WATER)) return super.getMaterial(state);
		return Material.SPONGE;
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SEASTAR_AABB;
    }
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (checkSurfaceWater(worldIn, pos, state)) return false;
        if (worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) return true;
        return false;
    }
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		if (this.canBlockStay(worldIn, pos, state)) swapWaterProperty(worldIn, pos, state);
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	if (!checkWater(worldIn, pos)) return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf(false));
    	return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf(true));
    }
    
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    	checkAndDropBlock(worldIn, pos, state);
        super.onBlockAdded(worldIn, pos, state);
    }
    
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{
			if (state.getValue(IN_WATER))
			{
				worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}
			else super.checkAndDropBlock(worldIn, pos, state);
		}
	}
	
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ if (state.getValue(IN_WATER)) worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(IN_WATER, (meta & 4) != 0); }
 
	public int getMetaFromState(IBlockState state)
	{
	    int i = 0;
	    if (((Boolean)state.getValue(IN_WATER)).booleanValue())
        { i |= 4; }
	    
	    return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLiquid.LEVEL, IN_WATER);
	}
}