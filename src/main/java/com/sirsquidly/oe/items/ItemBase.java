package com.sirsquidly.oe.items;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.item.Item;

public class ItemBase extends Item {
	
	public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		
		OEItems.ITEMS.add(this);
	}
}