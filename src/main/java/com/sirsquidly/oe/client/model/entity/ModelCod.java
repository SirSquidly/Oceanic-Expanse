package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.AbstractFish;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
	
@SideOnly(Side.CLIENT)
public class ModelCod extends ModelBase {
	public ModelRenderer body1;
    public ModelRenderer finL1;
    public ModelRenderer finR1;
    public ModelRenderer tail1;
    public ModelRenderer finT1;
    public ModelRenderer head1;
    public ModelRenderer nose1;

    public ModelCod() {
    	this.textureWidth = 32;
        this.textureHeight = 16;
        this.finR1 = new ModelRenderer(this, -2, 6);
        this.finR1.setRotationPoint(-1.0F, 1.0F, -1.0F);
        this.finR1.addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(finR1, 0.0F, 0.0F, -0.7853981633974483F);
        this.finL1 = new ModelRenderer(this, -2, 4);
        this.finL1.setRotationPoint(1.0F, 1.0F, -1.0F);
        this.finL1.addBox(0.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(finL1, 0.0F, 0.0F, 0.7853981633974483F);
        this.head1 = new ModelRenderer(this, 11, 0);
        this.head1.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.head1.addBox(-1.0F, -2.0F, -3.0F, 2, 4, 3, 0.0F);
        this.body1 = new ModelRenderer(this, 0, 3);
        this.body1.setRotationPoint(0.0F, 22.0F, -1.0F);
        this.body1.addBox(-1.0F, -2.0F, -2.0F, 2, 4, 7, 0.0F);
        this.tail1 = new ModelRenderer(this, 18, 8);
        this.tail1.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.tail1.addBox(0.0F, -2.0F, 0.0F, 0, 4, 4, 0.0F);
        this.finT1 = new ModelRenderer(this, 0, 6);
        this.finT1.setRotationPoint(0.0F, -4.0F, -4.0F);
        this.finT1.addBox(0.0F, 0.0F, 0.0F, 0, 2, 8, 0.0F);
        this.nose1 = new ModelRenderer(this, 0, 0);
        this.nose1.setRotationPoint(0.0F, -1.0F, -3.0F);
        this.nose1.addBox(-1.0F, -1.0F, -1.0F, 2, 3, 1, 0.0F);
        this.body1.addChild(this.finR1);
        this.body1.addChild(this.finL1);
        this.body1.addChild(this.head1);
        this.body1.addChild(this.tail1);
        this.body1.addChild(this.finT1);
        this.head1.addChild(this.nose1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body1.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
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
        head1.rotateAngleY = flap*0.1F;
        tail1.rotateAngleY = flap*0.6F;
    }
}
