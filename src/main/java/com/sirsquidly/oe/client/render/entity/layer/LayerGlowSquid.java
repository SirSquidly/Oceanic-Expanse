package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.client.render.entity.RenderGlowSquid;
import com.sirsquidly.oe.entity.EntityGlowSquid;
import com.sirsquidly.oe.util.Reference;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGlowSquid<T extends EntityGlowSquid> implements LayerRenderer<T>
{
	public static final ResourceLocation GLOWSQUID_BRIGHT_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/squid/glow_squid_bright.png");
	private final RenderGlowSquid GlowSquidRenderer;
	
	public LayerGlowSquid(RenderGlowSquid GlowSquidRendererIn)
    {
		this.GlowSquidRenderer = GlowSquidRendererIn;
    }

	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		int fullBright = 983055 * ConfigHandler.entity.glowSquid.glowSquidLayerBright;
		
        this.GlowSquidRenderer.bindTexture(GLOWSQUID_BRIGHT_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        int i = (int)(fullBright * entitylivingbaseIn.currBrightness);
        
        if (entitylivingbaseIn.getBrightnessForRender() > i) 
        {
        	i = entitylivingbaseIn.getBrightnessForRender();
        }
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.GlowSquidRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.GlowSquidRenderer.setLightmap(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}