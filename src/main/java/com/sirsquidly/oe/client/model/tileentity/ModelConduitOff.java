package com.sirsquidly.oe.client.model.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelConduitOff extends ModelBase
{
    public ModelRenderer smlBox;

    public ModelConduitOff()
    {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.smlBox = new ModelRenderer(this, 0, 0);
        this.smlBox.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.smlBox.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
    { this.smlBox.render(f5); }
}