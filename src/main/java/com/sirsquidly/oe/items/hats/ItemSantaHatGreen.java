package com.sirsquidly.oe.items.hats;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ItemSantaHatGreen extends ItemSantaHat{
	
	public ItemSantaHatGreen(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return "holidayspecial:textures/models/santa_hat_green.png";
	}
}
