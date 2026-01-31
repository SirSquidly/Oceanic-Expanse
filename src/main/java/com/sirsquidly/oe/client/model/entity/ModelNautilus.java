package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.common.entity.AbstractFish;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNautilus extends ModelBase
{
    public final ModelRenderer main;
    public final ModelRenderer shell;
    public final ModelRenderer lid;
    public final ModelRenderer head;
    public final ModelRenderer tentaclesMiddle;
    public final ModelRenderer tentaclesUpper;
    public final ModelRenderer tentaclesLower;


    public ModelNautilus()
    {
        textureWidth = 128;
        textureHeight = 128;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);

        shell = new ModelRenderer(this);
        shell.setRotationPoint(0.0F, 0.0F, 11.0F);
        main.addChild(shell);
        shell.cubeList.add(new ModelBox(shell, 0, 0, -7.0F, -18.0F, -18.0F, 14, 10, 16, 0.0F, false));
        shell.cubeList.add(new ModelBox(shell, 0, 26, -7.0F, -8.0F, -22.0F, 14, 8, 20, 0.0F, false));
        shell.cubeList.add(new ModelBox(shell, 48, 26, -7.0F, -8.0F, -15.0F, 14, 8, 0, 0.0F, false));

        lid = new ModelRenderer(this);
        lid.setRotationPoint(0.0F, -9.0F, -17.0F);
        shell.addChild(lid);
        setRotationAngle(lid, 0.2182F, 0.0F, 0.0F);
        lid.cubeList.add(new ModelBox(lid, 44, 0, -6.0F, -1.2164F, -6.9763F, 12, 3, 7, 0.0F, false));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -4.0F, -4.0F);
        main.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 54, -5.0F, -4.0F, -9.0F, 10, 8, 11, -0.01F, false));

        tentaclesMiddle = new ModelRenderer(this);
        tentaclesMiddle.setRotationPoint(0.0F, 0.0F, -9.0F);
        head.addChild(tentaclesMiddle);
        tentaclesMiddle.cubeList.add(new ModelBox(tentaclesMiddle, 0, 91, -3.0F, -2.0F, -4.0F, 6, 4, 6, 0.0F, false));

        tentaclesUpper = new ModelRenderer(this);
        tentaclesUpper.setRotationPoint(0.0F, -2.0F, -9.0F);
        head.addChild(tentaclesUpper);
        tentaclesUpper.cubeList.add(new ModelBox(tentaclesUpper, 0, 73, -5.0F, -2.0F, -4.0F, 10, 4, 5, -0.02F, false));

        tentaclesLower = new ModelRenderer(this);
        tentaclesLower.setRotationPoint(0.0F, 2.0F, -9.0F);
        head.addChild(tentaclesLower);
        tentaclesLower.cubeList.add(new ModelBox(tentaclesLower, 0, 82, -5.0F, -2.0F, -4.0F, 10, 4, 5, -0.02F, false));
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

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        AbstractFish fish = (AbstractFish) entityIn;
        float swim = MathHelper.cos((ageInTicks) * 0.3F);

        this.lid.rotateAngleX = 0.2182F + headPitch * (float) (Math.PI / 180.0);
        this.head.rotateAngleX = headPitch * (float) (Math.PI / 180.0);
        float clampedYaw = MathHelper.clamp(netHeadYaw, -15.0F, 15.0F);
        this.head.rotateAngleY = clampedYaw * (float) (Math.PI / 180.0);

        if ((fish.motionX * fish.motionX + fish.motionY * fish.motionY + fish.motionZ * fish.motionZ > 0.01F))
        {
            //this.main.rotateAngleX -= 0.05F * swim;
        }

        float moveSpeed = 0.1F;
        float flap = (float)(Math.pow(Math.sin(Math.pow((ageInTicks * moveSpeed) % Math.PI / Math.PI,3F) * Math.PI), 2F) * 0.6F);

        this.head.offsetZ = -flap/6;

        this.tentaclesMiddle.offsetZ = -flap/8;

        this.tentaclesUpper.rotateAngleX = -flap;
        this.tentaclesLower.rotateAngleX = flap;
    }
}