package com.sirsquidly.oe.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.client.model.entity.ModelTropicalFishA;
import com.sirsquidly.oe.client.render.entity.layer.LayerTropicalFish;
import com.sirsquidly.oe.entity.EntityTropicalFish;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTropicalFish extends RenderLiving<EntityTropicalFish>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/entities/tropical_fish/tropical_fish_a.png");
	
	public RenderTropicalFish(RenderManager manager)
    {
        super(manager, new ModelTropicalFishA(), 0.2F);
        this.addLayer(new LayerTropicalFish(this));
    }

	@Override
	protected void preRenderCallback(EntityTropicalFish entity, float f) {
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
		
		float[] afloat = EnumDyeColor.byMetadata(entity.getTropicalFishVariant() >> 16 & 255).getColorComponentValues();
		
        GlStateManager.color(afloat[0], afloat[1], afloat[2]);
		GlStateManager.scale(size, size, size);
	}
	
	protected ResourceLocation getEntityTexture(EntityTropicalFish entity)
	{
		return TEXTURES;
	}

    protected void applyRotations(EntityTropicalFish entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}