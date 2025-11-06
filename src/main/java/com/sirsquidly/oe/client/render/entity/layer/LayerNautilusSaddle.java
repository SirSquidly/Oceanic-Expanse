package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderNautilus;
import com.sirsquidly.oe.entity.EntityNautilus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerNautilusSaddle implements LayerRenderer<EntityNautilus>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/nautilus/nautilus_saddle.png");
    private final RenderNautilus nautilusRenderer;

    public LayerNautilusSaddle(RenderNautilus nautilusRendererIn)
    {
        this.nautilusRenderer = nautilusRendererIn;
    }

    @Override
    public void doRenderLayer(EntityNautilus entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.getSaddle().isEmpty())
        {
            float saddleScale = 1.05F;

            this.nautilusRenderer.bindTexture(TEXTURE);
            this.nautilusRenderer.getMainModel().setModelAttributes(this.nautilusRenderer.getMainModel());
            GlStateManager.pushMatrix();
            GlStateManager.scale(saddleScale, saddleScale, saddleScale);
            GlStateManager.translate(0, -0.05, 0);
            this.nautilusRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() { return false; }
}