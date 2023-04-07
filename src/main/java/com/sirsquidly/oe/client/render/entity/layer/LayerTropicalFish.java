package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelTropicalFishA;
import com.sirsquidly.oe.client.model.entity.ModelTropicalFishB;
import com.sirsquidly.oe.client.render.entity.RenderTropicalFish;
import com.sirsquidly.oe.entity.EntityTropicalFish;

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
	private final ModelTropicalFishA modelA = new ModelTropicalFishA();
	private final ModelTropicalFishB modelB = new ModelTropicalFishB();
	
	public LayerTropicalFish(RenderTropicalFish tropicalFishRendererIn)
    {
		this.tropicalFishRenderer = tropicalFishRendererIn;
    }

	public void doRenderLayer(EntityTropicalFish entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		if (!entitylivingbaseIn.isInvisible())
		{
			String getModel = (entitylivingbaseIn.getTropicalFishVariant() & 255) == 0 ? "a" : "b";
			
			this.tropicalFishRenderer.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/tropical_fish/tropical_fish_" + getModel + "_p" + (entitylivingbaseIn.getTropicalFishVariant() >> 8 & 255) + ".png"));
			float[] afloat = EnumDyeColor.byMetadata(entitylivingbaseIn.getTropicalFishVariant() >> 24 & 255).getColorComponentValues();
			
	        GlStateManager.color(afloat[0], afloat[1], afloat[2]);
			
	        /** Bonus check to only really apply the pattern if it's within the acceptable range. Otherwise, fish outside of range (only possible via commands) would display the missing texture.  */
	        if ((entitylivingbaseIn.getTropicalFishVariant() >> 8 & 255) <= 5)
	        {
	        	if (getModel == "a")
		        {
		        	this.modelA.setModelAttributes(this.tropicalFishRenderer.getMainModel());
			        this.modelA.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
			        this.modelA.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		        }
		        else
		        {
		        	this.modelB.setModelAttributes(this.tropicalFishRenderer.getMainModel());
			        this.modelB.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
			        this.modelB.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		        }
	        }
		}
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}