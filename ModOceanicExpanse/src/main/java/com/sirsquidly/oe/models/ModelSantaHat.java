package com.sirsquidly.oe.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

/**
 * ModelSantaHat - Sir Squidly
 * Created using Tabula 7.1.0
 */
public class ModelSantaHat extends ModelBiped {
    public ModelRenderer hat_rim1;
    public ModelRenderer hatPart1;
    public ModelRenderer hatPart2;
    public ModelRenderer puff;

    public ModelSantaHat() {
    	this.textureWidth = 64;
        this.textureHeight = 72;
        this.hat_rim1 = new ModelRenderer(this, 0, 39);
        this.hat_rim1.setRotationPoint(-4.5F, -10.0F, -3.0F);
        this.hat_rim1.addBox(0.0F, 0.0F, -1.5F, 9, 3, 9, 0.2F);
        this.setRotateAngle(hat_rim1, -0.06981317007977318F, 0.0F, 0.0F);
        this.hatPart2 = new ModelRenderer(this, 27, 39);
        this.hatPart2.setRotationPoint(-5.8F, -8.3F, 0.5F);
        this.hatPart2.addBox(0.0F, -4.0F, 0.0F, 1, 4, 2, 0.1F);
        this.setRotateAngle(hatPart2, -0.03490658503988659F, 0.0F, 0.2617993877991494F);
        this.puff = new ModelRenderer(this, 36, 39);
        this.puff.setRotationPoint(-7.3F, -8.0F, 0.0F);
        this.puff.addBox(0.0F, 0.0F, 0.0F, 3, 3, 3, 0.0F);
        this.hatPart1 = new ModelRenderer(this, 0, 51);
        this.hatPart1.setRotationPoint(-4.5F, -14.6F, -2.0F);
        this.hatPart1.addBox(0.0F, 2.0F, 0.0F, 7, 3, 6, 0.2F);
        this.setRotateAngle(hatPart1, -0.05235987755982988F, 0.0F, -0.06981317007977318F);
        
        this.bipedHead.addChild(hat_rim1);
        this.bipedHead.addChild(hatPart1);
        this.bipedHead.addChild(hatPart2);
        this.bipedHead.addChild(puff);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	
    	// So the hat rotates properly with the armor stand. Kinda hacky, but it works.
    	if(entity instanceof EntityArmorStand) {
    		f3 = 0;
		}
    	
    	super.render(entity, f, f1, f2, f3, f4, f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
