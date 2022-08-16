package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityCod;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ModelCod - Sir Squidly
 * Created using Tabula 7.1.0
 */
@SideOnly(Side.CLIENT)
public class ModelCod extends ModelBase {
    public ModelRenderer body1;
    public ModelRenderer finL1;
    public ModelRenderer finR1;
    public ModelRenderer tail1;
    public ModelRenderer finT1;

    public ModelCod() {
    	this.textureWidth = 32;
        this.textureHeight = 16;
        this.tail1 = new ModelRenderer(this, 14, 2);
        this.tail1.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.tail1.addBox(0.0F, -2.0F, 0.0F, 0, 4, 4, 0.0F);
        this.finL1 = new ModelRenderer(this, 12, 2);
        this.finL1.setRotationPoint(1.0F, 1.0F, -1.0F);
        this.finL1.addBox(0.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(finL1, 0.0F, 0.0F, 0.7853981633974483F);
        this.finT1 = new ModelRenderer(this, 0, 6);
        this.finT1.setRotationPoint(0.0F, -4.0F, -4.0F);
        this.finT1.addBox(0.0F, 0.0F, 0.0F, 0, 2, 8, 0.0F);
        this.body1 = new ModelRenderer(this, 0, 0);
        this.body1.setRotationPoint(0.0F, 22.0F, -1.0F);
        this.body1.addBox(-1.0F, -2.0F, -5.0F, 2, 4, 10, 0.0F);
        this.finR1 = new ModelRenderer(this, 12, 4);
        this.finR1.setRotationPoint(-1.0F, 1.0F, -1.0F);
        this.finR1.addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(finR1, 0.0F, 0.0F, -0.7853981633974483F);
        this.body1.addChild(this.tail1);
        this.body1.addChild(this.finL1);
        this.body1.addChild(this.finT1);
        this.body1.addChild(this.finR1);
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
    public void setLivingAnimations(EntityLivingBase entity, float swing, float speed, float partialRenderTicks) {
    	EntityCod fish = (EntityCod) entity;

            float flap = MathHelper.sin((fish.ticksExisted) * 0.6F) * 0.6F;
            if (fish.isFlopping())
            	flap = MathHelper.sin((fish.ticksExisted) * 1.2F) * 0.6F;
            body1.rotateAngleY = flap*0.2F;
            tail1.rotateAngleY = flap*0.4F;
    }

}
