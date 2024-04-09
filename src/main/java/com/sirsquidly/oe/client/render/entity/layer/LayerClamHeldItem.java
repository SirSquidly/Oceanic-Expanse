package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.client.render.entity.RenderClam;
import com.sirsquidly.oe.entity.EntityClam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * I tried re-writing this accursed renderer multiple times now, it is just horrendous.
 */
@SideOnly(Side.CLIENT)
public class LayerClamHeldItem implements LayerRenderer<EntityClam>
{
    protected final RenderClam clamRenderer;

    public LayerClamHeldItem(RenderClam clamRendererIn)
    { this.clamRenderer = clamRendererIn; }

    public void doRenderLayer(EntityClam clamEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean flag = clamEntity.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? clamEntity.getHeldItemOffhand() : clamEntity.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? clamEntity.getHeldItemMainhand() : clamEntity.getHeldItemOffhand();

        if ((!itemstack.isEmpty() || !itemstack1.isEmpty()) && clamEntity.getClientOpenAmount(partialTicks) > 0.075)
        {
            GlStateManager.pushMatrix();
            this.renderHeldItem(clamEntity, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(clamEntity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(EntityLivingBase entity, ItemStack item, ItemCameraTransforms.TransformType icamtrans, EnumHandSide handSide)
    {
    	Minecraft minecraft = Minecraft.getMinecraft();
    	
        if (!item.isEmpty())
        {
        	GlStateManager.pushMatrix();
            
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate(0.0F, 1.15F, -0.1F);

            //GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            
            
            
            minecraft.getItemRenderer().renderItemSide(entity, item, ItemCameraTransforms.TransformType.FIXED, flag);
            
            //minecraft.getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    { return false; }
}