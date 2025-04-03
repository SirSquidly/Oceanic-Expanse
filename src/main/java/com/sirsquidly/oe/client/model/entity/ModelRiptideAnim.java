package com.sirsquidly.oe.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRiptideAnim extends ModelBase
{
    private final ModelRenderer spin;
    private final ModelRenderer spin1;
    private final ModelRenderer spin2;

    public ModelRiptideAnim()
    {
        textureWidth = 128;
        textureHeight = 128;

        spin = new ModelRenderer(this);
        spin.setRotationPoint(0.0F, 24.0F, -3.0F);

        spin1 = new ModelRenderer(this);
        spin1.setRotationPoint(0.0F, 0.0F, 0.0F);
        spin.addChild(spin1);
        spin1.cubeList.add(new ModelBox(spin1, 0, 0, -9.0F, -33.0F, -9.0F, 18, 50, 18, 0.0F, false));

        spin2 = new ModelRenderer(this);
        spin2.setRotationPoint(0.0F, -5.0F, 0.0F);
        spin.addChild(spin2);
        setRotationAngle(spin2, 0.0F, 1.5708F, 0.0F);
        spin2.cubeList.add(new ModelBox(spin2, 0, 0, -9.0F, -34.25F, -9.0F, 18, 50, 18, -2.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableCull();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isChild())
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 1.2F, -0.1F);
            spin.render(f5);
            GlStateManager.popMatrix();
        }
        else
        {
            spin.render(f5);
        }
    }

    /** Spinning is intentionally de-synced from the Entity Spinning. */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float spiny = ageInTicks * (float)Math.PI;
        spin1.rotateAngleY = spiny * 0.1F;
        spin2.rotateAngleY = spiny * 0.05F;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}