package com.sirsquidly.oe.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sirsquidly.oe.entity.EntityTrident;
import com.sirsquidly.oe.init.OEItems;
	
@SideOnly(Side.CLIENT)
public class RenderTrident extends Render<EntityTrident>
{
	protected final Item item;
	
	public RenderTrident(RenderManager manager)
    { 
		super(manager);
		this.item = OEItems.TRIDENT_ORIG;
    }
	
	@Override
	public void doRender(EntityTrident entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, -1.0F);
        Minecraft.getMinecraft().getRenderItem().renderItem(entity.getItem(), TransformType.NONE);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
	
	public ItemStack getStackToRender(EntityTrident entityIn)
    {
        return entityIn.getItem();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityTrident entity)
	{
		return null;
	}
}