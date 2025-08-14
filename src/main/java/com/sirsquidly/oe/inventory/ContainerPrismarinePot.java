package com.sirsquidly.oe.inventory;

import com.sirsquidly.oe.blocks.tileentity.TilePrismarinePot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPrismarinePot extends Container
{
	public TilePrismarinePot tilePrismarinePot;
	
	public ContainerPrismarinePot(EntityPlayer player, TilePrismarinePot pot)
    {
        //this.prismarinePotInventory = dispenserInventoryIn;
		this.tilePrismarinePot = pot;

        //IItemHandler inventory = pot.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        
        this.addSlotToContainer(new Slot(tilePrismarinePot, 0, 71, 18));
        this.addSlotToContainer(new Slot(tilePrismarinePot, 1, 71, 36));
        this.addSlotToContainer(new Slot(tilePrismarinePot, 2, 89, 18));
        this.addSlotToContainer(new Slot(tilePrismarinePot, 3, 89, 36));


        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 68 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 126));
        }
    }
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 4)
            {
                if (!this.mergeItemStack(itemstack1, 4, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 4, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{ return true; }
}