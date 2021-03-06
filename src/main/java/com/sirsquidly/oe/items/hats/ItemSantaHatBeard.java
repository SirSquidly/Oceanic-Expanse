package com.sirsquidly.oe.items.hats;

import com.sirsquidly.oe.models.ModelSantaHatBearded;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemSantaHatBeard extends ItemSantaHat{


	public ItemSantaHatBeard(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    {
		if(itemStack != ItemStack.EMPTY)
		{
			if(itemStack.getItem() instanceof ItemArmor)
			{
				ModelSantaHatBearded model = new ModelSantaHatBearded();
				
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
}
