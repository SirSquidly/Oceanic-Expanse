package com.sirsquidly.oe.items;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemFoodBase extends ItemFood 
{
	/** Number of ticks to run while 'EnumAction'ing until result. */
	public int useDuration;
	
	public ItemFoodBase(String name, int amount, float saturation, int useDuration, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		this.useDuration = useDuration;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.setCreativeTab(null);
		
		OEItems.ITEMS.add(this);
	}
    
	@Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return this.useDuration;
    }
}