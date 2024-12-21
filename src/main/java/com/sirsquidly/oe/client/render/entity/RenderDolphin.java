package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelDolphin;
import com.sirsquidly.oe.entity.EntityDolphin;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDolphin extends RenderLiving<EntityDolphin>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/dolphin/dolphin.png");

	public RenderDolphin(RenderManager manager)
    {
        super(manager, new ModelDolphin(), 0.2F);
    }

	@Override
	protected void preRenderCallback(EntityDolphin entity, float f)
	{
		float size = 0.9375F;
		
		if (entity.getGrowingAge() < 0)
        {
			size = (float)((double)size * 0.5D);
            this.shadowSize = 0.25F;
        }

		GlStateManager.scale(size, size, size);
	}
	
	protected ResourceLocation getEntityTexture(EntityDolphin entity)
	{ return TEXTURES; }

    protected void applyRotations(EntityDolphin entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    { super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks); }
}