package com.sirsquidly.oe.blocks;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.items.ItemNautilusArmor;
import com.sirsquidly.oe.tileentity.TileNautilusShellBlock;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNautilusShellBlock extends BlockSkull
{
	private static final AxisAlignedBB[] AABBS = {new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D),
			new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D),
			new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D),
			new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D),
			new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D)};


	public BlockNautilusShellBlock()
	{
		setHardness(1.0F);
		setSoundType(SoundType.STONE);
		
		setDefaultState(blockState.getBaseState().withProperty(BlockSkull.NODROP, false).withProperty(BlockSkull.FACING, EnumFacing.UP));
	}


	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileNautilusShellBlock)
		{

			if (stack.getItem() instanceof ItemNautilusArmor)
			{
				playerIn.swingArm(hand);
				worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.BLOCKS, 1.0F, 1.0F);

				if (!worldIn.isRemote)
				{
					((TileNautilusShellBlock) tile).setArmorStack(stack);
					tile.markDirty();
					worldIn.notifyBlockUpdate(pos, state, state, 3);

					if (!playerIn.isCreative()) stack.shrink(1);
				}

				return true;
			}
			else if (!((TileNautilusShellBlock) tile).getArmorStack().isEmpty())
			{
				ItemStack newStack = ((TileNautilusShellBlock) tile).getArmorStack();

				if (stack.isEmpty())
				{ playerIn.setHeldItem(EnumHand.MAIN_HAND, newStack); }
				else if (!playerIn.inventory.addItemStackToInventory(newStack))
				{ playerIn.dropItem(newStack, false); }

				((TileNautilusShellBlock) tile).setArmorStack(ItemStack.EMPTY);
				tile.markDirty();
				worldIn.notifyBlockUpdate(pos, state, state, 3);
			}
		}

		return false;
	}



	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{ return new ItemStack(OEBlocks.NAUTILUS_SHELL_BLOCK); }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{ return AABBS[Math.max(0, state.getValue(FACING).getIndex() - 1)]; }

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{ return new TileNautilusShellBlock(); }

	@Override
	public boolean canDispenserPlace(World world, BlockPos pos, ItemStack stack)
	{ return false; }

	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune)
	{
        if (!((Boolean)state.getValue(NODROP)).booleanValue())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileNautilusShellBlock)
            {
                ItemStack itemstack = new ItemStack(OEBlocks.PICKLED_HEAD, 1);

                drops.add(itemstack);
            }
        }
    }
}