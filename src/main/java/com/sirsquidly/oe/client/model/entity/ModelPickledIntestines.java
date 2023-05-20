package com.sirsquidly.oe.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPickledIntestines extends ModelDrowned {;
    public ModelRenderer rightArmIntest1;
    public ModelRenderer rightArmIntest2;
    public ModelRenderer headIntest1;
    public ModelRenderer headIntest2;
    public ModelRenderer leftArmIntest1;
    public ModelRenderer leftArmIntest2;

    public ModelPickledIntestines() {
    	super();
    	this.textureWidth = 64;
        this.textureHeight = 64;
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
        this.headIntest2 = new ModelRenderer(this, 0, 32);
        this.headIntest2.mirror = true;
        this.headIntest2.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.headIntest2.addBox(-4.0F, -6.0F, 0.0F, 8, 6, 0, 0.0F);
        this.setRotateAngle(headIntest2, 0.0F, -2.356194490192345F, 0.0F);
        this.leftArmIntest1 = new ModelRenderer(this, 16, 32);
        this.leftArmIntest1.setRotationPoint(1.0F, -2.0F, 0.0F);
        this.leftArmIntest1.addBox(0.0F, -4.0F, -2.0F, 0, 4, 4, 0.0F);
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
    }
    
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}