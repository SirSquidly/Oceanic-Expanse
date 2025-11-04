package com.sirsquidly.oe.client.model.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelNautilusShellBlock extends ModelBase
{
    private final ModelRenderer main;
    private final ModelRenderer shell;
    private final ModelRenderer lid;

    public ModelNautilusShellBlock()
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
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.main.rotateAngleY = netHeadYaw * 0.017453292F;
        this.main.rotateAngleX = headPitch * 0.017453292F;

        main.render(scale);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}