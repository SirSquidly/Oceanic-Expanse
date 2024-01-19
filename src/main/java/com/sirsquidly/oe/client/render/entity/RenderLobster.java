package com.sirsquidly.oe.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelLobster;
import com.sirsquidly.oe.client.render.entity.layer.LayerLobsterHalf2;
import com.sirsquidly.oe.entity.EntityLobster;

@SideOnly(Side.CLIENT)
public class RenderLobster extends RenderLiving<EntityLobster>
{
	public static final ResourceLocation textureA = new ResourceLocation(Main.MOD_ID + ":textures/entities/lobster/lobster_0.png");
	public static final ResourceLocation textureB = new ResourceLocation(Main.MOD_ID + ":textures/entities/lobster/lobster_1.png");
	public static ModelLobster model = new ModelLobster();
	
	public RenderLobster(RenderManager manager)
    { 
		super(manager, model, 0.5F);
		this.addLayer(new LayerLobsterHalf2(this));
    }

	protected void preRenderCallback(EntityLobster entity, float f) {
		float size = 0.9375F;
		float crabRotation = 0.0F;
		
		if (entity.getGrowingAge() < 0)
        {
			size = (float)((double)size * 0.5D);
            this.shadowSize = 0.2F;
        }
        else { this.shadowSize = 0.35F; }
        
		GlStateManager.rotate(crabRotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(size, size, size);
	}
	
	protected ResourceLocation getEntityTexture(EntityLobster entity)
	{ return entity.getLobsterVariant() == 0 ? textureA : textureB; }

    protected void applyRotations(EntityLobster entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    { super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks); }
}