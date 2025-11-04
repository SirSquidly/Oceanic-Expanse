package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelNautilus;
import com.sirsquidly.oe.entity.EntityZombieNautilus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieNautilus extends RenderLiving<EntityZombieNautilus>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/nautilus/zombie_nautilus.png");

	public RenderZombieNautilus(RenderManager manager)
    {
        super(manager, new ModelNautilus(), 0.2F);
    }

	@Override
	protected void preRenderCallback(EntityZombieNautilus entity, float f)
	{
		float size = 0.9375F;
		
		if (entity.getGrowingAge() < 0)
        {
			size = (float)((double)size * 0.5D);
            this.shadowSize = 0.25F;
        }

		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

		GlStateManager.scale(size, size, size);
	}
	
	protected ResourceLocation getEntityTexture(EntityZombieNautilus entity)
	{ return TEXTURES; }

    protected void applyRotations(EntityZombieNautilus entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    { super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks); }
}