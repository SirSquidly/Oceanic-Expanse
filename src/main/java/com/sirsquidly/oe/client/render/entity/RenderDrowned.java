package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.client.render.entity.layer.LayerRiptideAnim;
import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelDrowned;
import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.client.render.entity.layer.LayerDrowned;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDrowned<T extends EntityDrowned> extends RenderLiving<T>
{
	public static final ResourceLocation DROWNED_ZOMBIE_CAPTAIN_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned_captain.png");
	public static final ResourceLocation DROWNED_ZOMBIE_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned.png");
	private static final ResourceLocation[] DROWNED_EMISSIVE_TEXTURE = new ResourceLocation[] { new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned_e.png"), new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned_captain_e.png") };

	/** This is separate so the same model is used for LayerCustomHead */
	public static ModelDrowned model = new ModelDrowned();
	
	public RenderDrowned(RenderManager manager)
    {
		super(manager, model, 0.5F);
        this.addLayer(new LayerCustomHead(model.bipedHead));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerHeldItem(this));

		this.addLayer(new LayerRiptideAnim(this));
        
        if (ConfigHandler.entity.drowned.drownedGlowLayer)
        { this.addLayer(new LayerDrowned(this)); }
        
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelDrowned(0.5F, true);
                this.modelArmor = new ModelDrowned(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

	
	public ModelZombie getMainModel()
    {
        return (ModelZombie)super.getMainModel();
    }
	
	protected ResourceLocation getEntityTexture(T entity)
	{ return entity.isCaptain() && ConfigHandler.entity.drowned.drownedCaptain.enableDrownedCaptainTexture ? DROWNED_ZOMBIE_CAPTAIN_TEXTURE : DROWNED_ZOMBIE_TEXTURE; }

	/** Returns the emissive texture to be rendered within `LayerDrowned` */
	public ResourceLocation getEntityEmissiveTexture(T entity)
	{ return DROWNED_EMISSIVE_TEXTURE[entity.isCaptain() ? 1: 0]; }
	
	@Override
	protected void preRenderCallback(T entity, float f)
	{
		float size = 0.9375F;
		GlStateManager.scale(size, size, size);
		if (entity.getRiptideUseTime() > 0)
		{
			float spinSpeed = 50.0F;

			GlStateManager.rotate(90.0F - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate((entity.ticksExisted + f) * -spinSpeed, 0.0F, 1.0F, 0.0F);
		}
		else
		{
			float bobbing = MathHelper.cos(entity.ticksExisted * 0.09F) * 0.05F + 0.05F;
			float swimAngle = entity.getClientSwimTime(f) * entity.rotationPitch;

			if (entity.isInWater() && ConfigHandler.entity.drowned.enableDrownedSwimAnims)
			{
				GL11.glRotatef(-bobbing*20, 1F, 0F, 0F);
				GL11.glTranslatef(0F, bobbing/2 - 0.1F, 0F);
			}
			if (entity.isInWater() && entity.getClientSwimTime(f) != 0)
			{
				GL11.glRotatef(90.0F * entity.getClientSwimTime(f), 1F, 0F, 0F);
				GL11.glTranslatef(0F, 1F * entity.getClientSwimTime(f), (1F + (swimAngle/30)) * entity.getClientSwimTime(f));
				GL11.glRotatef(swimAngle * entity.getClientSwimTime(f), 1F, 0F, 0F);
			}
		}
	}
}
