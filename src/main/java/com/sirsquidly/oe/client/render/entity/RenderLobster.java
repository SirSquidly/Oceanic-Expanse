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
import com.sirsquidly.oe.client.render.entity.layer.LayerLobsterSaddle;
import com.sirsquidly.oe.entity.EntityLobster;

@SideOnly(Side.CLIENT)
public class RenderLobster extends RenderLiving<EntityLobster>
{
	public static ModelLobster model = new ModelLobster();
	
	public RenderLobster(RenderManager manager)
    { 
		super(manager, model, 0.5F);
		this.addLayer(new LayerLobsterHalf2(this));
		this.addLayer(new LayerLobsterSaddle(this));
    }

	protected void preRenderCallback(EntityLobster entity, float f) {
		float sizeDefault = 0.9375F;
		float size = (sizeDefault/1.25F) + ((float)entity.getSize() * 0.1F - 0.1F);
		float crabRotation = 0.0F;
		
		if (entity.getGrowingAge() < 0)
        {
			size = (float)((double)size * 0.5D);
            this.shadowSize = 0.2F;
        }
        else { this.shadowSize = size * 0.35F; }
        
		GlStateManager.rotate(crabRotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(size, size, size);
	}
	
	protected ResourceLocation getEntityTexture(EntityLobster entity)
	{ return  new ResourceLocation(Main.MOD_ID + ":textures/entities/lobster/lobster_" + (entity.getLobsterVariant() & 255) + "a.png"); }

    protected void applyRotations(EntityLobster entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    { super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks); }
}