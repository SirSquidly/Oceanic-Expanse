package com.sirsquidly.oe.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

/**
 * This is the model for the Lobster
 * 
 * Some weird things are done here, specifically the Tail is actually shrunk down to half its size.
 * This is so Half-Half Lobsters can have that perfect half-pixel split on their tail!
 * 
 */
public class ModelLobster extends ModelBase
{
	private final ModelRenderer main;
	private final ModelRenderer legL3_r1;
	private final ModelRenderer legL4_r1;
	private final ModelRenderer legR1_r1;
	private final ModelRenderer legR2_r1;
	private final ModelRenderer legR3_r1;
	private final ModelRenderer legR4_r1;
	private final ModelRenderer legL2_r1;
	private final ModelRenderer legL1_r1;
	private final ModelRenderer rot_body;
	private final ModelRenderer body2;
	private final ModelRenderer head;
	private final ModelRenderer antennaR;
	private final ModelRenderer antennaL;
	private final ModelRenderer tail1;
	private final ModelRenderer tail2;
	private final ModelRenderer clawLOuter;
	private final ModelRenderer clawLInner;
	private final ModelRenderer clawROuter;
	private final ModelRenderer clawRInner;

	public ModelLobster()
	{
		textureWidth = 64;
		textureHeight = 32;

		main = new ModelRenderer(this);
		main.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		legL3_r1 = new ModelRenderer(this);
		legL3_r1.setRotationPoint(2.0F, -2.0F, 2.25F);
		main.addChild(legL3_r1);
		setRotationAngle(legL3_r1, 0.0F, -0.0873F, -0.3491F);
		legL3_r1.cubeList.add(new ModelBox(legL3_r1, 0, 28, -0.1632F, 0.0603F, -0.5594F, 3, 3, 1, 0.0F, false));

		legL4_r1 = new ModelRenderer(this);
		legL4_r1.setRotationPoint(2.0F, -2.0F, 3.5F);
		main.addChild(legL4_r1);
		setRotationAngle(legL4_r1, 0.0F, -0.2182F, -0.3491F);
		legL4_r1.cubeList.add(new ModelBox(legL4_r1, 0, 28, -0.1786F, 0.0603F, -0.617F, 3, 3, 1, 0.0F, false));

		legR1_r1 = new ModelRenderer(this);
		legR1_r1.setRotationPoint(-2.0F, -2.0F, -0.5F);
		main.addChild(legR1_r1);
		setRotationAngle(legR1_r1, -3.1416F, 0.2618F, -2.7925F);
		legR1_r1.cubeList.add(new ModelBox(legR1_r1, 9, 28, -0.1786F, 0.0603F, -0.383F, 3, 3, 1, 0.0F, false));

		legR2_r1 = new ModelRenderer(this);
		legR2_r1.setRotationPoint(-2.0F, -2.0F, 1.0F);
		main.addChild(legR2_r1);
		setRotationAngle(legR2_r1, 3.1416F, 0.0873F, -2.7925F);
		legR2_r1.cubeList.add(new ModelBox(legR2_r1, 9, 28, -0.1632F, 0.0603F, -0.4406F, 3, 3, 1, 0.0F, false));

		legR3_r1 = new ModelRenderer(this);
		legR3_r1.setRotationPoint(-2.0F, -2.0F, 2.25F);
		main.addChild(legR3_r1);
		setRotationAngle(legR3_r1, 3.1416F, -0.0873F, -2.7925F);
		legR3_r1.cubeList.add(new ModelBox(legR3_r1, 9, 28, -0.1632F, 0.0603F, -0.5594F, 3, 3, 1, 0.0F, false));

		legR4_r1 = new ModelRenderer(this);
		legR4_r1.setRotationPoint(-2.0F, -2.0F, 3.5F);
		main.addChild(legR4_r1);
		setRotationAngle(legR4_r1, -3.1416F, -0.2618F, -2.7925F);
		legR4_r1.cubeList.add(new ModelBox(legR4_r1, 9, 28, -0.1786F, 0.0603F, -0.617F, 3, 3, 1, 0.0F, false));

		legL2_r1 = new ModelRenderer(this);
		legL2_r1.setRotationPoint(2.0F, -2.0F, 1.0F);
		main.addChild(legL2_r1);
		setRotationAngle(legL2_r1, 0.0F, 0.0873F, -0.3491F);
		legL2_r1.cubeList.add(new ModelBox(legL2_r1, 0, 28, -0.1632F, 0.0603F, -0.4406F, 3, 3, 1, 0.0F, false));

		legL1_r1 = new ModelRenderer(this);
		legL1_r1.setRotationPoint(2.0F, -2.0F, -0.5F);
		main.addChild(legL1_r1);
		setRotationAngle(legL1_r1, 0.0F, 0.2618F, -0.3491F);
		legL1_r1.cubeList.add(new ModelBox(legL1_r1, 0, 28, -0.1786F, 0.0603F, -0.383F, 3, 3, 1, 0.0F, false));

		rot_body = new ModelRenderer(this);
		rot_body.setRotationPoint(0.0F, -3.0F, 1.0F);
		main.addChild(rot_body);
		

		body2 = new ModelRenderer(this);
		body2.setRotationPoint(0.0F, 3.0F, -1.0F);
		rot_body.addChild(body2);
		body2.cubeList.add(new ModelBox(body2, 0, 5, -2.0F, -4.0F, -2.0F, 4, 3, 7, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, -3.0F);
		rot_body.addChild(head);
		head.cubeList.add(new ModelBox(head, 0, 0, -2.0F, -1.0F, -3.0F, 4, 2, 3, 0.0F, false));

		antennaR = new ModelRenderer(this);
		antennaR.setRotationPoint(-1.0F, -0.5F, -3.0F);
		head.addChild(antennaR);
		antennaR.cubeList.add(new ModelBox(antennaR, 30, -8, 0.0F, -3.5F, -2.0F, 0, 4, 8, 0.0F, true));

		antennaL = new ModelRenderer(this);
		antennaL.setRotationPoint(1.0F, -0.5F, -3.0F);
		head.addChild(antennaL);
		antennaL.cubeList.add(new ModelBox(antennaL, 14, -8, 0.0F, -3.5F, -2.0F, 0, 4, 8, 0.0F, false));

		tail1 = new ModelRenderer(this);
		tail1.setRotationPoint(0.0F, 0.0F, 4.0F);
		rot_body.addChild(tail1);
		setRotationAngle(tail1, -0.2182F, 0.0F, 0.0F);
		tail1.cubeList.add(new ModelBox(tail1, 0, 15, -1.5F, -0.8853F, -0.1165F, 3, 2, 5, 0.0F, false));

		tail2 = new ModelRenderer(this);
		tail2.setRotationPoint(0.0F, 0.2825F, 5.015F);
		tail1.addChild(tail2);
		tail2.cubeList.add(new ModelBox(tail2, -4, 22, -2.5F, -0.1678F, -1.1315F, 5, 0, 4, 0.0F, false));

		clawLOuter = new ModelRenderer(this);
		clawLOuter.setRotationPoint(2.0F, -2.0F, -3.0F);
		main.addChild(clawLOuter);
		setRotationAngle(clawLOuter, 0.0F, -0.4363F, 0.0F);
		clawLOuter.cubeList.add(new ModelBox(clawLOuter, 22, 6, 0.0F, -1.0F, -5.5F, 3, 3, 6, 0.0F, false));

		clawLInner = new ModelRenderer(this);
		clawLInner.setRotationPoint(0.0F, 0.0F, 0.0F);
		clawLOuter.addChild(clawLInner);
		clawLInner.cubeList.add(new ModelBox(clawLInner, 23, 16, 0.0F, -1.0F, -5.5F, 3, 3, 5, -0.01F, false));

		clawROuter = new ModelRenderer(this);
		clawROuter.setRotationPoint(-2.0F, -2.0F, -3.0F);
		main.addChild(clawROuter);
		setRotationAngle(clawROuter, 0.0F, 0.4363F, 0.0F);
		clawROuter.cubeList.add(new ModelBox(clawROuter, 40, 6, -3.0F, -1.0F, -5.5F, 3, 3, 6, 0.0F, true));

		clawRInner = new ModelRenderer(this);
		clawRInner.setRotationPoint(-1.5F, 0.5F, 0.5F);
		clawROuter.addChild(clawRInner);
		clawRInner.cubeList.add(new ModelBox(clawRInner, 41, 16, -1.5F, -1.5F, -6.0F, 3, 3, 5, -0.01F, true));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{ main.render(f5); }
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {	
    	this.legL1_r1.rotateAngleY = 0.2618F;
        this.legL2_r1.rotateAngleY = 0.0873F;
        this.legL3_r1.rotateAngleY = -0.0873F;
        this.legL4_r1.rotateAngleY = -0.2618F;
        this.legR1_r1.rotateAngleY = 0.2618F;
        this.legR2_r1.rotateAngleY = 0.0873F;
        this.legR3_r1.rotateAngleY = -0.0873F;
        this.legR4_r1.rotateAngleY = -0.2618F;

        this.legL1_r1.rotateAngleZ = -0.3491F;
        this.legL2_r1.rotateAngleZ = -0.3491F;
        this.legL3_r1.rotateAngleZ = -0.3491F;
        this.legL4_r1.rotateAngleZ = -0.3491F;
        this.legR1_r1.rotateAngleZ = -2.7925F;
        this.legR2_r1.rotateAngleZ = -2.7925F;
        this.legR3_r1.rotateAngleZ = -2.7925F;
        this.legR4_r1.rotateAngleZ = -2.7925F;
        
        this.tail1.rotateAngleX = -0.2182F;
        
        float l1 = Math.abs(MathHelper.sin(limbSwing*2 * 0.8F + 0.0F) * 1F) * limbSwingAmount;
        float l2 = Math.abs(MathHelper.sin(limbSwing*2 * 0.8F + 0.33F) * 1F) * limbSwingAmount;
        float l3 = Math.abs(MathHelper.sin(limbSwing*2 * 0.8F + 0.66F) * 1F) * limbSwingAmount;
        float l4 = Math.abs(MathHelper.sin(limbSwing*2 * 0.8F + 0.99F) * 1F) * limbSwingAmount;
        
        float f7 = Math.abs(MathHelper.sin(limbSwing*2 * 0.6662F + 0.0F) * 1.0F) * limbSwingAmount;
        float f8 = Math.abs(MathHelper.sin(limbSwing*2 * 0.6662F + (float)Math.PI) * 1F) * limbSwingAmount;
        float f9 = Math.abs(MathHelper.sin(limbSwing*2 * 0.6662F + ((float)Math.PI / 2F)) * 1F) * limbSwingAmount;
        float f10 = Math.abs(MathHelper.sin(limbSwing*2 * 0.6662F + ((float)Math.PI * 3F / 2F)) * 1F) * limbSwingAmount;
        
        this.legL1_r1.rotateAngleY += -l1;
        this.legL2_r1.rotateAngleY += -l2;
        this.legL3_r1.rotateAngleY += -l3;
        this.legL4_r1.rotateAngleY += -l4;
        this.legR1_r1.rotateAngleY += -l1;
        this.legR2_r1.rotateAngleY += -l2;
        this.legR3_r1.rotateAngleY += -l3;
        this.legR4_r1.rotateAngleY += -l4;
        
        this.legL1_r1.rotateAngleZ += -f7;
        this.legL2_r1.rotateAngleZ += -f8;
        this.legL3_r1.rotateAngleZ += -f9;
        this.legL4_r1.rotateAngleZ += -f10;
        this.legR1_r1.rotateAngleZ += f7;
        this.legR2_r1.rotateAngleZ += f8;
        this.legR3_r1.rotateAngleZ += f9;
        this.legR4_r1.rotateAngleZ += f10;
        
        /** This chunk handles the Lobster angling its body to face things */
        this.tail1.rotateAngleX += headPitch/1.75 * -0.017453292F;
        
        this.antennaR.rotateAngleX = headPitch/2 * 0.017453292F;
        this.antennaL.rotateAngleX = headPitch/2 * 0.017453292F;
        this.antennaR.rotateAngleY = netHeadYaw/2 * 0.017453292F;
        this.antennaL.rotateAngleY = netHeadYaw/2 * 0.017453292F;
        
        
        this.rot_body.rotateAngleX = headPitch/2 * 0.017453292F;
        this.rot_body.rotateAngleY = netHeadYaw/2 * 0.017453292F;
        
        this.clawLOuter.rotateAngleX = headPitch/2 * 0.017453292F;
        this.clawLOuter.rotateAngleY = -0.4363F + netHeadYaw/2 * 0.017453292F;
        
        
        this.clawROuter.rotateAngleX = headPitch/2 * 0.017453292F;
        this.clawROuter.rotateAngleY = 0.4363F + netHeadYaw/2 * 0.017453292F;
    }
	
	 public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime)
	 {
		 float idle = MathHelper.sin((entity.ticksExisted) * 0.07F) * 0.7F;
		 
		 this.antennaL.rotateAngleX = 0;
	     this.antennaR.rotateAngleX = 0;
		 
	     this.antennaL.rotateAngleX += 0.15F + idle*0.2F;
	     this.antennaR.rotateAngleX += 0.15F + idle*0.2F;
	     
	     this.clawLOuter.rotateAngleX = 0;
	     this.clawROuter.rotateAngleX = 0;
	     
	     this.clawLOuter.rotateAngleX += 0.15F + idle*0.1F;
	     this.clawROuter.rotateAngleX += 0.15F + idle*0.1F;
		 
	     //this.body.offsetX = 0.0F;
	     
	     
		 //EntityCrab crab = (EntityCrab) entity;
	 }
}