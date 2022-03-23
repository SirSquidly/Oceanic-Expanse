package com.sirsquidly.oe.entity.render.layer;

import com.sirsquidly.oe.entity.EntityGlowSquid;
import com.sirsquidly.oe.entity.render.RenderGlowSquid;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGlowSquid<T extends EntityGlowSquid> implements LayerRenderer<T>
{
	public static final ResourceLocation GLOWSQUID_BRIGHT_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/glow_squid_bright.png");
	private final RenderGlowSquid GlowSquidRenderer;
	
	public LayerGlowSquid(RenderGlowSquid GlowSquidRendererIn)
    {
		this.GlowSquidRenderer = GlowSquidRendererIn;
    }

	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.GlowSquidRenderer.bindTexture(GLOWSQUID_BRIGHT_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        
        if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomNameTag()))
        {
            int m = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
            int n = EnumDyeColor.values().length;
            int o = m % n;
            int p = (m + 1) % n;
            float f = ((float)(entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
            float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(o));
            float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(p));
            GlStateManager.color(afloat1[0] * (2.5F - f) + afloat2[0] * f, afloat1[1] * (1.5F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
        }
        else
        {
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        
        
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
        return false;
    }
}
