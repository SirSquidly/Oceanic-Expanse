package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderLobster;
import com.sirsquidly.oe.entity.EntityLobster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerLobsterSaddle implements LayerRenderer<EntityLobster>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/lobster/lobster_saddle.png");
    private final RenderLobster lobsterRenderer;

	public LayerLobsterSaddle(RenderLobster lobsterRendererIn)
    {
        this.lobsterRenderer = lobsterRendererIn;
    }

    public void doRenderLayer(EntityLobster entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entitylivingbaseIn.getSaddled())
        {
        	float saddleScale = 1.1F;
        	
            this.lobsterRenderer.bindTexture(TEXTURE);
            this.lobsterRenderer.getMainModel().setModelAttributes(this.lobsterRenderer.getMainModel());
            GlStateManager.pushMatrix();
            GlStateManager.scale(saddleScale, saddleScale, saddleScale);
            GlStateManager.translate(0, -0.125, 0);
            this.lobsterRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}