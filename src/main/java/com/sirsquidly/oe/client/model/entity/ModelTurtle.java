package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityTurtle;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTurtle extends ModelBase {
    public ModelRenderer shell1;
    public ModelRenderer body1;
    public ModelRenderer head1;
    public ModelRenderer finfl1;
    public ModelRenderer finbl1;
    public ModelRenderer finfr1;
    public ModelRenderer finbr1;

    public ModelTurtle() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.head1 = new ModelRenderer(this, 0, 0);
        this.head1.setRotationPoint(0.0F, -0.5F, -10.0F);
        this.head1.addBox(-3.0F, -2.5F, -6.0F, 6, 5, 6, 0.0F);
        this.finfl1 = new ModelRenderer(this, 64, 0);
        this.finfl1.setRotationPoint(4.5F, 0.5F, -6.5F);
        this.finfl1.addBox(0.0F, -0.5F, -2.5F, 13, 1, 5, 0.0F);
        this.finfr1 = new ModelRenderer(this, 28, 0);
        this.finfr1.setRotationPoint(-5.5F, 0.5F, -6.5F);
        this.finfr1.addBox(-13.0F, -0.5F, -2.5F, 13, 1, 5, 0.0F);
        this.shell1 = new ModelRenderer(this, 0, 32);
        this.shell1.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.shell1.addBox(-9.5F, -6.0F, -10.0F, 19, 6, 20, 0.0F);
        this.body1 = new ModelRenderer(this, 0, 11);
        this.body1.setRotationPoint(0.5F, 0.0F, 0.0F);
        this.body1.addBox(-6.0F, 0.0F, -10.0F, 11, 3, 18, 0.0F);
        this.finbr1 = new ModelRenderer(this, 40, 6);
        this.finbr1.setRotationPoint(-4.0F, 0.5F, 8.0F);
        this.finbr1.addBox(-2.0F, -0.5F, 0.0F, 4, 1, 10, 0.0F);
        this.finbl1 = new ModelRenderer(this, 68, 6);
        this.finbl1.setRotationPoint(3.0F, 0.5F, 8.0F);
        this.finbl1.addBox(-2.0F, -0.5F, 0.0F, 4, 1, 10, 0.0F);
        this.setRotateAngle(finbl1, -0.014486232791552934F, 0.0F, 0.0F);
        this.shell1.addChild(this.head1);
        this.body1.addChild(this.finfl1);
        this.body1.addChild(this.finfr1);
        this.shell1.addChild(this.body1);
        this.body1.addChild(this.finbr1);
        this.body1.addChild(this.finbl1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shell1.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	EntityTurtle turtle = (EntityTurtle) entityIn;
    	float f1 = MathHelper.cos(limbSwing * 2.0F) * 3.0F * limbSwingAmount * 1.2F;
        float f2 = MathHelper.cos(limbSwing * 2.0F) * 2.0F * limbSwingAmount * 0.8F;
        float w1 = MathHelper.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount * 0.8F;
        float w2 = MathHelper.cos(limbSwing * 1.0F) * 0.8F * limbSwingAmount * 0.8F;
        
    	this.head1.rotateAngleX = headPitch * 0.017453292F;
        this.head1.rotateAngleY = netHeadYaw * 0.017453292F;
        
        this.finfl1.rotateAngleY = 0;
        this.finbl1.rotateAngleY = 0;
        this.finfr1.rotateAngleY = 0;
        this.finbr1.rotateAngleY = 0;
        this.finbl1.rotateAngleX = 0;
        this.finbr1.rotateAngleX = 0;
        this.finfl1.rotateAngleZ = 0;
        this.finfr1.rotateAngleZ = 0;
        
        if (turtle.isInWater())
        {
        	this.finfl1.rotateAngleZ = -w1;
            this.finbl1.rotateAngleX = -w2;
            this.finfr1.rotateAngleZ = w1;
            this.finbr1.rotateAngleX = w2;
        }
        else
        {
        	this.finfl1.rotateAngleY = -f1;
            this.finbl1.rotateAngleY = -f2;
            this.finfr1.rotateAngleY = f1;
            this.finbr1.rotateAngleY = f2;
        }
    }
}
