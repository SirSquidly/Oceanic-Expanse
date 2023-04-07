package com.sirsquidly.oe.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelCod;
import com.sirsquidly.oe.entity.EntityCod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCod extends RenderLiving<EntityCod>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/cod.png");
	
	public RenderCod(RenderManager manager)
    {
        super(manager, new ModelCod(), 0.2F);
    }

	@Override
	protected void preRenderCallback(EntityCod entity, float f) {
		float size = 0.9375F;
		
		if (entity.getGrowingAge() < 0)
        {
			size = (float)((double)size * 0.5D);
            this.shadowSize = 0.25F;
        }
		if (entity.isFlopping()) {
			GL11.glRotatef(90F, 0F, 0F, 1F);
			GL11.glTranslatef(0F, 0.15F, 0F);
		}
		GlStateManager.scale(size, size, size);
	}

	
	protected ResourceLocation getEntityTexture(EntityCod entity) {
		return TEXTURES;
	}

    protected void applyRotations(EntityCod entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}
