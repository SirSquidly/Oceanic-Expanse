package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityPufferfish;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPufferfish extends ModelBase {
    public ModelRenderer s3body1;
    public ModelRenderer s2body1;
    public ModelRenderer s1body1;
    public ModelRenderer s3spines1;
    public ModelRenderer s3spines2;
    public ModelRenderer s3spines3;
    public ModelRenderer s3spines4;
    public ModelRenderer s3spines5;
    public ModelRenderer s3spines5_1;
    public ModelRenderer s3finL1;
    public ModelRenderer s3finR1;
    public ModelRenderer s3tail1;
    public ModelRenderer s2spines1;
    public ModelRenderer s2spines2;
    public ModelRenderer s2spines3;
    public ModelRenderer s2spines4;
    public ModelRenderer s2finL1;
    public ModelRenderer s2finR1;
    public ModelRenderer s2tail1;
    public ModelRenderer s1finL1;
    public ModelRenderer s1finR1;
    public ModelRenderer s1tail1;

    public ModelPufferfish() {
        this.textureWidth = 32;
        this.textureHeight = 48;
        this.s3tail1 = new ModelRenderer(this, 21, 4);
        this.s3tail1.setRotationPoint(0.0F, -1.0F, 4.0F);
        this.s3tail1.addBox(-1.5F, 0.0F, 0.0F, 3, 0, 3, 0.0F);
        this.s2finL1 = new ModelRenderer(this, 20, -2);
        this.s2finL1.setRotationPoint(2.5F, -1.5F, -0.5F);
        this.s2finL1.addBox(0.0F, 0.0F, -1.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(s2finL1, 0.0F, 0.0F, -0.7853981633974483F);
        this.s3spines5 = new ModelRenderer(this, 0, 29);
        this.s3spines5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s3spines5.addBox(-6.5F, -4.0F, 0.0F, 13, 8, 0, 0.0F);
        this.setRotateAngle(s3spines5, 0.0F, 0.7853981633974483F, 0.0F);
        this.s2spines1 = new ModelRenderer(this, 0, 10);
        this.s2spines1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s2spines1.addBox(-2.5F, -4.5F, 0.0F, 5, 9, 0, 0.0F);
        this.setRotateAngle(s2spines1, -0.7853981633974483F, 0.0F, 0.0F);
        this.s3body1 = new ModelRenderer(this, 0, 0);
        this.s3body1.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.s3body1.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        this.s3finL1 = new ModelRenderer(this, 24, -2);
        this.s3finL1.setRotationPoint(4.0F, -2.0F, -1.0F);
        this.s3finL1.addBox(0.0F, 0.0F, -1.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(s3finL1, 0.0F, 0.0F, -0.7853981633974483F);
        this.s1finL1 = new ModelRenderer(this, 20, -2);
        this.s1finL1.setRotationPoint(1.5F, 0.0F, 0.0F);
        this.s1finL1.addBox(0.0F, 0.0F, -1.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(s1finL1, 0.0F, 0.0F, -0.7853981633974483F);
        this.s3spines1 = new ModelRenderer(this, 0, 16);
        this.s3spines1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s3spines1.addBox(-4.0F, -6.5F, 0.0F, 8, 13, 0, 0.0F);
        this.setRotateAngle(s3spines1, -0.7853981633974483F, 0.0F, 0.0F);
        this.s2finR1 = new ModelRenderer(this, 20, 0);
        this.s2finR1.setRotationPoint(-2.5F, -1.5F, -0.5F);
        this.s2finR1.addBox(0.0F, 0.0F, -1.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(s2finR1, 0.0F, -0.005061454830783555F, 0.7853981633974483F);
        this.s3spines2 = new ModelRenderer(this, 0, 16);
        this.s3spines2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s3spines2.addBox(-4.0F, -6.5F, 0.0F, 8, 13, 0, 0.0F);
        this.setRotateAngle(s3spines2, 0.7853981633974483F, 0.0F, 0.0F);
        this.s1tail1 = new ModelRenderer(this, 17, 4);
        this.s1tail1.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.s1tail1.addBox(-1.5F, 0.0F, 0.0F, 3, 0, 3, 0.0F);
        this.s1finR1 = new ModelRenderer(this, 20, 0);
        this.s1finR1.setRotationPoint(-1.5F, 0.0F, 0.0F);
        this.s1finR1.addBox(0.0F, 0.0F, -1.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(s1finR1, 0.0F, 0.0F, 0.7853981633974483F);
        this.s3spines4 = new ModelRenderer(this, 0, 16);
        this.s3spines4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s3spines4.addBox(-4.0F, -6.5F, 0.0F, 8, 13, 0, 0.0F);
        this.setRotateAngle(s3spines4, 0.7853981633974483F, 1.5707963267948966F, 0.0F);
        this.s3finR1 = new ModelRenderer(this, 24, 0);
        this.s3finR1.setRotationPoint(-4.0F, -2.0F, -1.0F);
        this.s3finR1.addBox(0.0F, 0.0F, -1.0F, 0, 2, 2, 0.0F);
        this.setRotateAngle(s3finR1, 0.0F, 0.0F, 0.7853981633974483F);
        this.s1body1 = new ModelRenderer(this, 0, 0);
        this.s1body1.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.s1body1.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 4, 0.0F);
        this.s2tail1 = new ModelRenderer(this, 17, 4);
        this.s2tail1.setRotationPoint(0.0F, -0.5F, 2.5F);
        this.s2tail1.addBox(-1.5F, 0.0F, 0.0F, 3, 0, 3, 0.0F);
        this.s2spines4 = new ModelRenderer(this, 0, 10);
        this.s2spines4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s2spines4.addBox(-2.5F, -4.5F, 0.0F, 5, 9, 0, 0.0F);
        this.setRotateAngle(s2spines4, 0.0F, -1.5707963267948966F, -0.7853981633974483F);
        this.s3spines5_1 = new ModelRenderer(this, 0, 29);
        this.s3spines5_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s3spines5_1.addBox(-6.4F, -4.0F, 0.0F, 13, 8, 0, 0.0F);
        this.setRotateAngle(s3spines5_1, 0.0F, 2.356194490192345F, 0.0F);
        this.s2body1 = new ModelRenderer(this, 0, 0);
        this.s2body1.setRotationPoint(0.0F, 20.5F, 0.0F);
        this.s2body1.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.s3spines3 = new ModelRenderer(this, 0, 16);
        this.s3spines3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s3spines3.addBox(-4.0F, -6.5F, 0.0F, 8, 13, 0, 0.0F);
        this.setRotateAngle(s3spines3, 0.7853981633974483F, -1.5707963267948966F, 0.0F);
        this.s2spines2 = new ModelRenderer(this, 0, 10);
        this.s2spines2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s2spines2.addBox(-2.5F, -4.5F, 0.0F, 5, 9, 0, 0.0F);
        this.setRotateAngle(s2spines2, 0.7853981633974483F, 0.0F, 0.0F);
        this.s2spines3 = new ModelRenderer(this, 0, 10);
        this.s2spines3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.s2spines3.addBox(-2.5F, -4.5F, 0.0F, 5, 9, 0, 0.0F);
        this.setRotateAngle(s2spines3, 0.0F, 1.5707963267948966F, 0.7853981633974483F);
        this.s3body1.addChild(this.s3tail1);
        this.s2body1.addChild(this.s2finL1);
        this.s3body1.addChild(this.s3spines5);
        this.s2body1.addChild(this.s2spines1);
        this.s3body1.addChild(this.s3finL1);
        this.s1body1.addChild(this.s1finL1);
        this.s3body1.addChild(this.s3spines1);
        this.s2body1.addChild(this.s2finR1);
        this.s3body1.addChild(this.s3spines2);
        this.s1body1.addChild(this.s1tail1);
        this.s1body1.addChild(this.s1finR1);
        this.s3body1.addChild(this.s3spines4);
        this.s3body1.addChild(this.s3finR1);
        this.s2body1.addChild(this.s2tail1);
        this.s2body1.addChild(this.s2spines4);
        this.s3body1.addChild(this.s3spines5_1);
        this.s3body1.addChild(this.s3spines3);
        this.s2body1.addChild(this.s2spines2);
        this.s2body1.addChild(this.s2spines3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        EntityPufferfish puff = (EntityPufferfish) entity;

        if(puff.getPuffState() == 0)
        {
            this.s1body1.render(f5);
        }
        else if(puff.getPuffState() == 1)
        {
            this.s2body1.render(f5);
        }
        else if(puff.getPuffState() == 2)
        {
            this.s3body1.render(f5);
        }
        else
        {
            this.s1body1.render(f5);
            this.s2body1.render(f5);
            this.s3body1.render(f5);
        }
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setLivingAnimations(EntityLivingBase entity, float swing, float speed, float partialRenderTicks) {
    	EntityPufferfish fish = (EntityPufferfish) entity;

            float tail = MathHelper.sin((fish.ticksExisted) * 0.6F) * 0.6F;
            float flap = MathHelper.sin((fish.ticksExisted)* 0.1F) * 0.3F + 1.0F;
            if (fish.isFlopping())
            	tail = MathHelper.sin((fish.ticksExisted) * 1.2F) * 0.6F;
            	flap = MathHelper.sin((fish.ticksExisted)* 0.4F) * 0.3F + 1.0F;;
            s1finL1.rotateAngleZ = flap*-1.0F;
            s1finR1.rotateAngleZ = flap*1.0F;
            s2finL1.rotateAngleZ = flap*-1.0F;
            s2finR1.rotateAngleZ = flap*1.0F;
            s3finL1.rotateAngleZ = flap*-1.0F;
            s3finR1.rotateAngleZ = flap*1.0F;
            
            s1tail1.rotateAngleY = tail*0.4F;
            s2tail1.rotateAngleY = tail*0.4F;
            s3tail1.rotateAngleY = tail*0.4F;
    }
}
