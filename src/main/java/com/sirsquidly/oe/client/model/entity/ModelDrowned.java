package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHeadwear.render(scale);
        }

        GlStateManager.popMatrix();
    }
	
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		EntityDrowned drowned = (EntityDrowned) entityIn;
		boolean flag = entityIn instanceof EntityZombie && ((EntityLivingBase) entityIn).isHandActive();
		
		// Used so a drowned lowering its arms will still properly use the Trident
		boolean usingRTrident = false;
		boolean usingLTrident = false;
		
		Item mainItem = drowned.getHeldItem(EnumHand.MAIN_HAND).getItem();
		
		if (mainItem == OEItems.TRIDENT_ORIG && flag)
		{
			float hand = Math.min((float)drowned.getItemInUseMaxCount() / 35.0F, 1.0F);
			
			if (drowned.getPrimaryHand() == EnumHandSide.RIGHT)
            {
        		this.bipedRightArm.rotateAngleX = -3 - hand;
        		usingRTrident = true;
        		
            }
            if (drowned.getPrimaryHand() == EnumHandSide.LEFT)
            {
            	this.bipedLeftArm.rotateAngleX = -3 -hand;
            	usingLTrident = true;
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
		
		float swimMath = drowned.getClientSwimTime(ageInTicks - (float)drowned.ticksExisted);
		if (drowned.isInWater() && swimMath != 0)
		{
			this.bipedHead.rotateAngleX -= 1.25F * swimMath;
			this.bipedHeadwear.rotateAngleX -= 1.25F * swimMath;
			
			this.bipedRightArm.rotateAngleY = 0.3F * swimMath;
			this.bipedLeftArm.rotateAngleY = -0.3F * swimMath;
			
			this.bipedRightArm.rotateAngleZ = -0.2F * swimMath;
			this.bipedLeftArm.rotateAngleZ = 0.2F * swimMath;
		}
		
		
		
		
		
		
		if (!drowned.getHeldItem(EnumHand.OFF_HAND).isEmpty() && ConfigHandler.entity.drowned.enableDrownedLowerArms && this.swingProgress <= 0.0F)
        {
			float f = 1.0F;
			
			if (!usingRTrident)
			{
				this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
				this.bipedRightArm.rotateAngleZ = 0.0F;
			}
			if (!usingLTrident)
			{
				this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		        this.bipedLeftArm.rotateAngleZ = 0.0F;
			}
        }
    }
}