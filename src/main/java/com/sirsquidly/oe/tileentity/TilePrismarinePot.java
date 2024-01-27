package com.sirsquidly.oe.tileentity;

import com.sirsquidly.oe.blocks.BlockPrismarinePot;
import com.sirsquidly.oe.inventory.ContainerPrismarinePot;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TilePrismarinePot extends TileEntityLockableLoot
{
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	
	@Override
	public int getSizeInventory()
	{ return 4; }

	@Override
	public boolean isEmpty()
	{
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

	@Override
	public int getInventoryStackLimit()
	{ return 64; }

	@Override
	public String getName()
	{ return this.hasCustomName() ? this.customName : "container.oe.prismarine_pot"; }

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
        this.fillWithLoot(playerIn);
        return new ContainerPrismarinePot(playerIn, this);
    }
	
	/**
	 *  This keeps the tile entity around even if the block gets its state changed. 
	 * 
	 *  The state is changed for the block using `setBlockState` when plugged or taken in/out of water.
	 * */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{ return oldState.getBlock() != newState.getBlock(); }
	
	@Override
	public String getGuiID()
	{ return "oe:prismarine_pot"; }

	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.stacks);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.stacks);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }
    
	@Override
	protected NonNullList<ItemStack> getItems()
	{ return this.stacks; }
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			this.markDirty();
			if(world != null && world.getBlockState(pos).getBlock() == getBlockType())
			{
				if (!world.getBlockState(pos).getValue(BlockPrismarinePot.SEALED))
				{
					return super.getCapability(capability, facing);
				}
			}
		}
		return null;
	}
}