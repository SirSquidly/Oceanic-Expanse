package com.sirsquidly.oe.blocks;

import javax.annotation.Nonnull;

import com.sirsquidly.oe.tileentity.TileConduit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConduit extends Block implements ITileEntityProvider, IChecksWater
{
	protected static final AxisAlignedBB CONDUIT_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
	public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
	
	public BlockConduit()
	{
		super(Material.WATER);
		this.setLightLevel(1.0F);
		this.hasTileEntity = true;
		this.setSoundType(SoundType.STONE);
	}

	@Override
	@Deprecated
	public Material getMaterial(IBlockState state)
	{
		if(state.getValue(IN_WATER)) return super.getMaterial(state);
		return Material.GROUND;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    { 
		doWaterCheck(worldIn, pos, state); 
		
		TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileConduit)
        {
            ((TileConduit)tileentity).update();
            worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }
	
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	if (!checkWater(worldIn, pos)) return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf(false));
    	return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf(true));
    }
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		this.dropBlockAsItem(worldIn, pos, state, 0);
		if (state.getValue(IN_WATER)) worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
	}
	
	public void doWaterCheck(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!checkWater(worldIn, pos) && state.getValue(IN_WATER))
		{
			worldIn.setBlockState(pos, state.withProperty(IN_WATER, false));
		}
		else if (checkWater(worldIn, pos) && !state.getValue(IN_WATER))
		{
			worldIn.setBlockState(pos, state.withProperty(IN_WATER, true));
		}
	
	}
	
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ //if (state.getValue(IN_WATER)) worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); 
	}
	
	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
	{
		return new TileConduit();
	}
	
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    { return CONDUIT_AABB; }
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return CONDUIT_AABB; }
	
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    public boolean isFullCube(IBlockState state)
    { return false; }

	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
	
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
	
	@SuppressWarnings("deprecation")
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
}