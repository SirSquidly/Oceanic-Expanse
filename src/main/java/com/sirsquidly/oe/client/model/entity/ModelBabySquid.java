package com.sirsquidly.oe.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBabySquid extends ModelBase {
    public ModelRenderer body1;
    public ModelRenderer body2;
    public ModelRenderer tentacleB1;
    public ModelRenderer tentacleF1;
    public ModelRenderer tentacleR1;
    public ModelRenderer tentacleL1;

    public ModelBabySquid() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.tentacleB1 = new ModelRenderer(this, 16, 20);
        this.tentacleB1.setRotationPoint(0.0F, 18.0F, 3.0F);
        this.tentacleB1.addBox(-3.0F, 0.0F, 0.0F, 6, 6, 0, 0.0F);
        this.tentacleF1 = new ModelRenderer(this, 0, 20);
        this.tentacleF1.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.tentacleF1.addBox(-3.0F, 0.0F, 0.0F, 6, 6, 0, 0.0F);
        this.setRotateAngle(tentacleF1, 0.0F, 3.141592653589793F, 0.0F);
        this.body1 = new ModelRenderer(this, 0, 0);
        this.body1.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.body1.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(body1, 0.0F, 0.0F, -0.008534618045777565F);
        this.tentacleL1 = new ModelRenderer(this, 16, 26);
        this.tentacleL1.setRotationPoint(-3.0F, 18.0F, 0.0F);
        this.tentacleL1.addBox(-3.0F, 0.0F, 0.0F, 6, 6, 0, 0.0F);
        this.setRotateAngle(tentacleL1, 0.0F, -1.5707963267948966F, 0.0F);
        this.body2 = new ModelRenderer(this, 0, 12);
        this.body2.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.body2.addBox(-3.0F, -2.0F, -3.0F, 6, 2, 6, 0.0F);
        this.tentacleR1 = new ModelRenderer(this, 0, 26);
        this.tentacleR1.setRotationPoint(3.0F, 18.0F, 0.0F);
        this.tentacleR1.addBox(-3.0F, 0.0F, 0.0F, 6, 6, 0, 0.0F);
        this.setRotateAngle(tentacleR1, 0.0F, 1.5707963267948966F, 0.0F);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    	
    	this.body1.render(scale);
    	this.body2.render(scale);
    	
    	this.tentacleB1.render(scale);
        this.tentacleF1.render(scale);
        this.tentacleL1.render(scale);
        this.tentacleR1.render(scale);
    }
    
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	this.tentacleB1.rotateAngleX = ageInTicks;
    	this.tentacleF1.rotateAngleX = ageInTicks;
    	this.tentacleL1.rotateAngleX = ageInTicks;
    	this.tentacleR1.rotateAngleX = ageInTicks;
    }
}
