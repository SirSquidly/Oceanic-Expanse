package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelNautilusCoral;
import com.sirsquidly.oe.client.render.entity.layer.LayerNautilusCoral;
import com.sirsquidly.oe.entity.EntityZombieNautilus;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieNautilus extends RenderNautilus<EntityZombieNautilus>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/nautilus/zombie_nautilus.png");
	public static final ResourceLocation WARM_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/nautilus/zombie_nautilus_coral.png");
	private final ModelNautilusCoral modelCoral = new ModelNautilusCoral();

	public RenderZombieNautilus(RenderManager manager)
    {
		super(manager);
		this.addLayer(new LayerNautilusCoral(this, modelCoral));
    }

	protected ResourceLocation getEntityTexture(EntityZombieNautilus entity)
	{ return WARM_TEXTURE; }
}