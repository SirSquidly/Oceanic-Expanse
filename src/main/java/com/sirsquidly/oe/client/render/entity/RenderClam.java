package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelClam;
import com.sirsquidly.oe.entity.EntityClam;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderClam extends RenderLiving<EntityClam>
{
	public static final ResourceLocation CLAM_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/clam.png");
    
	public RenderClam(RenderManager manager)
    {
        super(manager, new ModelClam(), 0.7F);
    }

	protected ResourceLocation getEntityTexture(EntityClam entity) 
	{
		return CLAM_TEXTURE;
	}
	
    protected void applyRotations(EntityClam entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
    	if (entityLiving.getShaking())
    	{
    		rotationYaw += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * 0.5D);
    	}
    	
    	super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}