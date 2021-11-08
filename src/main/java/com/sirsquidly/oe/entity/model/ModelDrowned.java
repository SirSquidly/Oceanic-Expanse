package com.sirsquidly.oe.entity.model;

import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.init.OEItems;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
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
        super();
    }
	
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHeadwear.render(scale);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }

        GlStateManager.popMatrix();
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
    }
}