package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderLobster;
import com.sirsquidly.oe.entity.EntityLobster;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerLobsterHalf2 implements LayerRenderer<EntityLobster>
{
	private final RenderLobster lobsterRenderer;
	
	public LayerLobsterHalf2(RenderLobster lobsterRendererIn)
    { this.lobsterRenderer = lobsterRendererIn; }

	public void doRenderLayer(EntityLobster entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		if (!entitylivingbaseIn.isInvisible())
		{
			this.lobsterRenderer.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/lobster/lobster_" + entitylivingbaseIn.getLobsterVariant() + "b.png"));
			
			this.lobsterRenderer.getMainModel().setModelAttributes(this.lobsterRenderer.getMainModel());
			//this.lobsterRenderer.getMainModel().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
			this.lobsterRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
    }

    public boolean shouldCombineTextures()
    { return true; }
}