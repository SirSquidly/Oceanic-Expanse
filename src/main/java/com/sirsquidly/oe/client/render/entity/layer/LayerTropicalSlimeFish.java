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
public class LayerTropicalSlimeFish implements LayerRenderer<EntityTropicalSlime>
{
    private final RenderTropicalSlime tropicalSlimeRenderer;
    private final ModelBase slimeModel = new ModelSlime(16);

    public LayerTropicalSlimeFish(RenderTropicalSlime creeperRendererIn)
    {
        this.tropicalSlimeRenderer = creeperRendererIn;
    }

    public void doRenderLayer(EntityTropicalSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        int a = (int) ageInTicks / 2;
		while (a > 48) { a -= 48; }
		
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		
		this.tropicalSlimeRenderer.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/slime/tropical_fish_layer/tropical_slime_fish" + a + ".png"));

    	this.slimeModel.setModelAttributes(this.tropicalSlimeRenderer.getMainModel());
        this.slimeModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.slimeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale + 0.0001F);
    }

    public boolean shouldCombineTextures()
    { return true; }
}