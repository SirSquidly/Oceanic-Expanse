package com.sirsquidly.oe.blocks;

import com.google.common.collect.ImmutableList;
import com.sirsquidly.oe.Main;
	


import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPalmLog extends BlockLog
{
	public BlockPalmLog()
	{
		super();
		this.setHarvestLevel("axe", 0);
		this.setDefaultState(blockState.getBaseState().withProperty(LOG_AXIS, EnumAxis.Y));
		
		this.setCreativeTab(Main.OCEANEXPTAB);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    { return 20; }

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{ return 5; }
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
            case 0:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 1:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 2:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
        {
            case Y:
                i = 0;
                break;
            case X:
                i = 1;
                break;
            case Z:
                i = 2;
                break;
            default:
			i = 0;
				break;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LOG_AXIS});
    }
    
	public ImmutableList<IBlockState> getProperties()
	{
		return this.blockState.getValidStates();
	}
}