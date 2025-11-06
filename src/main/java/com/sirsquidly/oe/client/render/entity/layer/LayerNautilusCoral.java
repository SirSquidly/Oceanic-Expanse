package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.client.model.entity.ModelNautilusCoral;
import com.sirsquidly.oe.client.render.entity.RenderNautilus;
import com.sirsquidly.oe.client.render.entity.RenderZombieNautilus;
import com.sirsquidly.oe.entity.EntityNautilus;
import com.sirsquidly.oe.entity.EntityZombieNautilus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerNautilusCoral implements LayerRenderer<EntityNautilus>
{
	private final RenderNautilus nautilusRenderer;
	private final ModelNautilusCoral nautilusCoralModel;

	public LayerNautilusCoral(RenderNautilus nautilusRendererIn, ModelNautilusCoral nautilusCoralModelIn)
    {
		this.nautilusRenderer = nautilusRendererIn;
		this.nautilusCoralModel = nautilusCoralModelIn;
    }

	public void doRenderLayer(EntityNautilus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		if (!entity.isInvisible() && entity instanceof EntityZombieNautilus)
		{
			if (((EntityZombieNautilus) entity).getVariant() != 1) return;

			this.nautilusRenderer.bindTexture(RenderZombieNautilus.WARM_TEXTURE);
			this.nautilusCoralModel.setModelAttributes(this.nautilusRenderer.getMainModel());
			GlStateManager.pushMatrix();
			this.nautilusCoralModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
			this.nautilusCoralModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();
		}
    }

    public boolean shouldCombineTextures()
    { return true; }
}