package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelLobster;
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
	private final ModelLobster model = new ModelLobster();
	
	public LayerLobsterHalf2(RenderLobster lobsterRendererIn)
    { this.lobsterRenderer = lobsterRendererIn; }

	public void doRenderLayer(EntityLobster entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		
		if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.getLobsterVariant() > 1)
		{
			//entitylivingbaseIn.getLobsterVariant()
			this.lobsterRenderer.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/lobster/lobster_" + (0) + "a.png"));

			this.model.setModelAttributes(this.lobsterRenderer.getMainModel());
			
			
			this.model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
			this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	        //this.model.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
	        
		}
    }

    public boolean shouldCombineTextures()
    { return true; }
}