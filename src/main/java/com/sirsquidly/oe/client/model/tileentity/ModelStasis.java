package com.sirsquidly.oe.client.model.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelStasis extends ModelBase
{
    private final ModelRenderer main;
    private final ModelRenderer eye;
    private final ModelRenderer cageTop;
    private final ModelRenderer cageBottom;
    private final ModelRenderer ringTop;
    private final ModelRenderer ringBottom;

    public ModelStasis()
    {
        textureWidth = 64;
        textureHeight = 32;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);


        eye = new ModelRenderer(this);
        eye.setRotationPoint(0.0F, -8.0F, 0.0F);
        main.addChild(eye);
        eye.cubeList.add(new ModelBox(eye, 0, 0, -3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F, false));
        eye.cubeList.add(new ModelBox(eye, 18, -6, 0.0F, -3.0F, 3.0F, 0, 6, 6, 0.0F, false));
        eye.cubeList.add(new ModelBox(eye, 12, 12, -3.0F, 0.0F, 3.0F, 6, 0, 6, 0.0F, false));

        cageTop = new ModelRenderer(this);
        cageTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(cageTop);
        cageTop.cubeList.add(new ModelBox(cageTop, 32, 0, -4.0F, -16.0F, -4.0F, 8, 6, 8, 0.0F, false));

        cageBottom = new ModelRenderer(this);
        cageBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(cageBottom);
        cageBottom.cubeList.add(new ModelBox(cageBottom, 32, 14, -4.0F, -6.0F, -4.0F, 8, 6, 8, 0.0F, false));

        ringTop = new ModelRenderer(this);
        ringTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(ringTop);
        ringTop.cubeList.add(new ModelBox(ringTop, -8, 16, -4.0F, -15.0F, -4.0F, 8, 0, 8, 0.0F, false));

        ringBottom = new ModelRenderer(this);
        ringBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(ringBottom);
        ringBottom.cubeList.add(new ModelBox(ringBottom, -8, 24, -4.0F, -1.0F, -4.0F, 8, 0, 8, 0.0F, false));
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        main.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float f = (MathHelper.sin(limbSwing * 0.02F) * 0.1F + 1.25F);
        float shellSpeed = 1.0F;
        float ringSpeed = shellSpeed * 1.5F;

        /* Should just spin. */
        cageTop.rotateAngleY = limbSwing * shellSpeed;
        cageBottom.rotateAngleY = -limbSwing * shellSpeed;

        ringTop.rotateAngleY = limbSwing * ringSpeed;
        ringBottom.rotateAngleY = -limbSwing * ringSpeed;

        /* Eye rotates to target, as given prior. */
        eye.rotateAngleY = (float) (-1.6 - netHeadYaw);
        eye.rotateAngleX = -headPitch;
    }
}