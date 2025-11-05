package com.sirsquidly.oe.client.model.entity;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNautilusCoral extends ModelNautilus
{
    private final ModelRenderer coralYellow;
    private final ModelRenderer coralYellow2_r1;
    private final ModelRenderer coralYellow1_r1;
    private final ModelRenderer coralPink;
    private final ModelRenderer coralPink2_r1;
    private final ModelRenderer coralPink1_r1;
    private final ModelRenderer coralBlue;
    private final ModelRenderer coralBlue2_r1;
    private final ModelRenderer coralBlue1_r1;
    private final ModelRenderer coralRed;
    private final ModelRenderer coralRed2_r1;
    private final ModelRenderer coralRed1_r1;

    public ModelNautilusCoral()
    {
        textureWidth = 128;
        textureHeight = 128;

        coralYellow = new ModelRenderer(this);
        coralYellow.setRotationPoint(-13.0F, 17.0F, -3.5F);

        coralYellow2_r1 = new ModelRenderer(this);
        coralYellow2_r1.setRotationPoint(6.0F, -2.0F, 1.5F);
        coralYellow.addChild(coralYellow2_r1);
        setRotationAngle(coralYellow2_r1, 0.0F, -0.7854F, 0.0F);
        coralYellow2_r1.cubeList.add(new ModelBox(coralYellow2_r1, 95, -6, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralYellow1_r1 = new ModelRenderer(this);
        coralYellow1_r1.setRotationPoint(6.0F, -2.0F, 1.5F);
        coralYellow.addChild(coralYellow1_r1);
        setRotationAngle(coralYellow1_r1, 0.0F, 0.7854F, 0.0F);
        coralYellow1_r1.cubeList.add(new ModelBox(coralYellow1_r1, 83, -6, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralPink = new ModelRenderer(this);
        coralPink.setRotationPoint(0.0F, 21.0F, 7.5F);


        coralPink2_r1 = new ModelRenderer(this);
        coralPink2_r1.setRotationPoint(6.0F, -14.0F, -15.5F);
        coralPink.addChild(coralPink2_r1);
        setRotationAngle(coralPink2_r1, -1.5708F, 0.0F, 1.5708F);
        coralPink2_r1.cubeList.add(new ModelBox(coralPink2_r1, 95, 5, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralPink1_r1 = new ModelRenderer(this);
        coralPink1_r1.setRotationPoint(6.0F, -14.0F, -16.5F);
        coralPink.addChild(coralPink1_r1);
        setRotationAngle(coralPink1_r1, -1.5708F, 0.0F, -3.1416F);
        coralPink1_r1.cubeList.add(new ModelBox(coralPink1_r1, 83, 5, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralBlue = new ModelRenderer(this);
        coralBlue.setRotationPoint(0.0F, 24.0F, 0.0F);

        coralBlue2_r1 = new ModelRenderer(this);
        coralBlue2_r1.setRotationPoint(7.0F, 0.0F, 5.5F);
        coralBlue.addChild(coralBlue2_r1);
        setRotationAngle(coralBlue2_r1, 0.0F, -0.7854F, 0.0F);
        coralBlue2_r1.cubeList.add(new ModelBox(coralBlue2_r1, 95, 16, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralBlue1_r1 = new ModelRenderer(this);
        coralBlue1_r1.setRotationPoint(7.0F, 0.0F, 5.5F);
        coralBlue.addChild(coralBlue1_r1);
        setRotationAngle(coralBlue1_r1, 0.0F, 0.7854F, 0.0F);
        coralBlue1_r1.cubeList.add(new ModelBox(coralBlue1_r1, 83, 16, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralRed = new ModelRenderer(this);
        coralRed.setRotationPoint(1.0F, 26.0F, 4.0F);

        coralRed2_r1 = new ModelRenderer(this);
        coralRed2_r1.setRotationPoint(-8.0F, -2.0F, 5.0F);
        coralRed.addChild(coralRed2_r1);
        setRotationAngle(coralRed2_r1, 0.0F, -0.7854F, 0.0F);
        coralRed2_r1.cubeList.add(new ModelBox(coralRed2_r1, 95, 27, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        coralRed1_r1 = new ModelRenderer(this);
        coralRed1_r1.setRotationPoint(-8.0F, -2.0F, 5.0F);
        coralRed.addChild(coralRed1_r1);
        setRotationAngle(coralRed1_r1, 0.0F, 0.7854F, 0.0F);
        coralRed1_r1.cubeList.add(new ModelBox(coralRed1_r1, 83, 27, 0.0F, -10.0F, -3.0F, 0, 11, 6, 0.0F, false));

        this.shell.addChild(this.coralYellow);
        this.shell.addChild(this.coralPink);
        this.shell.addChild(this.coralBlue);
        this.shell.addChild(this.coralRed);
    }
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.coralYellow.render(scale);
        this.coralPink.render(scale);
        this.coralBlue.render(scale);
        this.coralRed.render(scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    { super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn); }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}