package com.sirsquidly.oe.client.model.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPickledHead extends ModelBase
{
	public ModelRenderer pickledHead;
	public ModelRenderer headIntest1;
    public ModelRenderer headIntest2;
    
    public ModelPickledHead()
    {
        this(0, 0, 64, 64);
    }

    public ModelPickledHead(int p_i1155_1_, int p_i1155_2_, int textureW, int textureH)
    {
        this.textureWidth = textureW;
        this.textureHeight = textureH;
        
        this.pickledHead = new ModelRenderer(this, p_i1155_1_, p_i1155_2_);
        this.pickledHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.pickledHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.headIntest1 = new ModelRenderer(this, 0, 32);
        this.headIntest1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.headIntest1.addBox(-4.0F, -6.0F, 0.0F, 8, 6, 0, 0.0F);
        //this.setRotateAngle(headIntest1, 0.0F, -0.7853981633974483F, 0.0F);
        
        this.headIntest2 = new ModelRenderer(this, 0, 32);
        this.headIntest2.mirror = true;
        this.headIntest2.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.headIntest2.addBox(-4.0F, -6.0F, 0.0F, 8, 6, 0, 0.0F);
        //this.setRotateAngle(headIntest2, 0.0F, -2.356194490192345F, 0.0F);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	this.pickledHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.pickledHead.rotateAngleX = headPitch * 0.017453292F;
        
        this.pickledHead.render(scale);
    }
    
    public void renderIntestines(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.headIntest1.rotateAngleY = netHeadYaw * 0.017453292F - 0.7853981633974483F;
        this.headIntest2.rotateAngleY = netHeadYaw * 0.017453292F - 2.356194490192345F;
        
        this.headIntest1.rotateAngleX = headPitch * 0.017453292F;
        this.headIntest2.rotateAngleX = headPitch * 0.017453292F;
        
        this.headIntest1.render(scale);
        this.headIntest2.render(scale);
    }
    
}
