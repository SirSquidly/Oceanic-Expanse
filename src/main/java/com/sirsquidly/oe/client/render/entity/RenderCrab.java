package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelCrab;
import com.sirsquidly.oe.entity.EntityCrab;
import com.sirsquidly.oe.client.render.entity.layer.LayerCrabHeldItem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCrab extends RenderLiving<EntityCrab>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/crab.png");
	
	public RenderCrab(RenderManager manager)
    { 
		super(manager, new ModelCrab(), 0.5F); 
		this.addLayer(new LayerCrabHeldItem(this));
	}

	protected void preRenderCallback(EntityCrab entity, float f)
	{
		float size = 0.9375F + (entity.isCarryingEgg() ? 0.0703125F : 0);
		float crabRotation = 90.0F;
		
		if (entity.getGrowingAge() < 0)
        {
			size = (float)((double)size * 0.5D);
            this.shadowSize = 0.25F;
        }
        else { this.shadowSize = 0.5F; }
        
		GlStateManager.rotate(crabRotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(size, size, size);
	}

	@Override
	public void doRender(EntityCrab entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ((ModelCrab)this.mainModel).holdingItem = !entity.getHeldItemMainhand().isEmpty();
		
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
	
	public void transformHeldFull3DItemLayer()
    { GlStateManager.translate(0.0F, 0.1875F, 0.0F); }
	
	protected ResourceLocation getEntityTexture(EntityCrab entity)
	{ return TEXTURES; }

    protected void applyRotations(EntityCrab entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    { super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks); }
}