package com.sirsquidly.oe.items.hats;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.sirsquidly.oe.items.ItemArmorBase;
import com.sirsquidly.oe.models.ModelSantaHat;

public class ItemSantaHat extends ItemArmorBase{
	
	public ItemSantaHat(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    {
		if(itemStack != ItemStack.EMPTY)
		{
			if(itemStack.getItem() instanceof ItemArmor)
			{
				ModelSantaHat model = new ModelSantaHat();
				
				model.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
			    model.isChild = _default.isChild;
			    model.isRiding = _default.isRiding;
			    model.isSneak = _default.isSneak;
			    model.rightArmPose = _default.rightArmPose;
			    model.leftArmPose = _default.leftArmPose;
			     
			    return model;
			}
		}
		return null;
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return "holidayspecial:textures/models/santa_hat_red.png";
	}
}
