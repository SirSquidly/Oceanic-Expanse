package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDrowned extends ModelZombie
{
	public ModelDrowned()
    {
        this(0.0F, false);
    }

    public ModelDrowned(float modelSize, boolean p_i1168_2_)
    {
        super(modelSize, p_i1168_2_);
    }
	
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		EntityDrowned drowned = (EntityDrowned) entityIn;
		boolean flag = entityIn instanceof EntityZombie && ((EntityZombie)entityIn).isArmsRaised();
		
		Item mainItem = drowned.getHeldItem(EnumHand.MAIN_HAND).getItem();
		
		if (mainItem == OEItems.TRIDENT_ORIG && flag)
		{
			if (drowned.getPrimaryHand() == EnumHandSide.RIGHT)
            {
        		this.bipedRightArm.rotateAngleX = -4.0F;
            }
            if (drowned.getPrimaryHand() == EnumHandSide.LEFT)
            {
            	this.bipedLeftArm.rotateAngleX = 4.0F;
            }
		}
		
		if (drowned.isInWater() && ConfigHandler.entity.drowned.enableDrownedSwimAnims)
        {
			float swayX = MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
			
			this.bipedBody.rotateAngleX = 0.15F;
			this.bipedRightLeg.rotateAngleX += 0.15F + swayX;
			this.bipedLeftLeg.rotateAngleX += 0.15F - swayX;
			
			this.bipedRightLeg.rotationPointZ += 1.8F;
	        this.bipedLeftLeg.rotationPointZ += 1.8F;
	        this.bipedRightLeg.rotationPointY -= 0.15F;
	        this.bipedLeftLeg.rotationPointY -= 0.15F;
        }
    }
}