package com.sirsquidly.oe.items;

import com.sirsquidly.oe.Main;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemArmorBase extends ItemArmor{

	public ItemArmorBase(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setMaxStackSize(1);
		this.setCreativeTab(Main.OCEANEXPTAB);
	}
}
