package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityPickled;
import com.sirsquidly.oe.init.OEItems;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ModelPickled - Sir Squidly
 * Created using Tabula 7.1.0
 */
@SideOnly(Side.CLIENT)
public class ModelPickled extends ModelZombie {;
    public ModelRenderer rightArmIntest1;
    public ModelRenderer rightArmIntest2;
    public ModelRenderer headIntest1;
    public ModelRenderer headIntest2;
    public ModelRenderer leftArmIntest1;
    public ModelRenderer leftArmIntest2;

    public ModelPickled() {
    	super();
    	this.textureWidth = 64;
        this.textureHeight = 64;
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(bipedRightArm, -1.4191272147965894F, -0.10000736613927509F, 0.0029670597283903604F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.rightArmIntest1 = new ModelRenderer(this, 16, 28);
        this.rightArmIntest1.setRotationPoint(-1.0F, -2.0F, 0.0F);
        this.rightArmIntest1.addBox(0.0F, -4.0F, -2.0F, 0, 4, 4, 0.0F);
        this.headIntest1 = new ModelRenderer(this, 0, 32);
        this.headIntest1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.headIntest1.addBox(-4.0F, -6.0F, 0.0F, 8, 6, 0, 0.0F);
        this.setRotateAngle(headIntest1, 0.0F, -0.7853981633974483F, 0.0F);
        this.leftArmIntest2 = new ModelRenderer(this, 16, 36);
        this.leftArmIntest2.setRotationPoint(1.0F, -2.0F, 0.0F);
        this.leftArmIntest2.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 0, 0.0F);
        this.rightArmIntest2 = new ModelRenderer(this, 16, 32);
        this.rightArmIntest2.setRotationPoint(-1.0F, -2.0F, 0.0F);
        this.rightArmIntest2.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 0, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.headIntest2 = new ModelRenderer(this, 0, 32);
        this.headIntest2.mirror = true;
        this.headIntest2.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.headIntest2.addBox(-4.0F, -6.0F, 0.0F, 8, 6, 0, 0.0F);
        this.setRotateAngle(headIntest2, 0.0F, -2.356194490192345F, 0.0F);
        this.leftArmIntest1 = new ModelRenderer(this, 16, 32);
        this.leftArmIntest1.setRotationPoint(1.0F, -2.0F, 0.0F);
        this.leftArmIntest1.addBox(0.0F, -4.0F, -2.0F, 0, 4, 4, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(bipedLeftArm, -1.3733995883943377F, 0.10000736613927509F, -0.0029670597283903604F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.bipedRightArm.addChild(this.rightArmIntest1);
        this.bipedHead.addChild(this.headIntest1);
        this.bipedLeftArm.addChild(this.leftArmIntest2);
        this.bipedRightArm.addChild(this.rightArmIntest2);
        this.bipedHead.addChild(this.headIntest2);
        this.bipedLeftArm.addChild(this.leftArmIntest1);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		EntityPickled pickled = (EntityPickled) entityIn;
		boolean flag = entityIn instanceof EntityZombie && ((EntityZombie)entityIn).isArmsRaised();
		
		Item mainItem = pickled.getHeldItem(EnumHand.MAIN_HAND).getItem();
		
		if (mainItem == OEItems.TRIDENT_ORIG && flag)
		{
			if (pickled.getPrimaryHand() == EnumHandSide.RIGHT)
            {
        		this.bipedRightArm.rotateAngleX = -4.0F;
            }
            if (pickled.getPrimaryHand() == EnumHandSide.LEFT)
            {
            	this.bipedLeftArm.rotateAngleX = 4.0F;
            }
		}
    }
    
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}