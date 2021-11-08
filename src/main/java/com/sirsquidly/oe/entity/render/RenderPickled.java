package com.sirsquidly.oe.entity.render;

import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.entity.EntityPickled;
import com.sirsquidly.oe.entity.model.ModelPickled;
import com.sirsquidly.oe.entity.render.layer.LayerPickled;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPickled extends RenderLiving<EntityPickled>
{
	public static final ResourceLocation PICKLED_ZOMBIE_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled.png");
	public static final ResourceLocation PICKLED_ZOMBIE_DRY_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled_dry.png");
	public static final ResourceLocation PICKLED_SECRET_TEXTURE1 = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled_spec1.png");
	
	public RenderPickled(RenderManager manager)
    {
		super(manager, new ModelPickled(), 0.5F);
		this.addLayer(new LayerPickled(this));
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }
	
	public ModelPickled getMainModel()
    {
        return (ModelPickled)super.getMainModel();
    }
	
	protected ResourceLocation getEntityTexture(EntityPickled entity) {
		String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());

        if (s != null && ("Rick".equals(s) || "Pickle Rick".equals(s) || "Pickled Rick".equals(s)))
        {
            return PICKLED_SECRET_TEXTURE1;
        }
        if(entity.isDry())
        {
        	return PICKLED_ZOMBIE_DRY_TEXTURE;
        }
		return PICKLED_ZOMBIE_TEXTURE;
	}
	
	@Override
	protected void preRenderCallback(EntityPickled entity, float f) {
		float size = 0.9375F;
		
		if (entity.isSwimming())
        {
			GL11.glRotatef(0F, 0F, 45F, 1F);
        }
		GlStateManager.scale(size, size, size);
	}
	
	protected void applyRotations(EntityPickled entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        if (entityLiving.isDry())
        {
            rotationYaw += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}
