package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelBabySquid;
import com.sirsquidly.oe.entity.EntityBabySquid;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySquid extends RenderLiving<EntityBabySquid>
{
	public static final ResourceLocation BABY_SQUID_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/squid/squid_baby.png");
	public static final ResourceLocation SQUID_TEXTURE = new ResourceLocation("textures/entity/squid.png");
    
	public RenderBabySquid(RenderManager manager)
    {
        super(manager, ConfigHandler.entity.babySquid.babySquidCustomModel ? new ModelBabySquid() : new ModelSquid(), 0.7F);
        if (ConfigHandler.entity.glowSquid.glowSquidLayer)
        {
        	//this.addLayer(new LayerGlowSquid(this));
        }
    }

	@Override
	protected void preRenderCallback(EntityBabySquid entity, float f)
	{
		float size = 0.9375F;
		float awshit = entity.tentacleAngle;
		
		if (!ConfigHandler.entity.babySquid.babySquidCustomModel)
		{
			size = (float)((double)size * 0.5D);
			GlStateManager.scale(size, size, size);
		}
		else
		{
			if (entity.isInWater())
			{
				GlStateManager.scale(size, size - awshit/3, size);
			}
			else
			{
				GlStateManager.scale(size, size, size);
			}
			
		}
	}
	
	protected ResourceLocation getEntityTexture(EntityBabySquid entity) 
	{
		return ConfigHandler.entity.babySquid.babySquidCustomModel ? BABY_SQUID_TEXTURE : SQUID_TEXTURE;
	}
	
    protected void applyRotations(EntityBabySquid entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        float f = entityLiving.prevSquidPitch + (entityLiving.squidPitch - entityLiving.prevSquidPitch) * partialTicks;
        float f1 = entityLiving.prevSquidYaw + (entityLiving.squidYaw - entityLiving.prevSquidYaw) * partialTicks;
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -0.5F, 0.0F);
    }

    protected float handleRotationFloat(EntityBabySquid livingBase, float partialTicks)
    {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}