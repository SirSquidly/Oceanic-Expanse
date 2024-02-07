package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.AbstractFish;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTropicalFishA extends ModelBase {
	public ModelRenderer body1;
    public ModelRenderer finL1;
    public ModelRenderer finR1;
    public ModelRenderer tail1;
    public ModelRenderer finT1;

    public ModelTropicalFishA() {
    	this.textureWidth = 32;
        this.textureHeight = 32;
        this.tail1 = new ModelRenderer(this, 14, -4);
        this.tail1.setRotationPoint(0.0F, 0.5F, 6.0F);
        this.tail1.addBox(0.0F, -2.0F, 0.0F, 0, 3, 4, 0.0F);
        this.finT1 = new ModelRenderer(this, 0, 3);
        this.finT1.setRotationPoint(0.0F, -1.5F, 3.0F);
        this.finT1.addBox(0.0F, -3.0F, -3.0F, 0, 3, 6, 0.0F);
        this.finL1 = new ModelRenderer(this, 10, -2);
        this.finL1.setRotationPoint(1.0F, 0.5F, 3.0F);
        this.finL1.addBox(0.0F, -1.0F, 0.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(finL1, 0.0F, 0.7853981633974483F, 0.0F);
        this.body1 = new ModelRenderer(this, 0, 0);
        this.body1.setRotationPoint(0.0F, 22.5F, -3.0F);
        this.body1.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 6, 0.0F);
        this.finR1 = new ModelRenderer(this, 10, 0);
        this.finR1.setRotationPoint(-1.0F, 0.5F, 3.0F);
        this.finR1.addBox(0.0F, -1.0F, 0.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(finR1, 0.0F, -0.7853981633974483F, 0.0F);
        this.body1.addChild(this.tail1);
        this.body1.addChild(this.finT1);
        this.body1.addChild(this.finL1);
        this.body1.addChild(this.finR1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body1.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	AbstractFish fish = (AbstractFish) entityIn;

        float flap = MathHelper.sin((ageInTicks) * 0.6F) * 0.6F;
        if (fish.isFlopping())
        	flap = MathHelper.sin((ageInTicks) * 1.2F) * 0.6F;
        body1.rotateAngleY = flap*0.2F;
        tail1.rotateAngleY = flap*0.6F;
    }
}