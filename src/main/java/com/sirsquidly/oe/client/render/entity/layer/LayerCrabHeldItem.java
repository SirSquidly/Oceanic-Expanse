package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.client.model.entity.ModelCrab;
import com.sirsquidly.oe.client.render.entity.RenderCrab;
import com.sirsquidly.oe.entity.EntityCrab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCrabHeldItem implements LayerRenderer<EntityCrab>
{
    protected final RenderCrab crabRenderer;

    public LayerCrabHeldItem(RenderCrab crabRendererIn)
    { this.crabRenderer = crabRendererIn; }

    public void doRenderLayer(EntityCrab crabRendererIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean flag = crabRendererIn.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? crabRendererIn.getHeldItemOffhand() : crabRendererIn.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? crabRendererIn.getHeldItemMainhand() : crabRendererIn.getHeldItemOffhand();

        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (this.crabRenderer.getMainModel().isChild)
            {
                //float f = 0.5F;
                GlStateManager.translate(0.0F, 0.75F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(crabRendererIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(crabRendererIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(EntityLivingBase entity, ItemStack item, ItemCameraTransforms.TransformType icamtrans, EnumHandSide handSide)
    {
    	Minecraft minecraft = Minecraft.getMinecraft();
    	
        if (!item.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (entity.isSneaking())
            { GlStateManager.translate(0.0F, 0.2F, 0.0F); }
            
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((flag ? -1 : 1) * 0.1F, 1.2F, -0.2F);
            this.translateToHand(handSide);
            
            //GlStateManager.translate((flag ? -1 : 1) * 0.3F, -0.468F, -1.2F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            
            minecraft.getItemRenderer().renderItemSide(entity, item, icamtrans, flag);
            GlStateManager.popMatrix();
        }
    }
    
    protected void translateToHand(EnumHandSide side)
    {
        ((ModelCrab)this.crabRenderer.getMainModel()).getClaw(side).postRender(0.0625F);
    }

    public boolean shouldCombineTextures()
    { return false; }
}