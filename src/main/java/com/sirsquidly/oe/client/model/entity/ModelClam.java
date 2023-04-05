package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityClam;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelClam extends ModelBase
{
	public ModelRenderer base;
    public ModelRenderer shell_top;
    public ModelRenderer shell_bottom;

	public ModelClam()
	{
		this.textureWidth = 128;
        this.textureHeight = 80;
        this.shell_top = new ModelRenderer(this, 0, 0);
        this.shell_top.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shell_top.addBox(-16.0F, -3.0F, -24.0F, 32, 3, 24, 0.0F);
        this.base = new ModelRenderer(this, 34, 54);
        this.base.setRotationPoint(0.0F, 21.0F, 10.0F);
        this.base.addBox(-8.0F, -3.0F, 0.0F, 16, 6, 6, 0.0F);
        this.shell_bottom = new ModelRenderer(this, 0, 27);
        this.shell_bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shell_bottom.addBox(-16.0F, 0.0F, -24.0F, 32, 3, 24, 0.0F);
        this.base.addChild(this.shell_top);
        this.base.addChild(this.shell_bottom);
	}

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{ 
        this.base.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	
    	EntityClam clam = (EntityClam)entityIn;
        float f = ageInTicks - (float)clam.ticksExisted;
        float f1 = (0.5F + clam.getClientOpenAmount(f)) * (float)Math.PI;
    	
    	this.shell_top.rotateAngleX = 4.567F + MathHelper.sin(f1) * 8.0F;
    }
}