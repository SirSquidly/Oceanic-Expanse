package com.sirsquidly.oe.entity.render;

import com.sirsquidly.oe.entity.EntityGlowSquid;
import com.sirsquidly.oe.entity.render.layer.LayerGlowSquid;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGlowSquid extends RenderLiving<EntityGlowSquid>
{
	public static final ResourceLocation GLOWSQUID_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/glow_squid.png");
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RenderGlowSquid(RenderManager manager)
    {
        super(manager, new ModelSquid(), 0.7F);
        this.addLayer(new LayerGlowSquid(this));
    }

	protected ResourceLocation getEntityTexture(EntityGlowSquid entity) {
		return GLOWSQUID_TEXTURE;
	}

    protected void applyRotations(EntityGlowSquid entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        float f = entityLiving.prevSquidPitch + (entityLiving.squidPitch - entityLiving.prevSquidPitch) * partialTicks;
        float f1 = entityLiving.prevSquidYaw + (entityLiving.squidYaw - entityLiving.prevSquidYaw) * partialTicks;
        GlStateManager.translate(0.0F, 0.5F, 0.0F);
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -1.2F, 0.0F);
    }

    protected float handleRotationFloat(EntityGlowSquid livingBase, float partialTicks)
    {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}
