package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntitySalmon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ModelSalmon - Sir Squidly
 * Created using Tabula 7.1.0
 */
@SideOnly(Side.CLIENT)
public class ModelSalmon extends ModelBase {
    public ModelRenderer body1;
    public ModelRenderer finL1;
    public ModelRenderer finR1;
    public ModelRenderer head1;
    public ModelRenderer body2;
    public ModelRenderer finT1;
    public ModelRenderer tail1;

    public ModelSalmon() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.finR1 = new ModelRenderer(this, 18, 10);
        this.finR1.setRotationPoint(-1.5F, 1.5F, -4.0F);
        this.finR1.addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(finR1, 0.0F, 0.0F, -0.7853981633974483F);
        this.finT1 = new ModelRenderer(this, 16, -7);
        this.finT1.setRotationPoint(0.0F, -2.5F, 3.0F);
        this.finT1.addBox(0.0F, -3.0F, -3.0F, 0, 3, 7, 0.0F);
        this.body2 = new ModelRenderer(this, 0, 20);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-1.5F, -2.5F, 0.0F, 3, 5, 7, 0.0F);
        this.head1 = new ModelRenderer(this, 5, 1);
        this.head1.setRotationPoint(0.0F, -0.5F, -7.0F);
        this.head1.addBox(-1.0F, -2.0F, -3.0F, 2, 4, 3, 0.0F);
        this.finL1 = new ModelRenderer(this, 18, 8);
        this.finL1.setRotationPoint(1.5F, 1.5F, -4.0F);
        this.finL1.addBox(0.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(finL1, 0.0F, 0.0F, 0.7853981633974483F);
        this.body1 = new ModelRenderer(this, 0, 8);
        this.body1.setRotationPoint(0.0F, 21.5F, 0.0F);
        this.body1.addBox(-1.5F, -2.5F, -7.0F, 3, 5, 7, 0.0F);
        this.tail1 = new ModelRenderer(this, 15, -2);
        this.tail1.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.tail1.addBox(0.0F, -2.5F, 0.0F, 0, 5, 5, 0.0F);
        this.body1.addChild(this.finR1);
        this.body2.addChild(this.finT1);
        this.body1.addChild(this.body2);
        this.body1.addChild(this.head1);
        this.body1.addChild(this.finL1);
        this.body2.addChild(this.tail1);
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
    	EntitySalmon fish = (EntitySalmon) entity;

            float flap = MathHelper.sin((fish.ticksExisted) * 0.6F) * 0.6F;
            if (fish.isFlopping())
            	flap = MathHelper.sin((fish.ticksExisted) * 1.2F) * 0.6F;
            body1.rotateAngleY = flap*0.2F;
            body2.rotateAngleY = flap*0.4F;
            tail1.rotateAngleY = flap*0.6F;
    }
}
