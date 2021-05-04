package com.sirsquidly.oe.entity.render.layer;

import com.sirsquidly.oe.entity.EntityPickled;
import com.sirsquidly.oe.entity.render.RenderPickled;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerPickled implements LayerRenderer<EntityPickled>
{
	public static final ResourceLocation PICKLED_ZOMBIE_BRIGHT_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled_bright.png");
	private final RenderPickled PickledRenderer;
	
	public LayerPickled(RenderPickled PickledRendererIn)
    {
		this.PickledRenderer = PickledRendererIn;
    }

	// Playing with the Alpha causes the entire mob to glow, so theya re commented out
	public void doRenderLayer(EntityPickled entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	 {
		if (!(entitylivingbaseIn.isDry()))
        {
		this.PickledRenderer.bindTexture(PICKLED_ZOMBIE_BRIGHT_TEXTURE);
		GlStateManager.enableBlend();
		//GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.PickledRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.PickledRenderer.setLightmap(entitylivingbaseIn);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        //GlStateManager.enableAlpha();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
