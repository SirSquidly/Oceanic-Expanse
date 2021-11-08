package com.sirsquidly.oe.entity.render;

import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.entity.model.ModelDrowned;
import com.sirsquidly.oe.entity.render.layer.LayerDrowned;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDrowned extends RenderLiving<EntityDrowned>
{
	public static final ResourceLocation DROWNED_ZOMBIE_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/drowned.png");
	
	public RenderDrowned(RenderManager manager)
    {
		super(manager, new ModelDrowned(), 0.5F);
        this.addLayer(new LayerCustomHead(new ModelDrowned().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerHeldItem(this)
        {
			public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
			{
				GlStateManager.pushMatrix();

				if (entitylivingbaseIn.getHeldItemMainhand().getItem() == OEItems.TRIDENT_ORIG && ((EntityZombie)entitylivingbaseIn).isArmsRaised())
	            {
	            	GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
	            	GlStateManager.translate(0.0F, 0.5F, -0.8F);
	            }
				super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
				GlStateManager.popMatrix();
			}
        });
        this.addLayer(new LayerDrowned(this));
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

	
	public ModelZombie getMainModel()
    {
        return (ModelZombie)super.getMainModel();
    }
	
	protected ResourceLocation getEntityTexture(EntityDrowned entity) {
		return DROWNED_ZOMBIE_TEXTURE;
	}
	
	@Override
	protected void preRenderCallback(EntityDrowned entity, float f) {
		float size = 0.9375F;
		
		if (entity.isSwimming())
        {
			GL11.glRotatef(0F, 0F, 45F, 1F);
        }
		GlStateManager.scale(size, size, size);
	}
}
