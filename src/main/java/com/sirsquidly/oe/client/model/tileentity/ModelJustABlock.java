package com.sirsquidly.oe.client.model.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelJustABlock extends ModelBase
{
    public ModelRenderer block;

    public ModelJustABlock()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.block = new ModelRenderer(this, 0, 0);
        this.block.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.block.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    { this.block.render(f5); }
}