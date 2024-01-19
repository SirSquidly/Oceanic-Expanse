package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderTropicalSlime;
import com.sirsquidly.oe.entity.EntityTropicalSlime;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTropicalSlimeGel implements LayerRenderer<EntityTropicalSlime>
{
	private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/tropical_slime.png");
    private final RenderTropicalSlime slimeRenderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerTropicalSlimeGel(RenderTropicalSlime slimeRendererIn)
    {
        this.slimeRenderer = slimeRendererIn;
    }

    public void doRenderLayer(EntityTropicalSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isInvisible())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            //GlStateManager.pushMatrix();
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            
            GlStateManager.depthMask(true);
            
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
           // if (entitylivingbaseIn.world.getCurrentMoonPhaseFactor() == 0.0F) GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.slimeRenderer.bindTexture(SLIME_TEXTURES);
            this.slimeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
            //GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    { return true; }
}