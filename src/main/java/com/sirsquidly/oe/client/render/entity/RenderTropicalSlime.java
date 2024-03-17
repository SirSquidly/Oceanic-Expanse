package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.layer.LayerTropicalSlimeFish;
import com.sirsquidly.oe.client.render.entity.layer.LayerTropicalSlimeGel;
import com.sirsquidly.oe.entity.EntityTropicalSlime;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTropicalSlime extends RenderLiving<EntityTropicalSlime>
{
	private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/tropical_slime.png");
    
	public RenderTropicalSlime(RenderManager manager)
    {
        super(manager, new ModelSlime(16), 0.25F);
        
        this.addLayer(new LayerTropicalSlimeFish(this));
        this.addLayer(new LayerTropicalSlimeGel(this));
    }

	protected ResourceLocation getEntityTexture(EntityTropicalSlime entity) 
	{
		return SLIME_TEXTURES;
	}
	
    public void doRender(EntityTropicalSlime entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.shadowSize = 0.25F * (float)entity.getSlimeSize();
        
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(EntityTropicalSlime entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.999F, 0.999F, 0.999F);
        float f1 = (float)entitylivingbaseIn.getSlimeSize();
        float f2 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        GlStateManager.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }
}