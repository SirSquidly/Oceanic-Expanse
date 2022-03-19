package com.sirsquidly.oe.items;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEItems;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemArmorBase extends ItemArmor{

	public ItemArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(1);
		this.setCreativeTab(Main.OCEANEXPTAB);
		
		OEItems.ITEMS.add(this);
	}
}
