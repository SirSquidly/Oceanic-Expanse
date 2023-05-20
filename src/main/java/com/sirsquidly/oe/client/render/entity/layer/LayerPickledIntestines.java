package com.sirsquidly.oe.client.render.entity.layer;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sirsquidly.oe.client.model.entity.ModelPickledIntestines;
import com.sirsquidly.oe.client.render.entity.RenderPickled;
import com.sirsquidly.oe.entity.EntityPickled;

@SideOnly(Side.CLIENT)
public class LayerPickledIntestines implements LayerRenderer<EntityPickled>
{
	private final RenderPickled pickledRenderer;
	private final ModelPickledIntestines pickledIntestinesModel;
	
	public LayerPickledIntestines(RenderPickled pickledRendererIn, ModelPickledIntestines modelPickledIntestinesIn)
    {
		this.pickledRenderer = pickledRendererIn;
		this.pickledIntestinesModel = modelPickledIntestinesIn;
    }

	public void doRenderLayer(EntityPickled entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWet())
		{
	        this.pickledIntestinesModel.setModelAttributes(this.pickledRenderer.getMainModel());
	        this.pickledIntestinesModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
	        this.pickledIntestinesModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
    }

    public boolean shouldCombineTextures()
    { return true; }
}