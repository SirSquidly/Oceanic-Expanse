package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.client.model.entity.ModelTropicalFishA;
import com.sirsquidly.oe.client.render.entity.RenderTropicalFish;
import com.sirsquidly.oe.entity.EntityTropicalFish;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTropicalFish implements LayerRenderer<EntityTropicalFish>
{
	private final RenderTropicalFish tropicalFishRenderer;
	private final ModelTropicalFishA tropicalFishModel = new ModelTropicalFishA();
	
	public LayerTropicalFish(RenderTropicalFish tropicalFishRendererIn)
    {
		this.tropicalFishRenderer = tropicalFishRendererIn;
    }

	public void doRenderLayer(EntityTropicalFish entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		if (!entitylivingbaseIn.isInvisible())
		{
			this.tropicalFishRenderer.bindTexture(new ResourceLocation(Reference.MOD_ID + ":textures/entities/tropical_fish/tropical_fish_" + "a" + "_p" + (entitylivingbaseIn.getTropicalFishVariant() >> 8 & 255) + ".png"));
			float[] afloat = EnumDyeColor.byMetadata(entitylivingbaseIn.getTropicalFishVariant() >> 24 & 255).getColorComponentValues();
			
	        GlStateManager.color(afloat[0], afloat[1], afloat[2]);
			
	        this.tropicalFishModel.setModelAttributes(this.tropicalFishRenderer.getMainModel());
	        this.tropicalFishModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
	        this.tropicalFishModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}