package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderDrowned;
import com.sirsquidly.oe.entity.EntityDrowned;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerDrowned implements LayerRenderer<EntityDrowned>
{
	public static final ResourceLocation DROWNED_ZOMBIE_BRIGHT_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned_bright.png");
	private final RenderDrowned DrownedRenderer;
	
	public LayerDrowned(RenderDrowned DrownedRendererIn)
    {
		this.DrownedRenderer = DrownedRendererIn;
    }

	// Playing with the Alpha causes the entire mob to glow, so theya re commented out
	public void doRenderLayer(EntityDrowned entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	 {
		this.DrownedRenderer.bindTexture(DROWNED_ZOMBIE_BRIGHT_TEXTURE);
		GlStateManager.enableBlend();
		//GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        //this.DrownedRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        
        
        this.DrownedRenderer.getMainModel().setModelAttributes(this.DrownedRenderer.getMainModel());
        this.DrownedRenderer.getMainModel().setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.DrownedRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.DrownedRenderer.setLightmap(entitylivingbaseIn);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        //GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
