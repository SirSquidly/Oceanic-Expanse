package com.sirsquidly.oe.blocks;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.tileentity.TilePickledSkull;

import net.minecraft.block.BlockSkull;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPickledHead extends BlockSkull
{
	public BlockPickledHead()
	{
		setHardness(1.0F);
		setSoundType(SoundType.STONE);
		
		setDefaultState(blockState.getBaseState().withProperty(BlockSkull.NODROP, false).withProperty(BlockSkull.FACING, EnumFacing.UP));
	}
	
	public boolean hasComparatorInputOverride(IBlockState state)
    { return true; }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    { 
    	TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TilePickledSkull)
        { return ((TilePickledSkull) tileentity).currCompOutput; }
        
    	return 0;
    }
    
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{ return new ItemStack(OEBlocks.PICKLED_HEAD); }

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{ return new TilePickledSkull(); }

	@Override
	public boolean canDispenserPlace(World world, BlockPos pos, ItemStack stack)
	{ return false; }

	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune)
	{
        if (!((Boolean)state.getValue(NODROP)).booleanValue())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TilePickledSkull)
            {
                ItemStack itemstack = new ItemStack(OEBlocks.PICKLED_HEAD, 1);

                drops.add(itemstack);
            }
        }
    }
}