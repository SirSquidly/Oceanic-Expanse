package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderTropicalSlime;
import com.sirsquidly.oe.entity.EntityTropicalSlime;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTropicalSlimeFish implements LayerRenderer<EntityTropicalSlime>
{
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/tropical_slime_fish.png");
    private final RenderTropicalSlime creeperRenderer;
    private final ModelBase slimeModel = new ModelSlime(16);

    public LayerTropicalSlimeFish(RenderTropicalSlime creeperRendererIn)
    {
        this.creeperRenderer = creeperRendererIn;
    }

    public void doRenderLayer(EntityTropicalSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	/**
        boolean flag = entitylivingbaseIn.isInvisible();
        GlStateManager.depthMask(!flag);
        this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
        float f1 = MathHelper.cos(f * 0.02F) * 1.0F;
        float f2 = f * 0.01F;
        
        f = ageInTicks * 0.12F;
        
        while (f > 0.375F) { f -= 0.375F; }
        
        GlStateManager.translate(-f * 0.005F, 0.0F, 0.0F);
        GlStateManager.scale(2F, 1.0F, 1.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        //float f1 = 0.5F;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        this.slimeModel.setModelAttributes(this.creeperRenderer.getMainModel());
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.slimeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale + 0.0001F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(flag);
        **/
        
        
        int a = (int) ageInTicks / 2;
		while (a > 48) { a -= 48; }
		
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		
		this.creeperRenderer.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/slime/tropical_fish_layer/tropical_slime_fish" + a + ".png"));

    	this.slimeModel.setModelAttributes(this.creeperRenderer.getMainModel());
        this.slimeModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.slimeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale + 0.0001F);
        
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}